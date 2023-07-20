package de.tobi1craft.concacity.util;

import de.tobi1craft.concacity.Concacity;
import de.tobi1craft.concacity.client.gui.ModGUIs;
import io.wispforest.owo.network.OwoNetChannel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

public class ModChannels {

    private final static OwoNetChannel CHANNEL = Concacity.CHANNEL;

    public static void registerChannels() {
        Concacity.LOGGER.info("registering mod channels");

        CHANNEL.registerServerbound(ModPackets.OnlyPacket.class, (message, access) -> {
            PlayerEntity player = access.player();
            player.openHandledScreen(new NamedScreenHandlerFactory() {
                @Override
                public Text getDisplayName() {
                    return Text.literal("Überschrift vom Inventar");
                }

                @Override
                public @NotNull ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
                    return new ModGUIs(syncId, playerInventory, ScreenHandlerContext.create(player.getWorld(), player.getBlockPos()));
                }

            });
        });
    }
}
