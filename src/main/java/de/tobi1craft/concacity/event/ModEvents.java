package de.tobi1craft.concacity.event;

import de.tobi1craft.concacity.Concacity;
import de.tobi1craft.concacity.util.ConcacityConfig;
import net.fabricmc.fabric.api.entity.event.v1.EntityElytraEvents;

public class ModEvents {
    static ConcacityConfig CONFIG = Concacity.CONFIG;

    public static void registerModEvents() {
        if (!CONFIG.elytra_durability_loss()) EntityElytraEvents.CUSTOM.register(new ElytraDurability());
    }
}
