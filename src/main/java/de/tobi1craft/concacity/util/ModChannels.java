package de.tobi1craft.concacity.util;

import de.tobi1craft.concacity.Concacity;
import de.tobi1craft.concacity.entity.helper.HelperEntity;
import de.tobi1craft.concacity.entity.helper.HelperForesterEntity;
import io.wispforest.owo.network.OwoNetChannel;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;

public class ModChannels {

    private final static OwoNetChannel CHANNEL = Concacity.CHANNEL;

    public static void registerChannels() {
        Concacity.LOGGER.info("registering mod channels");

        CHANNEL.registerServerbound(ModPackets.HelperGuiPacket.class, (message, access) -> {
            if (message.entityUUID() == null) return;
            Entity entity = access.player().getServerWorld().getEntity(message.entityUUID());
            if(entity instanceof HelperEntity helperEntity) {
                helperEntity.tabToOpen = message.inventoryTab();
                access.player().openHandledScreen(helperEntity);
            }
        });

        CHANNEL.registerServerbound(ModPackets.EntityIntegerPacket.class, (message, access) -> {
            ServerPlayerEntity player = access.player();

            if (message.uuid() == null || message.variable() == null) return;
            switch (message.variable()) {
                case HELPER_UPGRADE_MODE -> {
                    HelperForesterEntity helperForesterEntity = (HelperForesterEntity) player.getServerWorld().getEntity(message.uuid());
                    assert helperForesterEntity != null;
                    helperForesterEntity.upgrade_mode = message.integer();
                    helperForesterEntity.markDirty();
                }
                case HELPER_MODE -> {
                    HelperForesterEntity helperForesterEntity = (HelperForesterEntity) player.getServerWorld().getEntity(message.uuid());
                    assert helperForesterEntity != null;
                    helperForesterEntity.mode = message.integer();
                    helperForesterEntity.markDirty();
                }
            }
        });
    }
}
