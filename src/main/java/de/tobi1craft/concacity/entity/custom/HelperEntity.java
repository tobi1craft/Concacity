package de.tobi1craft.concacity.entity.custom;

import de.tobi1craft.concacity.entity.goal.MineTreeGoal;
import de.tobi1craft.concacity.inventory.HelperInventory;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

public class HelperEntity extends PathAwareEntity implements GeoEntity, HelperInventory {

    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    private final DefaultedList<ItemStack> items = DefaultedList.ofSize(3, ItemStack.EMPTY);
    public int mode;
    public int upgrade_mode;

    public HelperEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
    }


    public static DefaultAttributeContainer.Builder setAttributes() {
        return AnimalEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 16.0D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 0.0f)
                .add(EntityAttributes.GENERIC_ATTACK_SPEED, 2.0f)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.4f);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(2, new MeleeAttackGoal(this, 1.2D, false));
        this.goalSelector.add(3, new WanderAroundFarGoal(this, 0.75f, 1));

        this.goalSelector.add(4, new LookAroundGoal(this));
        this.goalSelector.add(0, new MineTreeGoal(this));

        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, MerchantEntity.class, true));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, ChickenEntity.class, true));
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> tAnimationState) {
        if (tAnimationState.isMoving())
            tAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.helper.walk", Animation.LoopType.LOOP));

        //if (this.isAttacking()) tAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.helper.attack", Animation.LoopType.PLAY_ONCE));

        tAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.helper.idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return items;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, items);
        mode = nbt.getInt("mode");
        upgrade_mode = nbt.getInt("upgrade_mode");
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        Inventories.writeNbt(nbt, items);
        nbt.putInt("mode", mode);
        nbt.putInt("upgrade_mode", upgrade_mode);
        return super.writeNbt(nbt);
    }

}
