package de.tobi1craft.concacity.client.util;

import de.tobi1craft.concacity.Concacity;
import io.wispforest.owo.network.OwoNetChannel;

public class ClientChannels {
    private final static OwoNetChannel CHANNEL = Concacity.CHANNEL;

    public static void registerChannels() {
        Concacity.LOGGER.info("registering client mod channels");

    }
}
