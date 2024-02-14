package de.tobi1craft.concacity.util;

import de.tobi1craft.concacity.Concacity;
import de.tobi1craft.concacity.client.gui.forester.HelperForesterInventoryHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class ModScreenHandlers {
    public static ScreenHandlerType<HelperForesterInventoryHandler> HELPER_FORESTER_INVENTORY_HANDLER;

    public static void registerScreenHandlers() {
        Concacity.LOGGER.info("registering mod screen handlers");
        HELPER_FORESTER_INVENTORY_HANDLER = register(new ExtendedScreenHandlerType<>(HelperForesterInventoryHandler::new), "HELPER_FORESTER_INVENTORY_HANDLER".toLowerCase());
    }

    private static <T extends ScreenHandler> ScreenHandlerType<T> register(ExtendedScreenHandlerType<T> screenHandler, String name) {
        return Registry.register(Registries.SCREEN_HANDLER, new Identifier(Concacity.ID, name), screenHandler);
    }
}
