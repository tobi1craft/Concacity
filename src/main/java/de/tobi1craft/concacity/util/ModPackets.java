package de.tobi1craft.concacity.util;

import de.tobi1craft.concacity.Concacity;

public class ModPackets {

    public record OnlyPacket() {}
    public static void registerPackets() {
        Concacity.LOGGER.info("registered mod packets");
    }
}
