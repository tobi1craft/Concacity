package de.tobi1craft.concacity.client.gui;

import de.tobi1craft.concacity.Concacity;
import io.wispforest.owo.client.screens.ScreenUtils;
import io.wispforest.owo.client.screens.SlotGenerator;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.Text;

public class ModGUIs extends ScreenHandler {
    public static HelperInventoryGUI helperInventoryGUI;
    private final ScreenHandlerContext context;

    public ModGUIs(int syncId, PlayerInventory inventory) {
        this(syncId, inventory, ScreenHandlerContext.EMPTY);
    }

    public ModGUIs(int syncId, PlayerInventory inventory, ScreenHandlerContext context) {
        super(Concacity.SCREEN_HANDLER_TYPE, syncId);
        this.context = context;
        SlotGenerator.begin(this::addSlot, 8, 84)
                .grid(new SimpleInventory(4), 0, 4, 1)
                .playerInventory(inventory);

        helperInventoryGUI = new HelperInventoryGUI(this, inventory, Text.literal("Test"));
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
