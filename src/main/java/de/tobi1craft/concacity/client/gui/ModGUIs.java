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
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;

public class ModGUIs extends ScreenHandler {
    public HelperEntity targetedEntity;
    public Inventory inv;

    public ModGUIs(int syncId, PlayerInventory inventory) {
        this(syncId, inventory, ScreenHandlerContext.EMPTY, MinecraftClient.getInstance().targetedEntity);
    }

    public ModGUIs(int syncId, PlayerInventory inventory, ScreenHandlerContext context, Entity targetedEntity) {
        super(Concacity.SCREEN_HANDLER_TYPE, syncId);
        inv = (Inventory) targetedEntity;
        this.targetedEntity = (HelperEntity) targetedEntity;

        SlotGenerator.begin(this::addSlot, 8, 84)
                //.grid(new SimpleInventory(4), 0, 4, 1)
                .grid(inv, 0, 2, 1)
                .playerInventory(inventory);

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
