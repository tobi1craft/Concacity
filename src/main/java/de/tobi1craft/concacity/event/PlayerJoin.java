package de.tobi1craft.concacity.event;

import de.tobi1craft.concacity.Concacity;
import de.tobi1craft.concacity.entity.helper.HelperEntity;
import de.tobi1craft.concacity.util.ModPackets;
import de.tobi1craft.concacity.util.enums.AnimationTypes;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.util.TypeFilter;

public class PlayerJoin implements ServerPlayConnectionEvents.Join {
    @Override
    public void onPlayReady(ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server) {
        server.getWorlds().forEach(world -> world.getEntitiesByType(TypeFilter.instanceOf(HelperEntity.class), EntityPredicates.VALID_ENTITY).forEach(entity -> {
            Concacity.CHANNEL.serverHandle(entity.getServer()).send(new ModPackets.HelperAnimationPacket(entity.getUuid(), AnimationTypes.SIT, entity.sitting));
            Concacity.CHANNEL.serverHandle(entity.getServer()).send(new ModPackets.HelperAnimationPacket(entity.getUuid(), AnimationTypes.BREAK, entity.breaking));
        }));
    }
}
