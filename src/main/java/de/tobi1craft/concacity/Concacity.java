package de.tobi1craft.concacity;

import de.tobi1craft.concacity.block.ModBlocks;
import de.tobi1craft.concacity.client.ModItemGroup;
import de.tobi1craft.concacity.discord.Discord;
import de.tobi1craft.concacity.event.ModEvents;
import de.tobi1craft.concacity.item.ModItems;
import de.tobi1craft.concacity.util.ConcacityConfig;
import net.fabricmc.api.ModInitializer;
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
        ModEvents.registerModEvents();
        if(CONFIG.discord_enabled()) Discord.registerDiscord();
    }
}
