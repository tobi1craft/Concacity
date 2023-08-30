package de.tobi1craft.concacity.util;

import de.tobi1craft.concacity.Concacity;
import de.tobi1craft.concacity.client.gui.ModGUIs;
import de.tobi1craft.concacity.entity.custom.HelperEntity;
import io.wispforest.owo.network.OwoNetChannel;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ModChannels {

    private final static OwoNetChannel CHANNEL = Concacity.CHANNEL;

    public static void registerChannels() {
        Concacity.LOGGER.info("registering mod channels");

        CHANNEL.registerServerbound(ModPackets.GuiPacket.class, (message, access) -> {
            if (message.uuid() == null) return;
            access.player().openHandledScreen((HelperEntity) access.player().getServerWorld().getEntity(message.uuid()));
        });

        CHANNEL.registerServerbound(ModPackets.EntityIntegerPacket.class, (message, access) -> {
            ServerPlayerEntity player = access.player();

            if (message.uuid() == null || message.variable() == null) return;
            switch (message.variable()) {
                case HELPER_UPGRADE_MODE -> {
                    HelperEntity helperEntity = (HelperEntity) player.getServerWorld().getEntity(message.uuid());
                    assert helperEntity != null;
                    helperEntity.upgrade_mode = message.integer();
                    helperEntity.markDirty();
                }
                case HELPER_MODE -> {
                    HelperEntity helperEntity = (HelperEntity) player.getServerWorld().getEntity(message.uuid());
                    assert helperEntity != null;
                    helperEntity.mode = message.integer();
                    helperEntity.markDirty();
                }
            }
        });
    }
}
