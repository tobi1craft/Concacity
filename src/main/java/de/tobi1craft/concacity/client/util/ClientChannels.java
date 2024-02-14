package de.tobi1craft.concacity.client.util;

import de.tobi1craft.concacity.Concacity;
import de.tobi1craft.concacity.entity.helper.HelperForesterEntity;
import de.tobi1craft.concacity.util.ModPackets;
import io.wispforest.owo.network.OwoNetChannel;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;

public class ClientChannels {
    private final static OwoNetChannel CHANNEL = Concacity.CHANNEL;

    public static void registerChannels() {
        Concacity.LOGGER.info("registering client mod channels");

        CHANNEL.registerClientbound(ModPackets.HelperAnimationPacket.class, (message, access) -> {
            ClientWorld world = MinecraftClient.getInstance().world;
            if(world == null) return;
            world.getEntities().forEach(entity -> {
                if(entity.getUuid().equals(message.uuid())) {
                    switch (message.animationType()){
                        case SIT -> ((HelperForesterEntity) entity).sitting = message.active();
                        case BREAK -> ((HelperForesterEntity) entity).breaking = message.active();
                    }

                }
            });
        });
    }
}
