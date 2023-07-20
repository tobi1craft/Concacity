package de.tobi1craft.concacity.client;

import de.tobi1craft.concacity.Concacity;
import de.tobi1craft.concacity.client.gui.ModGUIs;
import de.tobi1craft.concacity.client.key.ModKeys;
import de.tobi1craft.concacity.client.util.ClientChannels;
import de.tobi1craft.concacity.entity.ModEntities;
import de.tobi1craft.concacity.entity.client.HelperRenderer;
import de.tobi1craft.concacity.client.gui.HelperInventoryGUI;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class ConcacityClient implements ClientModInitializer {



    @Override
    public void onInitializeClient() {
        ClientChannels.registerChannels();
        ModGUIs.registerModGUIs();
        ModKeys.registerModKeys();

        EntityRendererRegistry.register(ModEntities.HELPER, HelperRenderer::new);
    }
}
