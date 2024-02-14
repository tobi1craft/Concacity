package de.tobi1craft.concacity.client.gui.forester;

import de.tobi1craft.concacity.entity.helper.HelperEntity;
import de.tobi1craft.concacity.entity.helper.HelperForesterEntity;
import de.tobi1craft.concacity.inventory.EntityEquipmentSlot;
import de.tobi1craft.concacity.util.ModScreenHandlers;
import io.wispforest.owo.client.screens.ScreenUtils;
import io.wispforest.owo.client.screens.SlotGenerator;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;

public class HelperForesterInventoryHandler extends ScreenHandler {
    public final HelperEntity helperEntity;
    private final Inventory inventory;
    public int upgrade_mode;
    public int mode;


    public HelperForesterInventoryHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, playerInventory, new SimpleInventory(HelperForesterEntity.getInventorySize()), ScreenHandlerContext.EMPTY, (HelperForesterEntity) MinecraftClient.getInstance().targetedEntity);
        upgrade_mode = buf.readInt();
        mode = buf.readInt();
    }

    @SuppressWarnings("unused") //ScreenHandlerContext maybe useful at some point
    public HelperForesterInventoryHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, ScreenHandlerContext context, HelperEntity helperEntity) {
        super(ModScreenHandlers.HELPER_FORESTER_INVENTORY_HANDLER, syncId);
        this.inventory = inventory;
        this.helperEntity = helperEntity;

        addSlot(new EntityEquipmentSlot(helperEntity, EquipmentSlot.MAINHAND));
        addSlot(new EntityEquipmentSlot(helperEntity, EquipmentSlot.HEAD));
        addSlot(new EntityEquipmentSlot(helperEntity, EquipmentSlot.CHEST));
        addSlot(new EntityEquipmentSlot(helperEntity, EquipmentSlot.LEGS));
        addSlot(new EntityEquipmentSlot(helperEntity, EquipmentSlot.FEET));

        SlotGenerator.begin(this::addSlot, 10, 50)
                .grid(this.inventory, 0, this.inventory.size(), 1)
                .moveTo(0, 150)
                .playerInventory(playerInventory);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return ScreenUtils.handleSlotTransfer(this, slot, this.inventory.size());
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }
}
