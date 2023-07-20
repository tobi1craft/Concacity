package de.tobi1craft.concacity.client.key;

import de.tobi1craft.concacity.Concacity;
import de.tobi1craft.concacity.util.ModPackets;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class ModKeys {

    private static final KeyBinding keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.concacity.test", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_R, "category.concacity.test"));

    public static void registerModKeys() {
        Concacity.LOGGER.info("registering mod keys");

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyBinding.wasPressed()) {
                Concacity.CHANNEL.clientHandle().send(new ModPackets.OnlyPacket());
            }
        });
    }
}
