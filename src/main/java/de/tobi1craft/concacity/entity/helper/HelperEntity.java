package de.tobi1craft.concacity.entity.helper;

import de.tobi1craft.concacity.entity.goal.SitOnGroundGoal;
import de.tobi1craft.concacity.inventory.HelperInventory;
import de.tobi1craft.concacity.util.enums.HelperInventoryTabs;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.constant.DefaultAnimations;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public class HelperEntity extends PathAwareEntity implements GeoEntity, HelperInventory, ExtendedScreenHandlerFactory {

    private static int inventorySize;
    protected final DefaultedList<ItemStack> inventory;
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    public boolean sitting;
    public boolean breaking;
    public HelperInventoryTabs tabToOpen = HelperInventoryTabs.INVENTORY;

    public HelperEntity(EntityType<? extends PathAwareEntity> entityType, World world, int inventorySize) {
        super(entityType, world);
        HelperEntity.inventorySize = inventorySize;
        inventory = DefaultedList.ofSize(inventorySize, ItemStack.EMPTY);
    }

    public static int getInventorySize() {
        return inventorySize;
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return AnimalEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 16.0D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 0.0f)
                .add(EntityAttributes.GENERIC_ATTACK_SPEED, 2.0f)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.4f);
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return null;
    }

    @Override
    public boolean shouldCloseCurrentScreen() {
        return false;
    }

    @Override
    public Text getDisplayName() {
        return Text.literal("Helper");
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {

    }

    public boolean isValid(EquipmentSlot equipmentSlot, ItemStack stack) {
        boolean isValid = true;
        switch (equipmentSlot) {
            case HEAD ->
                    isValid = stack.getItem() instanceof ArmorItem && stack.getItem().getTranslationKey().contains("helmet");
            case CHEST ->
                    isValid = stack.getItem() instanceof ArmorItem && stack.getItem().getTranslationKey().contains("chestplate");
            case LEGS ->
                    isValid = stack.getItem() instanceof ArmorItem && stack.getItem().getTranslationKey().contains("leggins");
            case FEET ->
                    isValid = stack.getItem() instanceof ArmorItem && stack.getItem().getTranslationKey().contains("boots");
        }
        return isValid;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(10, new SwimGoal(this));

        this.goalSelector.add(11, new SitOnGroundGoal(this));
    }

    public boolean hasActiveGoals() {
        return this.goalSelector.getRunningGoals().anyMatch(goal -> !(goal.getPriority() == 11));
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(DefaultAnimations.genericWalkController(this));
        controllerRegistrar.add(DefaultAnimations.genericAttackAnimation(this, DefaultAnimations.ATTACK_SWING));
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, this::handleAnimations));
    }

    private <T extends GeoAnimatable> PlayState handleAnimations(AnimationState<T> state) {
        if (breaking) {
            if (this.getEquippedStack(EquipmentSlot.MAINHAND).isIn(ItemTags.PICKAXES))
                state.setAnimation(RawAnimation.begin().thenPlay("break.pickaxe"));
            else if (this.getEquippedStack(EquipmentSlot.MAINHAND).isIn(ItemTags.AXES))
                state.setAnimation(RawAnimation.begin().thenPlay("break.axe"));
            else state.setAnimation(RawAnimation.begin().thenPlay("break.punch"));
            return PlayState.CONTINUE;
        } else if (jumping) {
            state.setAnimation(DefaultAnimations.JUMP);
            return PlayState.CONTINUE;
        } else if (sitting) {
            state.setAnimation(DefaultAnimations.SIT);
            return PlayState.CONTINUE;
        }

        return PlayState.STOP;
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, this.inventory);
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        Inventories.writeNbt(nbt, inventory);
        return super.writeNbt(nbt);
    }
}
