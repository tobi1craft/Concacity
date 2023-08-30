package de.tobi1craft.concacity.util;

import de.tobi1craft.concacity.Concacity;
import de.tobi1craft.concacity.util.enums.EntityVariables;

import java.util.UUID;

public class ModPackets {

    public record GuiPacket(UUID uuid) {}
    public record EntityIntegerPacket(UUID uuid, int integer, EntityVariables variable) {}
    public static void registerPackets() {
        Concacity.LOGGER.info("registered mod packets");
    }
}
