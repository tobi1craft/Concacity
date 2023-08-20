package de.tobi1craft.concacity.util;

import de.tobi1craft.concacity.Concacity;
import de.tobi1craft.concacity.util.enums.EntityVariables;
import de.tobi1craft.concacity.util.enums.GUIs;

import java.util.UUID;

public class ModPackets {

    public record GuiPacket(UUID uuid, GUIs gui) {}
    public record EntityIntegerPacket(UUID uuid, int integer, EntityVariables variable) {}
    public record HelperGUIPacket(UUID uuid, GUIs gui, int upgrade_mode, int mode) {}
    public record RequestHelperGUIPacket(UUID uuid, GUIs gui) {}
    public static void registerPackets() {
        Concacity.LOGGER.info("registered mod packets");
    }
}
