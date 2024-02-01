package de.tobi1craft.concacity;

import de.tobi1craft.concacity.block.ModBlocks;
import de.tobi1craft.concacity.client.ModItemGroup;
import de.tobi1craft.concacity.entity.ModEntities;
import de.tobi1craft.concacity.entity.goal.ModGoals;
import de.tobi1craft.concacity.event.ModEvents;
import de.tobi1craft.concacity.client.gui.miner.HelperMinerInventoryHandler;
import de.tobi1craft.concacity.item.ModItems;
import de.tobi1craft.concacity.util.ConcacityConfig;
import de.tobi1craft.concacity.util.ModChannels;
import de.tobi1craft.concacity.util.ModPackets;
import io.wispforest.owo.network.OwoNetChannel;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.*;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Concacity implements ModInitializer {
    public static final String ID = "concacity";
    public static final Logger LOGGER = LoggerFactory.getLogger("concacity");
    public static final ConcacityConfig CONFIG = ConcacityConfig.createAndLoad();
    public static ScreenHandlerType<HelperMinerInventoryHandler> SCREEN_HANDLER_TYPE = new ExtendedScreenHandlerType<>(HelperMinerInventoryHandler::new);
    static {
        SCREEN_HANDLER_TYPE = Registry.register(Registries.SCREEN_HANDLER, new Identifier(ID, "screenhandler"), SCREEN_HANDLER_TYPE);
    }
    public static final OwoNetChannel CHANNEL = OwoNetChannel.create(new Identifier(ID, "main"));

    @Override
    public void onInitialize() {
        LOGGER.info("STARTING");
        ModPackets.registerPackets();
        ModChannels.registerChannels();
        ModItemGroup.registerItemGroup();
        ModItems.registerModItems();
        ModBlocks.registerModBlocks();
        ModEvents.registerModEvents();
        ModGoals.registerGoals();
        ModEntities.registerModEntities();
    }
}
