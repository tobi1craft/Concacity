package de.tobi1craft.concacity.client.gui.forester;

import de.tobi1craft.concacity.Concacity;
import de.tobi1craft.concacity.client.key.ModKeys;
import de.tobi1craft.concacity.util.ModPackets;
import de.tobi1craft.concacity.util.enums.EntityVariables;
import io.wispforest.owo.ui.base.BaseOwoHandledScreen;
import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.*;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public class HelperForesterInventoryGUI extends BaseOwoHandledScreen<FlowLayout, HelperForesterInventoryHandler> {
    private static final Identifier TEXTURE = new Identifier("minecraft", "textures/gui/container/dispenser.png");

    public HelperForesterInventoryGUI(HelperForesterInventoryHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected @NotNull OwoUIAdapter<FlowLayout> createAdapter() {
        return OwoUIAdapter.create(this, Containers::verticalFlow);
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        rootComponent
                .surface(Surface.VANILLA_TRANSLUCENT)
                //.surface(Surface.tiled(TEXTURE, 0, 0))
                .horizontalAlignment(HorizontalAlignment.CENTER)
                .verticalAlignment(VerticalAlignment.CENTER);

        Text mode0 = Text.translatable("text.concacity.button.helper.guard.mode.selected");
        Text mode1 = Text.translatable("text.concacity.button.helper.guard.mode.confirm");
        Text mode2 = Text.translatable("text.concacity.button.helper.guard.mode.auto");
        Text mode;

        switch (handler.mode) {
            case 1 -> mode = mode1;
            case 2 -> mode = mode2;
            default -> mode = mode0;
        }
        ButtonComponent button;
        switch (handler.upgrade_mode) {
            case 1 -> button = Components.button(mode, buttonComponent -> {
                if (buttonComponent.getMessage().equals(mode0)) {
                    buttonComponent.setMessage(mode1);
                    Concacity.CHANNEL.clientHandle().send(new ModPackets.EntityIntegerPacket(handler.helperEntity.getUuid(), 1, EntityVariables.HELPER_MODE));
                } else if (buttonComponent.getMessage().equals(mode1)) {
                    buttonComponent.setMessage(mode0);
                    Concacity.CHANNEL.clientHandle().send(new ModPackets.EntityIntegerPacket(handler.helperEntity.getUuid(), 0, EntityVariables.HELPER_MODE));
                }
            });
            case 2 -> button = Components.button(mode, buttonComponent -> {
                if (buttonComponent.getMessage().equals(mode0)) {
                    buttonComponent.setMessage(mode1);
                    Concacity.CHANNEL.clientHandle().send(new ModPackets.EntityIntegerPacket(handler.helperEntity.getUuid(), 1, EntityVariables.HELPER_MODE));
                } else if (buttonComponent.getMessage().equals(mode1)) {
                    buttonComponent.setMessage(mode2);
                    Concacity.CHANNEL.clientHandle().send(new ModPackets.EntityIntegerPacket(handler.helperEntity.getUuid(), 2, EntityVariables.HELPER_MODE));
                } else if (buttonComponent.getMessage().equals(mode2)) {
                    buttonComponent.setMessage(mode0);
                    Concacity.CHANNEL.clientHandle().send(new ModPackets.EntityIntegerPacket(handler.helperEntity.getUuid(), 0, EntityVariables.HELPER_MODE));
                }
            });
            default ->
                    button = Components.button(Text.translatable("text.concacity.upgrade_required"), buttonComponent -> {
                    }).active(false);
        }


        rootComponent.child(
                Containers.grid(Sizing.fixed(400), Sizing.fixed(300), 9, 2)
                        .child(Components.button(Text.translatable("text.concacity.button.helper.miner"), buttonComponent -> switchToMiner()).margins(Insets.of(5)), 0, 0)
                        .child(Components.button(Text.translatable("text.concacity.button.helper.carrier"), buttonComponent -> switchToCarrier()).margins(Insets.of(5)), 0, 1)

                        .child(Components.label(Text.translatable("text.concacity.label.helper.guard.mode").append(" (")
                                .append(ModKeys.keyBinding.getBoundKeyLocalizedText()).append(")")).color(Color.BLACK).margins(Insets.of(5)), 1, 0)


                        .child(button.margins(Insets.of(5)), 1, 1)


                        .child(this.slotAsComponent(0).margins(Insets.of(5)), 2, 0)
                        .child(this.slotAsComponent(1).margins(Insets.of(5)), 2, 1)
                        .child(this.slotAsComponent(2).margins(Insets.of(5)), 3, 0)
                        .child(this.slotAsComponent(3).margins(Insets.of(5)), 3, 1)
                        .child(this.slotAsComponent(4).margins(Insets.of(5)), 4, 0)
                        .child(this.slotAsComponent(5).margins(Insets.of(5)), 4, 1)
                        .child(this.slotAsComponent(6).margins(Insets.of(5)), 5, 0)
                        .child(this.slotAsComponent(7).margins(Insets.of(5)), 5, 1)

                        .child(Components.entity(Sizing.fill(20), handler.helperEntity).allowMouseRotation(true).scaleToFit(true)
                                .verticalSizing(Sizing.fill(60)).margins(Insets.of(5)), 8, 0)


                        .padding(Insets.of(10))
                        .surface(Surface.PANEL)
                        //.surface(Surface.tiled(new Identifier(Concacity.ID, "textures/gui/HelperMinerInventory.png"), 100, 100)) TODO: Texture
                        .verticalAlignment(VerticalAlignment.CENTER)
                        .horizontalAlignment(HorizontalAlignment.LEFT)
        );
    }

    private void switchToMiner() {
        //TODO: Entity zu Miner
        Concacity.CHANNEL.clientHandle().send(new ModPackets.HelperGuiPacket(handler.helperEntity.getUuid(), handler.helperEntity.tabToOpen));
    }

    private void switchToCarrier() {
        //TODO: Entity zu Carrier
        Concacity.CHANNEL.clientHandle().send(new ModPackets.HelperGuiPacket(handler.helperEntity.getUuid(), handler.helperEntity.tabToOpen));
    }
}
