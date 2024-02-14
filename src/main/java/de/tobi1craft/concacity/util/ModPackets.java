package de.tobi1craft.concacity.util;

import de.tobi1craft.concacity.Concacity;
import de.tobi1craft.concacity.util.enums.AnimationTypes;
import de.tobi1craft.concacity.util.enums.EntityVariables;
import de.tobi1craft.concacity.util.enums.HelperInventoryTabs;

import java.util.UUID;

public class ModPackets {
    public record HelperGuiPacket(UUID entityUUID, HelperInventoryTabs inventoryTab) {}
    public record EntityIntegerPacket(UUID uuid, int integer, EntityVariables variable) {}
    public record HelperAnimationPacket(UUID uuid, AnimationTypes animationType, boolean active) {}
    public static void registerPackets() {
        Concacity.LOGGER.info("registered mod packets");
    }
}
