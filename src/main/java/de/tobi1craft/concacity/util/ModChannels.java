package de.tobi1craft.concacity.util;

import de.tobi1craft.concacity.Concacity;
import de.tobi1craft.concacity.client.gui.ModGUIs;
import de.tobi1craft.concacity.entity.custom.HelperEntity;
import io.wispforest.owo.network.OwoNetChannel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;

public class ModChannels {

    private final static OwoNetChannel CHANNEL = Concacity.CHANNEL;

    public static void registerChannels() {
        Concacity.LOGGER.info("registering mod channels");

        CHANNEL.registerServerbound(ModPackets.GuiPacket.class, (message, access) -> {
            PlayerEntity player = access.player();

            if (message.gui() == null || message.uuid() == null) return;
            switch (message.gui()) {
                case GUARD -> player.openHandledScreen(new NamedScreenHandlerFactory() {
                    @Override
                    public Text getDisplayName() {
                        return Text.translatable("text.concacity.inventory.helper").formatted(Formatting.DARK_GREEN);
                    }

                    @Override
                    public @NotNull ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
                        return new ModGUIs(syncId, playerInventory, ScreenHandlerContext.create(player.getWorld(), player.getBlockPos()), ((ServerPlayerEntity) player).getServerWorld().getEntity(message.uuid()));
                    }
                });
            }
        });

        CHANNEL.registerServerbound(ModPackets.EntityIntegerPacket.class, (message, access) -> {
            ServerPlayerEntity player = access.player();

            if (message.uuid() == null || message.variable() == null) return;
            switch (message.variable()) {
                case HELPER_MODE -> {
                    HelperEntity helperEntity = (HelperEntity) player.getServerWorld().getEntity(message.uuid());
                    assert helperEntity != null;
                    helperEntity.mode = message.integer();
                    helperEntity.markDirty();
                }
                case HELPER_UPGRADE_MODE -> {
                    HelperEntity helperEntity = (HelperEntity) player.getServerWorld().getEntity(message.uuid());
                    assert helperEntity != null;
                    helperEntity.upgrade_mode = message.integer();
                    helperEntity.markDirty();
                }
            }
        });

        CHANNEL.registerServerbound(ModPackets.RequestHelperGUIPacket.class, (message, access) -> {
            ServerPlayerEntity player = access.player();
            HelperEntity helperEntity = (HelperEntity) player.getServerWorld().getEntity(message.uuid());
            assert helperEntity != null;
            CHANNEL.serverHandle(access.player()).send(new ModPackets.HelperGUIPacket(message.uuid(), message.gui(), helperEntity.upgrade_mode, helperEntity.mode));
        });
    }
}
