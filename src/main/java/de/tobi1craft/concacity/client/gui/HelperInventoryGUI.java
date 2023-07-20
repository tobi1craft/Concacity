package de.tobi1craft.concacity.client.gui;

import de.tobi1craft.concacity.Concacity;
import io.wispforest.owo.ui.base.BaseOwoHandledScreen;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.*;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

public class HelperInventoryGUI extends BaseOwoHandledScreen<FlowLayout, ModGUIs> {
    public HelperInventoryGUI(ModGUIs handler, PlayerInventory inventory, Text title) {
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
                .horizontalAlignment(HorizontalAlignment.CENTER)
                .verticalAlignment(VerticalAlignment.CENTER);

        rootComponent.child(
                Containers.verticalScroll(Sizing.content(), Sizing.fill(50),
                        Containers.grid(Sizing.content(), Sizing.content(), 9, 2)
                                .child(Components.label(Text.literal("Test Label 1")).color(Color.BLACK).margins(Insets.of(5)), 0, 0)
                                .child(Components.checkbox(Text.empty()).margins(Insets.of(5)), 0, 1)

                                .child(Components.label(Text.literal("Label 2")).color(Color.BLACK).margins(Insets.of(5)), 1, 0)
                                .child(Components.checkbox(Text.empty()).onChanged(box -> Concacity.LOGGER.info("Test")).margins(Insets.of(5)), 1, 1)

                                .child(Components.label(Text.literal("Label 2")).color(Color.BLACK).margins(Insets.of(5)), 2, 0)
                                .child(Components.checkbox(Text.empty()).onChanged(box -> Concacity.LOGGER.info("Test")).margins(Insets.of(5)), 2, 1)

                                .child(Components.label(Text.literal("Label 2")).color(Color.BLACK).margins(Insets.of(5)), 3, 0)
                                .child(Components.checkbox(Text.empty()).onChanged(box -> Concacity.LOGGER.info("Test")).margins(Insets.of(5)), 3, 1)

                                .child(Components.label(Text.literal("Label 2")).color(Color.BLACK).margins(Insets.of(5)), 4, 0)
                                .child(Components.checkbox(Text.empty()).onChanged(box -> Concacity.LOGGER.info("Test")).margins(Insets.of(5)), 4, 1)

                                .child(Components.label(Text.literal("Label 2")).color(Color.BLACK).margins(Insets.of(5)), 5, 0)
                                .child(Components.checkbox(Text.empty()).onChanged(box -> Concacity.LOGGER.info("Test")).margins(Insets.of(5)), 5, 1)

                                .child(Components.label(Text.literal("Label 2")).color(Color.BLACK).margins(Insets.of(5)), 6, 0)
                                .child(Components.checkbox(Text.empty()).onChanged(box -> Concacity.LOGGER.info("Test")).margins(Insets.of(5)), 6, 1)

                                .child(Components.label(Text.literal("Label 2")).color(Color.BLACK).margins(Insets.of(5)), 7, 0)
                                .child(Components.checkbox(Text.empty()).onChanged(box -> Concacity.LOGGER.info("Test")).margins(Insets.of(5)), 7, 1)

                                .child(Components.box(Sizing.fill(30), Sizing.fill(30)), 8, 0)

                                .child(this.slotAsComponent(0), 8, 1)


                                .padding(Insets.of(10))
                                .surface(Surface.PANEL)
                                .verticalAlignment(VerticalAlignment.CENTER)
                                .horizontalAlignment(HorizontalAlignment.LEFT)
                )
        );
    }
}
