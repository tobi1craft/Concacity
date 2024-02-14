package de.tobi1craft.concacity.inventory;

import de.tobi1craft.concacity.entity.helper.HelperEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class EntityEquipmentSlot extends Slot {
    private final HelperEntity entity;
    private final EquipmentSlot equipmentSlot;

    public EntityEquipmentSlot(HelperEntity entity, EquipmentSlot equipmentSlot) {
        super(new SimpleInventory(1), 0, 0, 0);
        this.entity = entity;
        this.equipmentSlot = equipmentSlot;
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return entity.getEquippedStack(equipmentSlot).isEmpty() && entity.isValid(equipmentSlot, stack);
    }

    @Override
    public boolean canTakeItems(net.minecraft.entity.player.PlayerEntity playerEntity) {
        return !getStack().isEmpty();
    }

    @Override
    public ItemStack getStack() {
        return entity.getEquippedStack(equipmentSlot);
    }

    @Override
    public void setStack(ItemStack stack) {
        entity.equipStack(equipmentSlot, stack);
        super.setStack(stack);
    }

    @Override
    public ItemStack takeStack(int amount) {
        ItemStack copy = entity.getEquippedStack(equipmentSlot).copy();
        entity.getEquippedStack(equipmentSlot).decrement(amount);
        return copy.copyWithCount(amount);
    }

    @Override
    public ItemStack insertStack(ItemStack stack) {
        return super.insertStack(stack);
    }

    @Override
    public int getMaxItemCount(ItemStack stack) {
        return stack.getMaxCount();
    }
}

