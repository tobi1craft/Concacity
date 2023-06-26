package de.tobi1craft.concacity;

import de.tobi1craft.concacity.block.ModBlocks;
import de.tobi1craft.concacity.client.ModItemGroup;
import de.tobi1craft.concacity.event.ElytraDurability;
import de.tobi1craft.concacity.item.ModItems;
import de.tobi1craft.concacity.util.ConcacityConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.EntityElytraEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Concacity implements ModInitializer {
    public static final String ID = "concacity";
    public static final Logger LOGGER = LoggerFactory.getLogger("concacity");

    public static final ConcacityConfig CONFIG = ConcacityConfig.createAndLoad();

    @Override
    public void onInitialize() {
        LOGGER.info("STARTING");
        ModItemGroup.registerItemGroup();
        ModItems.registerModItems();
        ModBlocks.registerModBlocks();

        EntityElytraEvents.CUSTOM.register(new ElytraDurability());
    }
}
