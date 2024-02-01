package de.tobi1craft.concacity.entity.goal;

import de.tobi1craft.concacity.Concacity;
import de.tobi1craft.concacity.entity.ModEntities;
import de.tobi1craft.concacity.entity.helper.HelperMinerEntity;
import de.tobi1craft.concacity.util.ItemTransferHelper;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.ai.pathing.PathNode;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.poi.PointOfInterestStorage;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class MineTreeGoal extends Goal {
    private final MobEntity mob;
    private final HelperMinerEntity helper;
    private final double speed;
    private final HashMap<BlockPos, Integer> progress = new HashMap<>();
    private Path path;
    private BlockPos blockPos;
    private boolean finished;
    private boolean arrived;
    private BlockPos timeoutMobPos;
    private int timeout;
    private boolean mining;

    public MineTreeGoal(MobEntity mob, Double speed) {
        this.mob = mob;
        this.helper = (HelperMinerEntity) mob;
        this.speed = speed;
        this.finished = false;
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
    }

    @Override
    public boolean canStart() {
        ServerWorld world = (ServerWorld) this.mob.getWorld();
        return world.getPointOfInterestStorage().getNearestPosition(pointOfInterestTypeRegistryEntry -> true, blockPos -> {
            for (int offset = 1; !world.getBlockState(blockPos.offset(Direction.Axis.Y, -offset)).isAir(); offset++) {
                if (world.getBlockState(blockPos.offset(Direction.Axis.Y, -offset)).isOf(Blocks.OAK_LOG)) return false;
            }
            this.blockPos = blockPos;
            world.spawnParticles(ParticleTypes.HEART, blockPos.getX(), blockPos.getY(), blockPos.getZ(), 10, 0, 0, 0, 0);
            this.path = this.mob.getNavigation().findPathTo(blockPos, 0);
            return (this.path != null && this.blockPos.getSquaredDistanceFromCenter(this.path.getTarget().getX(), this.blockPos.getY(), this.path.getTarget().getZ()) <= 1);
        }, this.mob.getBlockPos(), getRadius(this.helper), PointOfInterestStorage.OccupationStatus.ANY).isPresent();
    }

    @Override
    public void start() {
        Concacity.LOGGER.error("start");
        this.finished = false;
        this.arrived = false;
        ServerWorld world = (ServerWorld) this.mob.getWorld();
        this.mob.getNavigation().startMovingAlong(this.path, this.speed);
        GlobalPos globalPos = GlobalPos.create(world.getRegistryKey(), this.blockPos);
        this.mob.getBrain().remember(ModEntities.TREE_POINT, globalPos);
        this.progress.clear();
        this.timeout = 0;
        this.mining = false;
    }

    @Override
    public void tick() {
        if (this.finished) return;

        ServerWorld world = (ServerWorld) this.mob.getWorld();

        //cancel because of breaking or pathfinding errors
        if (!world.getBlockState(this.blockPos).isOf(Blocks.OAK_LOG) || (!this.arrived && this.path == null)) {
            this.finished = true;
            //test if mob (Helper) arrived
        } else if (this.arrived || this.path.isFinished() || this.blockPos.getSquaredDistanceFromCenter(this.mob.getX(), this.blockPos.toCenterPos().getY(), this.mob.getZ()) <= 1) {
            this.arrived = true;
            this.path = null;
            this.mob.getNavigation().stop();
            Inventory inventory = (Inventory) this.mob;
            this.mob.getLookControl().lookAt(this.blockPos.toCenterPos().getX(), this.blockPos.toCenterPos().getY(), this.blockPos.toCenterPos().getZ());

            //stack up with scaffolding
            if (stackUp(world, inventory)) return;

            if (mine(world, inventory, this.blockPos)) {
                this.finished = true;
                world.playSoundAtBlockCenter(this.blockPos, SoundEvents.BLOCK_WOOD_BREAK, SoundCategory.BLOCKS, 1, 1, true);
            }

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
        this.mob.getWorld().setBlockBreakingInfo(this.mob.getId(), this.blockPos, 0);
        this.path = null;
        this.mob.getNavigation().stop();
    }

    @Override
    public boolean canStop() {
        return true;
    }

    private int getRadius(HelperMinerEntity helperMinerEntity) {
        int radius = 0;
        switch (helperMinerEntity.upgrade_searchRadius) {
            case 0 -> radius = Concacity.CONFIG.helper_upgrade_searchRadius_0();
            case 1 -> radius = Concacity.CONFIG.helper_upgrade_searchRadius_1();
            case 2 -> radius = Concacity.CONFIG.helper_upgrade_searchRadius_2();
        }
        return radius;
    }

    private boolean stackUp(ServerWorld world, Inventory inventory) {
        //TODO: (Nach einem Blatt abbauen baut er immer Log ab.)
        //TODO: Er geht net mehr aufs Scaffolding
        if (this.blockPos.toCenterPos().getY() - this.mob.getY() >= 4) {
            if (this.mining) {
                if (mine(world, inventory, this.mob.getBlockPos().offset(Direction.Axis.Y, 2))) {
                    world.playSoundAtBlockCenter(this.mob.getBlockPos().offset(Direction.Axis.Y, 2), SoundEvents.BLOCK_GRASS_BREAK, SoundCategory.BLOCKS, 1, 1, true);
                    PathNode pathNode = new PathNode(this.blockPos.getX(), this.blockPos.getY() - 3, this.blockPos.getZ());
                    Path jumpPath = new Path(List.of(pathNode), this.blockPos.offset(Direction.Axis.Y, -3), true);
                    this.mob.getNavigation().startMovingAlong(jumpPath, this.speed);
                    return true;
                }
                return true;
            }
            int offset = 5;
            while (world.getBlockState(this.blockPos.offset(Direction.Axis.Y, -offset)).isAir()) {
                offset++;
            }
            if (world.getBlockState(this.blockPos.offset(Direction.Axis.Y, -offset + 1)).isAir()) {
                //TODO: Scaffolding aus Inventar
                //TODO: Dann auch wieder abbauen
                world.setBlockState(this.blockPos.offset(Direction.Axis.Y, -offset + 1), Blocks.SCAFFOLDING.getDefaultState());
            }
            if (world.getBlockState(this.blockPos.offset(Direction.Axis.Y, -4)).isOf(Blocks.SCAFFOLDING)) {
                if (world.getBlockState(this.mob.getBlockPos().offset(Direction.Axis.Y, 2)).isOf(Blocks.OAK_LEAVES)) {
                    if (mine(world, inventory, this.mob.getBlockPos().offset(Direction.Axis.Y, 2))) {
                        world.playSoundAtBlockCenter(this.mob.getBlockPos().offset(Direction.Axis.Y, 2), SoundEvents.BLOCK_GRASS_BREAK, SoundCategory.BLOCKS, 1, 1, true);
                        PathNode pathNode = new PathNode(this.blockPos.getX(), this.blockPos.getY() - 3, this.blockPos.getZ());
                        Path jumpPath = new Path(List.of(pathNode), this.blockPos.offset(Direction.Axis.Y, -3), true);
                        this.mob.getNavigation().startMovingAlong(jumpPath, this.speed);
                        return true;
                    }
                }

            }
            return true;
        }
        return false;
    }

    private int getMiningTime(ServerWorld world, ItemStack tool, BlockPos blockPos) {
        float speedMultiplier = 1;
        if (tool.isSuitableFor(world.getBlockState(blockPos))) {
            //TOOL
            speedMultiplier = tool.getMiningSpeedMultiplier(world.getBlockState(blockPos));
            //EFFICIENCY
            if (EnchantmentHelper.getLevel(Enchantments.EFFICIENCY, tool) > 0)
                speedMultiplier += EnchantmentHelper.getLevel(Enchantments.EFFICIENCY, tool) ^ 2 + 1;
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

        float damage = speedMultiplier / world.getBlockState(blockPos).getHardness(world, blockPos) / 30;
        if (damage > 1) return 0;
        else return (int) Math.ceil(1 / damage);
    }

    private boolean mine(ServerWorld world, Inventory inventory, BlockPos blockPos) {
        this.mining = true;
        //time (ticks) to mine block
        int ticks = getMiningTime(world, inventory.getStack(0), blockPos);
        int currentProgress = this.progress.get(blockPos) == null ? 0 : this.progress.get(blockPos) + 1;
        this.progress.put(blockPos, currentProgress);
        int progress = this.progress.get(blockPos);

        //mining finished
        if (progress >= ticks) {
            this.mining = false;
            for (ItemStack itemStack : world.getBlockState(this.blockPos).getDroppedStacks(new LootContextParameterSet.Builder(world).add(LootContextParameters.ORIGIN, this.blockPos.toCenterPos()).add(LootContextParameters.TOOL, inventory.getStack(0)))) {
                ItemTransferHelper.insertStackIntoInventory(itemStack, inventory);
                if (!itemStack.isEmpty()) {
                    world.spawnEntity(new ItemEntity(world, this.blockPos.toCenterPos().getX(), this.blockPos.toCenterPos().getY(), this.blockPos.toCenterPos().getZ(), itemStack.copyAndEmpty()));
                }
            }
            world.breakBlock(blockPos, false, this.mob);
            world.setBlockBreakingInfo(this.mob.getId(), blockPos, 0);
            this.progress.put(blockPos, 0);
            world.addBlockBreakParticles(blockPos, world.getBlockState(blockPos));
            return true;
        } else {
            int animation = Math.round(((float) progress / ticks) * 9f);
            world.setBlockBreakingInfo(this.mob.getId(), blockPos, animation);
        }
        return false;
    }
}