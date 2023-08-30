package de.tobi1craft.concacity.entity.goal;

import de.tobi1craft.concacity.Concacity;
import de.tobi1craft.concacity.entity.ModEntities;
import de.tobi1craft.concacity.entity.custom.HelperEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.poi.PointOfInterestStorage;

import java.util.EnumSet;
import java.util.Objects;

public class MineTreeGoal extends Goal {
    private final MobEntity mob;
    private final HelperEntity helper;
    private final double speed;
    private Path path;
    private BlockPos blockPos;
    private boolean finished;
    private boolean started;
    private int progress;
    private BlockPos timeoutMobPos;
    private int timeout;

    public MineTreeGoal(MobEntity mob, Double speed) {
        this.mob = mob;
        this.helper = (HelperEntity) mob;
        this.speed = speed;
        this.finished = false;
        this.started = false;
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
    }

    @Override
    public boolean canStart() {
        if (this.started) return false;
        ServerWorld world = (ServerWorld) this.mob.getWorld();
        return world.getPointOfInterestStorage().getNearestPosition(pointOfInterestTypeRegistryEntry -> true, blockPos -> {
            this.blockPos = blockPos;
            this.path = this.mob.getNavigation().findPathTo(blockPos, 0);
            return (this.path != null && this.path.reachesTarget() && blockPos.isWithinDistance(path.getTarget(), 3d));
        }, this.mob.getBlockPos(), getRadius(this.helper), PointOfInterestStorage.OccupationStatus.ANY).isPresent();
    }

    @Override
    public void start() {
        this.finished = false;
        ServerWorld world = (ServerWorld) this.mob.getWorld();
        world.getPointOfInterestStorage().getNearestPosition(pointOfInterestTypeRegistryEntry -> true, this.mob.getBlockPos(), getRadius(this.helper), PointOfInterestStorage.OccupationStatus.ANY).ifPresent(blockPos -> {
            this.mob.getNavigation().startMovingAlong(this.path, this.speed);
            GlobalPos globalPos = GlobalPos.create(world.getRegistryKey(), blockPos);
            this.mob.getBrain().remember(ModEntities.TREE_POINT, globalPos);
        });
        this.progress = 0;
        this.started = true;
        this.timeout = 0;
    }

