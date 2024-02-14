package de.tobi1craft.concacity.entity.goal;

import de.tobi1craft.concacity.Concacity;
import de.tobi1craft.concacity.entity.ModEntities;
import de.tobi1craft.concacity.entity.helper.HelperForesterEntity;
import de.tobi1craft.concacity.util.ItemTransferHelper;
import de.tobi1craft.concacity.util.ModPackets;
import de.tobi1craft.concacity.util.enums.AnimationTypes;
import net.minecraft.block.BlockState;
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
import net.minecraft.item.Items;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.poi.PointOfInterestStorage;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class MineTreeGoal extends Goal {
    private final MobEntity mob;
    private final HelperForesterEntity helper;
    private final double speed;
    private final HashMap<BlockPos, Integer> progress = new HashMap<>();
    private final List<BlockPos> scaffolding = new ArrayList<>();
    private Path path;
    private BlockPos blockPos;
    private BlockPos groundPos;
    private boolean finished;
    private boolean arrived;
    private BlockPos timeoutMobPos;
    private int timeout;
    private boolean goingDown;

    public MineTreeGoal(MobEntity mob, Double speed) {
        this.mob = mob;
        this.helper = (HelperForesterEntity) mob;
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
            //Untergrund (Block unter Baum) finden
            for (int offset = 1; blockPos.offset(Direction.Axis.Y, -offset).getY() >= world.getBottomY(); offset++) {
                BlockPos offsetPos = blockPos.offset(Direction.Axis.Y, -offset);
                BlockState offsetState = world.getBlockState(offsetPos);
                if (!offsetState.isOf(Blocks.OAK_LOG) && !offsetState.isAir()) {
                    groundPos = offsetPos;
                    break;
                }
            }
            if (groundPos == null) return false; //Falls kein Untergrund gefunden wurde
            world.spawnParticles(ParticleTypes.HEART, blockPos.getX(), blockPos.getY(), blockPos.getZ(), 10, 0, 0, 0, 0);

            //Build path
            this.path = mob.getNavigation().findPathTo(blockPos, 0);
            if (path == null) return false;
            List<PathNode> pathNodes = new ArrayList<>();
            for (int i = 0; i < path.getLength(); i++) {
                pathNodes.add(path.getNode(i));
            }
            pathNodes.add(new PathNode(blockPos.getX(), groundPos.offset(Direction.Axis.Y, 1).getY(), blockPos.getZ()));
            this.path = new Path(pathNodes, blockPos, true);

            return this.blockPos.getSquaredDistanceFromCenter(this.path.getTarget().getX(), this.blockPos.getY(), this.path.getTarget().getZ()) <= 1;
        }, this.mob.getBlockPos(), getSearchRadius(this.helper), PointOfInterestStorage.OccupationStatus.ANY).isPresent();
    }

    @Override
    public void start() {
        this.finished = false;
        this.arrived = false;
        this.goingDown = false;
        this.progress.clear();
        this.timeout = 0;
        ServerWorld world = (ServerWorld) this.mob.getWorld();
        this.mob.getNavigation().startMovingAlong(this.path, this.speed);
        GlobalPos globalPos = GlobalPos.create(world.getRegistryKey(), this.blockPos);
        this.mob.getBrain().remember(ModEntities.TREE_POINT, globalPos);
    }

    @Override
    public void tick() {
        if (this.finished) return;

        ServerWorld world = (ServerWorld) this.mob.getWorld();
        Inventory inventory = (Inventory) this.mob;

        if (goingDown) {
            breakDown(world, inventory);
            return;
        }

        //cancel because of breaking or pathfinding errors
        if (!world.getBlockState(this.blockPos).isIn(BlockTags.LOGS) || (!this.arrived && this.path == null)) {
            this.finished = true;
            return;
        }
        //test if mob (Helper) arrived
        if (this.arrived || this.path.isFinished() || ((!world.getBlockState(groundPos.offset(Direction.Axis.Y, 1)).isAir() || !world.getBlockState(groundPos.offset(Direction.Axis.Y, 2)).isAir()) && groundPos.offset(Direction.Axis.Y, 1).toCenterPos().distanceTo(mob.getPos()) <= 1) || groundPos.offset(Direction.Axis.Y, 1).toCenterPos().distanceTo(mob.getPos()) <= 0) {
            this.arrived = true;
            this.path = null;
            this.mob.getNavigation().stop();
            this.mob.getLookControl().lookAt(this.blockPos.toCenterPos().getX(), this.blockPos.toCenterPos().getY(), this.blockPos.toCenterPos().getZ());

            //stack up with scaffolding
            if (stackUp(world, inventory)) return;

            if (mine(world, inventory, this.blockPos)) {
                this.goingDown = true;
                world.playSoundAtBlockCenter(this.blockPos, SoundEvents.BLOCK_WOOD_BREAK, SoundCategory.BLOCKS, 1, 1, true);
            }
            return;
        }

        BlockPos lookingAt = getLookingAt(false);
        BlockPos eyesLookingAt = getLookingAt(true);
        if (world.getBlockState(lookingAt).isIn(BlockTags.LEAVES)) {
            if (mine(world, inventory, lookingAt))
                world.playSoundAtBlockCenter(lookingAt, SoundEvents.BLOCK_GRASS_BREAK, SoundCategory.BLOCKS, 1, 1, true);
            return;
        } else if (world.getBlockState(eyesLookingAt).isIn(BlockTags.LEAVES)) {
            if (mine(world, inventory, eyesLookingAt))
                world.playSoundAtBlockCenter(eyesLookingAt, SoundEvents.BLOCK_GRASS_BREAK, SoundCategory.BLOCKS, 1, 1, true);
            return;
        }


        if (this.mob.getBlockPos() == this.timeoutMobPos) {
            if (this.timeout >= 60) {
                this.scaffolding.clear();
                this.finished = true;
            } else {
                this.timeout++;
            }
        } else {
            this.timeoutMobPos = this.mob.getBlockPos();
            this.timeout = 0;
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
        this.mob.getWorld().setBlockBreakingInfo(this.mob.getId(), this.blockPos, 10);
        this.path = null;
        this.mob.getNavigation().stop();
        for (BlockPos pos : progress.keySet()) {
            mob.getWorld().setBlockBreakingInfo(mob.getId(), pos, 10);
        }
    }

    @Override
    public boolean canStop() {
        return true;
    }

    private @NotNull BlockPos getLookingAt(boolean useEyes) {
        Vec3d entityPos;
        if (useEyes) entityPos = mob.getEyePos();
        else entityPos = mob.getPos();
        Vec3d entityLook = mob.getRotationVector();

        // Strahl (Ray) erstellen
        double reachDistance = 1.0; // Maximale Reichweite des Blicks einstellen
        Vec3d rayTarget = entityPos.add(entityLook.multiply(reachDistance));
        RaycastContext raycastContext = new RaycastContext(entityPos, rayTarget, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, mob);

        // Raycasting durchführen
        BlockHitResult blockHitResult = mob.getWorld().raycast(raycastContext);

        // Blockposition zurückgeben, die die Entität anschaut
        BlockPos blockPos = blockHitResult.getBlockPos();
        ((ServerWorld) this.mob.getWorld()).spawnParticles(ParticleTypes.ANGRY_VILLAGER, blockPos.toCenterPos().getX(), blockPos.toCenterPos().getY(), blockPos.toCenterPos().getZ(), 1, 0, 0, 0, 0);
        return blockPos;
    }

    private int getSearchRadius(@NotNull HelperForesterEntity helperForesterEntity) {
        int radius = 0;
        switch (helperForesterEntity.upgrade_searchRadius) {
            case 0 -> radius = Concacity.CONFIG.helper_upgrade_searchRadius_0();
            case 1 -> radius = Concacity.CONFIG.helper_upgrade_searchRadius_1();
            case 2 -> radius = Concacity.CONFIG.helper_upgrade_searchRadius_2();
        }
        return radius;
    }

    private boolean stackUp(ServerWorld world, Inventory inventory) {
        if (this.blockPos.toCenterPos().getY() - this.mob.getEyeY() <= 5) {
            return false;
        }

        BlockPos offsetPos = groundPos.offset(Direction.Axis.Y, 1);
        while (world.getBlockState(offsetPos).isOf(Blocks.SCAFFOLDING)) {
            offsetPos = offsetPos.offset(Direction.Axis.Y, 1);
        }

        if (world.getBlockState(new BlockPos(blockPos.getX(), mob.getBlockY(), blockPos.getZ())).isOf(Blocks.SCAFFOLDING)) {
            if (mob.getNavigation().isIdle())
                mob.getNavigation().startMovingAlong(new Path(List.of(new PathNode(offsetPos.getX(), offsetPos.getY() + 1, offsetPos.getZ())), blockPos, true), speed);
            return true;
        }

        for (int i = 0; i < 3; i++)
            if (!world.getBlockState(offsetPos.offset(Direction.Axis.Y, i)).isAir()) return true;

        for (int slot = 0; slot < inventory.size(); slot++) {
            if (inventory.getStack(slot).isOf(Items.SCAFFOLDING)) {
                inventory.removeStack(slot, 1);
                world.setBlockState(offsetPos, Blocks.SCAFFOLDING.getDefaultState());
                world.playSoundAtBlockCenter(offsetPos, SoundEvents.BLOCK_SCAFFOLDING_PLACE, SoundCategory.BLOCKS, 1, 1, true);
                scaffolding.add(offsetPos);
                mob.getNavigation().startMovingAlong(new Path(List.of(new PathNode(offsetPos.getX(), offsetPos.getY() + 1, offsetPos.getZ())), blockPos, true), speed);
                break;
            }
        }
        return true;
    }

    private void breakDown(ServerWorld world, Inventory inventory) {
        BlockPos oldPos = blockPos;
        if (scaffolding.isEmpty()) {
            this.finished = true;
            return;
        }
        if (this.canStart() && blockPos.getX() == oldPos.getX() && blockPos.getY() > oldPos.getY() && blockPos.getZ() == oldPos.getZ()) {
            this.finished = true;
            return;
        }

        if (!(mob.getY() == (int) mob.getY())) return;

        if (mine(world, inventory, scaffolding.get(scaffolding.size() - 1))) scaffolding.remove(scaffolding.size() - 1);
    }

    private int getMiningTime(@NotNull ServerWorld world, @NotNull ItemStack tool, BlockPos blockPos) {
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

    private boolean mine(ServerWorld world, @NotNull Inventory inventory, BlockPos blockPos) {
        Concacity.CHANNEL.serverHandle(mob.getServer()).send(new ModPackets.HelperAnimationPacket(mob.getUuid(), AnimationTypes.BREAK, true));
        //time (ticks) to mine block
        int ticks = getMiningTime(world, inventory.getStack(0), blockPos);
        int currentProgress = this.progress.get(blockPos) == null ? 0 : this.progress.get(blockPos) + 1;
        this.progress.put(blockPos, currentProgress);
        int progress = this.progress.get(blockPos);

        //mining finished
        if (progress >= ticks) {
            for (ItemStack itemStack : world.getBlockState(blockPos).getDroppedStacks(new LootContextParameterSet.Builder(world).add(LootContextParameters.ORIGIN, blockPos.toCenterPos()).add(LootContextParameters.TOOL, inventory.getStack(0)))) {
                ItemTransferHelper.insertStackIntoInventory(itemStack, inventory);
                if (!itemStack.isEmpty()) {
                    world.spawnEntity(new ItemEntity(world, blockPos.toCenterPos().getX(), blockPos.toCenterPos().getY(), blockPos.toCenterPos().getZ(), itemStack.copyAndEmpty()));
                }
            }
            world.breakBlock(blockPos, false, this.mob);
            world.setBlockBreakingInfo(this.mob.getId(), blockPos, 10);
            this.progress.remove(blockPos);
            world.addBlockBreakParticles(blockPos, world.getBlockState(blockPos));
            Concacity.CHANNEL.serverHandle(mob.getServer()).send(new ModPackets.HelperAnimationPacket(mob.getUuid(), AnimationTypes.BREAK, false));
            return true; //finished
        } else {
            int animation = Math.round(((float) progress / ticks) * 9f);
            world.setBlockBreakingInfo(this.mob.getId(), blockPos, animation);
        }
        return false; //unfinished
    }
}