package de.tobi1craft.concacity.client.gui.miner;

import de.tobi1craft.concacity.Concacity;
import de.tobi1craft.concacity.entity.helper.HelperMinerEntity;
import io.wispforest.owo.client.screens.ScreenUtils;
import io.wispforest.owo.client.screens.SlotGenerator;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;

public class HelperMinerInventoryHandler extends ScreenHandler {
    private final Inventory inventory;
    public final HelperMinerEntity helperEntity;
    public boolean cave;
    public int upgrade_mode;
    public int mode;


    public HelperMinerInventoryHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, playerInventory, new SimpleInventory(4), ScreenHandlerContext.EMPTY, (HelperMinerEntity) MinecraftClient.getInstance().targetedEntity);
        cave = buf.readBoolean();
        upgrade_mode = buf.readInt();
        mode = buf.readInt();
    }

    @SuppressWarnings("unused")
    public HelperMinerInventoryHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, ScreenHandlerContext context, HelperMinerEntity helperEntity) {
        super(Concacity.SCREEN_HANDLER_TYPE, syncId);
        this.inventory = inventory;
        this.helperEntity = helperEntity;

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
