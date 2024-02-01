package de.tobi1craft.concacity.client.gui;

import de.tobi1craft.concacity.Concacity;
import de.tobi1craft.concacity.client.gui.miner.HelperMinerInventoryGUI;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class ModGUIs {
    public static void registerModGUIs() {
        Concacity.LOGGER.info("registering mod GUIs");
        HandledScreens.register(Concacity.SCREEN_HANDLER_TYPE, HelperMinerInventoryGUI::new);
    }
}
