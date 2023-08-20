package de.tobi1craft.concacity.client;

import de.tobi1craft.concacity.client.gui.ModGUIs;
import de.tobi1craft.concacity.client.key.ModKeys;
import de.tobi1craft.concacity.client.util.ClientChannels;
import de.tobi1craft.concacity.entity.ModEntities;
import de.tobi1craft.concacity.client.entity.HelperRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

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
