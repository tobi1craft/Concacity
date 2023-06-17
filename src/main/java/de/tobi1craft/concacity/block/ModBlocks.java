package de.tobi1craft.concacity.block;

import de.tobi1craft.concacity.Concacity;
import de.tobi1craft.concacity.client.ModItemGroup;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlocks {

    public static Item CONCACITY_BLOCK_ITEM;
    public static final Block CONCACITY_BLOCK = registerBlock("concacity_block", new Block(FabricBlockSettings.create().mapColor(MapColor.BRIGHT_RED).strength(1F)));

    private static Block registerBlock(String name, Block block) {
        CONCACITY_BLOCK_ITEM = registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, new Identifier(Concacity.ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block) {
        Item item = Registry.register(Registries.ITEM, new Identifier(Concacity.ID, name), new BlockItem(block, new FabricItemSettings()));
        ItemGroupEvents.modifyEntriesEvent(ModItemGroup.CONCACITY).register(entries -> entries.add(item));
        return item;
    }


    public static void registerModBlocks() {
        Concacity.LOGGER.info("registering mod blocks");
        CONCACITY_BLOCK.getSlipperiness();
    }
}
