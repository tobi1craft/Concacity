package de.tobi1craft.concacity.util;

import de.tobi1craft.concacity.Concacity;
import de.tobi1craft.concacity.entity.helper.HelperMinerEntity;
import io.wispforest.owo.network.OwoNetChannel;
import net.minecraft.server.network.ServerPlayerEntity;

public class ModChannels {

    private final static OwoNetChannel CHANNEL = Concacity.CHANNEL;

    public static void registerChannels() {
        Concacity.LOGGER.info("registering mod channels");

        CHANNEL.registerServerbound(ModPackets.GuiPacket.class, (message, access) -> {
            if (message.uuid() == null) return;
            access.player().openHandledScreen((HelperMinerEntity) access.player().getServerWorld().getEntity(message.uuid()));
        });

        CHANNEL.registerServerbound(ModPackets.EntityIntegerPacket.class, (message, access) -> {
            ServerPlayerEntity player = access.player();

            if (message.uuid() == null || message.variable() == null) return;
            switch (message.variable()) {
                case HELPER_UPGRADE_MODE -> {
                    HelperMinerEntity helperMinerEntity = (HelperMinerEntity) player.getServerWorld().getEntity(message.uuid());
                    assert helperMinerEntity != null;
                    helperMinerEntity.upgrade_mode = message.integer();
                    helperMinerEntity.markDirty();
                }
                case HELPER_MODE -> {
                    HelperMinerEntity helperMinerEntity = (HelperMinerEntity) player.getServerWorld().getEntity(message.uuid());
                    assert helperMinerEntity != null;
                    helperMinerEntity.mode = message.integer();
                    helperMinerEntity.markDirty();
                }
            }
        });
    }
}
