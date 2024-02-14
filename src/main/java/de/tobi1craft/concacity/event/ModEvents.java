package de.tobi1craft.concacity.event;

import de.tobi1craft.concacity.Concacity;
import de.tobi1craft.concacity.util.ConcacityConfig;
import net.fabricmc.fabric.api.entity.event.v1.EntityElytraEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

public class ModEvents {
    static final ConcacityConfig CONFIG = Concacity.CONFIG;

    public static void registerModEvents() {
        ServerPlayConnectionEvents.JOIN.register(new PlayerJoin());
        if (CONFIG.elytra()) EntityElytraEvents.CUSTOM.register(new ElytraDurability());
    }
}
