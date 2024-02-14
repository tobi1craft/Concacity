package de.tobi1craft.concacity.client.gui;

import de.tobi1craft.concacity.Concacity;
import de.tobi1craft.concacity.client.gui.forester.HelperForesterInventoryGUI;
import de.tobi1craft.concacity.util.ModScreenHandlers;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class ModGUIs {
    public static void registerModGUIs() {
        Concacity.LOGGER.info("registering mod GUIs");
        HandledScreens.register(ModScreenHandlers.HELPER_FORESTER_INVENTORY_HANDLER, HelperForesterInventoryGUI::new);
    }
}
