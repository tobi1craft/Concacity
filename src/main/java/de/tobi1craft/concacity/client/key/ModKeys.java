package de.tobi1craft.concacity.client.key;

import de.tobi1craft.concacity.Concacity;
import de.tobi1craft.concacity.entity.helper.HelperMinerEntity;
import de.tobi1craft.concacity.util.ModPackets;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;

public class ModKeys {

    public static final KeyBinding keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.concacity.test", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_R, "category.concacity.test"));

    public static void registerModKeys() {
        Concacity.LOGGER.info("registering mod keys");

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyBinding.wasPressed()) {
                if (!(client.targetedEntity instanceof HelperMinerEntity)) {
                    assert client.player != null;
                    client.player.sendMessage(Text.translatable("text.concacity.error.no_targeted_helper_entity").formatted(Formatting.DARK_RED));
                    return;
                }
                Concacity.CHANNEL.clientHandle().send(new ModPackets.GuiPacket(client.targetedEntity.getUuid()));
            }
        });
    }
}
