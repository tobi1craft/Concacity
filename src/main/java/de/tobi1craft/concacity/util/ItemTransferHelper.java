package de.tobi1craft.concacity.util;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

public class ItemTransferHelper {
    public static void insertStackIntoInventory(ItemStack itemStack, Inventory inventory) {
        for (int slotId = 0; slotId < inventory.size(); slotId++) {
            ItemStack targetStack = inventory.getStack(slotId);
            if (targetStack.isEmpty() || (targetStack.isOf(itemStack.getItem()) && !(targetStack.getCount() == targetStack.getMaxCount()))) {
                if (itemStack.getCount() > (targetStack.getMaxCount() - targetStack.getCount())) {
                    itemStack.split(targetStack.getMaxCount() - targetStack.getCount());
                    if (targetStack.isEmpty())
                        inventory.setStack(slotId, itemStack.copyWithCount(targetStack.getMaxCount()));
                    else targetStack.setCount(targetStack.getMaxCount());
                } else {
                    if (targetStack.isEmpty()) inventory.setStack(slotId, itemStack.copyAndEmpty());
                    else {
                        targetStack.setCount(targetStack.getCount() + itemStack.getCount());
                        itemStack.setCount(0);
                    }
                    break;
                }
            }
        }
    }
}
