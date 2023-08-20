package de.tobi1craft.concacity.client.util;

import de.tobi1craft.concacity.Concacity;
import de.tobi1craft.concacity.client.gui.HelperInventoryGUI;
import de.tobi1craft.concacity.util.ModPackets;
import io.wispforest.owo.network.OwoNetChannel;

public class ClientChannels {
    private final static OwoNetChannel CHANNEL = Concacity.CHANNEL;

    public static void registerChannels() {
        Concacity.LOGGER.info("registering client mod channels");

        CHANNEL.registerClientbound(ModPackets.HelperGUIPacket.class, ((message, access) -> {
            HelperInventoryGUI.upgrade_mode = message.upgrade_mode();
            HelperInventoryGUI.mode = message.mode();
            CHANNEL.clientHandle().send(new ModPackets.GuiPacket(message.uuid(), message.gui()));
        }));
    }
}