    @Override
    public void tick() {
        if (!this.started || this.finished) return;

        ServerWorld world = (ServerWorld) this.mob.getWorld();

        if (world.getBlockState(blockPos).isAir() || !this.path.reachesTarget()) {
            this.finished = true;
        } else {
            if (this.path.isFinished()) {
                Inventory inventory = (Inventory) this.mob;

                //REPRODUCING DIGGING SPEED
                float speedMultiplier = 1;
                if (inventory.getStack(0).isSuitableFor(world.getBlockState(this.blockPos))) {
                    //TOOL
                    speedMultiplier = inventory.getStack(0).getMiningSpeedMultiplier(world.getBlockState(this.blockPos));
                    //EFFICIENCY
                    if (EnchantmentHelper.getLevel(Enchantments.EFFICIENCY, inventory.getStack(0)) > 0)
                        speedMultiplier += EnchantmentHelper.getLevel(Enchantments.EFFICIENCY, inventory.getStack(0)) ^ 2 + 1;
                }
                //HASTE
                if (this.mob.hasStatusEffect(StatusEffects.HASTE))
                    speedMultiplier *= 0.2f * Objects.requireNonNull(this.mob.getStatusEffect(StatusEffects.HASTE)).getAmplifier() + 1;
                //MINING FATIGUE
                if (this.mob.hasStatusEffect(StatusEffects.MINING_FATIGUE)) {
                    float fatigueMultiplier = 0.3f;
                    for (int i = 0; i < Math.min(Objects.requireNonNull(this.mob.getStatusEffect(StatusEffects.MINING_FATIGUE)).getAmplifier(), 4); i++) {
                        fatigueMultiplier *= fatigueMultiplier;
                    }
                    speedMultiplier *= fatigueMultiplier;
                }
                //IN WATER
                if (this.mob.isTouchingWater() && !this.mob.hasStatusEffect(StatusEffects.WATER_BREATHING))
                    speedMultiplier /= 5;
                //NOT ON GROUND
                if (!this.mob.isOnGround()) speedMultiplier /= 5;

                float damage = speedMultiplier / world.getBlockState(this.blockPos).getHardness(world, this.blockPos) / 30;
                int ticks;
                if (damage > 1) ticks = 0;
                else ticks = (int) Math.ceil(1 / damage);

                this.progress++;

                if (this.progress >= ticks) {
                    this.finished = true;
                    for (ItemStack itemStack : world.getBlockState(this.blockPos).getDroppedStacks(new LootContextParameterSet.Builder(world).add(LootContextParameters.ORIGIN, this.blockPos.toCenterPos()).add(LootContextParameters.TOOL, inventory.getStack(0)))) {
                        for (int slotId = 0; slotId < inventory.size(); slotId++) {
                            ItemStack targetStack = inventory.getStack(slotId);
                            if (targetStack.isEmpty() || (targetStack.isOf(itemStack.getItem()) && !(targetStack.getCount() == targetStack.getMaxCount()))) {
                                if (itemStack.getCount() > (targetStack.getMaxCount() - targetStack.getCount())) {
                                    itemStack.split(targetStack.getMaxCount() - targetStack.getCount());
                                    if (targetStack.isEmpty())
                                        inventory.setStack(slotId, itemStack.copyWithCount(targetStack.getMaxCount()));
                                    else targetStack.setCount(targetStack.getMaxCount());
                                } else {
                                    if (targetStack.isEmpty())
                                        inventory.setStack(slotId, itemStack.copyAndEmpty());
                                    else {
                                        targetStack.setCount(targetStack.getCount() + itemStack.getCount());
                                        itemStack.setCount(0);
                                    }
                                    break;
                                }
                            }
                        }
                        if (!itemStack.isEmpty()) {
                            world.spawnEntity(new ItemEntity(world, blockPos.toCenterPos().getX(), blockPos.toCenterPos().getY(), blockPos.toCenterPos().getZ(), itemStack.copyAndEmpty()));
                        }
                    }
                    world.breakBlock(this.blockPos, false, this.mob);
                    world.setBlockBreakingInfo(this.mob.getId(), this.blockPos, 0);
                    world.playSoundAtBlockCenter(this.blockPos, SoundEvents.BLOCK_WOOD_BREAK, SoundCategory.BLOCKS, 1, 1, true);
                    world.addBlockBreakParticles(this.blockPos, world.getBlockState(this.blockPos));
                }
                int animation = Math.round(((float) this.progress / ticks) * 9f);
                world.setBlockBreakingInfo(this.mob.getId(), this.blockPos, animation);
            } else {
                if (this.mob.getBlockPos() == this.timeoutMobPos) {
                    if (this.timeout >= 20) {
                        this.finished = true;
                    } else {
                        this.timeout++;
                    }
                } else {
                    this.timeoutMobPos = this.mob.getBlockPos();
                    this.timeout = 0;
                }
            }
        }
    }
    //TODO: Überprüfung ob angekommen nur mit x,z Abstand 1 oder so machen

    @Override
    public boolean shouldContinue() {
        return !this.finished;
    }

    @Override
    public boolean shouldRunEveryTick() {
        return true;
    }

    @Override
    public void stop() {
        this.started = false;
    }

    @Override
    public boolean canStop() {
        return true;
    }

    private int getRadius(HelperEntity helperEntity) {
        int radius = 0;
        switch (helperEntity.upgrade_searchRadius) {
            case 0 -> radius = Concacity.CONFIG.helper_upgrade_searchRadius_0();
            case 1 -> radius = Concacity.CONFIG.helper_upgrade_searchRadius_1();
            case 2 -> radius = Concacity.CONFIG.helper_upgrade_searchRadius_2();
        }
        return radius;
    }
}