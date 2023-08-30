package de.tobi1craft.concacity.client.gui;

import de.tobi1craft.concacity.Concacity;
import de.tobi1craft.concacity.entity.custom.HelperEntity;
import io.wispforest.owo.client.screens.ScreenUtils;
import io.wispforest.owo.client.screens.SlotGenerator;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;

public class ModGUIs extends ScreenHandler {
    public final HelperEntity targetedEntity;
    public final Inventory inv;
    private final Inventory inventory;
    public int upgrade_mode;
    public int mode;

    public ModGUIs(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, playerInventory, new SimpleInventory(4), ScreenHandlerContext.EMPTY, MinecraftClient.getInstance().targetedEntity);
        upgrade_mode = buf.readInt();
        mode = buf.readInt();
    }

    @SuppressWarnings("unused")
    public ModGUIs(int syncId, PlayerInventory playerInventory, Inventory inventory, ScreenHandlerContext context, Entity targetedEntity) {
        super(Concacity.SCREEN_HANDLER_TYPE, syncId);
        this.inventory = inventory;
        this.inv = (Inventory) targetedEntity;
        this.targetedEntity = (HelperEntity) targetedEntity;

        SlotGenerator.begin(this::addSlot, 8, 84)
                .grid(inv, 0, 2, 1)
                .playerInventory(playerInventory);
    }

    public static void registerModGUIs() {
        Concacity.LOGGER.info("registering mod GUIs");
        HandledScreens.register(Concacity.SCREEN_HANDLER_TYPE, HelperInventoryGUI::new);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return ScreenUtils.handleSlotTransfer(this, slot, 4);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }
}
