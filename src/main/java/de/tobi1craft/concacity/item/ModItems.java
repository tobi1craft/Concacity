package de.tobi1craft.concacity.item;

import de.tobi1craft.concacity.Concacity;
import de.tobi1craft.concacity.client.ModItemGroup;
import de.tobi1craft.concacity.entity.ModEntities;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {

    //TODO: besseres Item zum spawnen / Ritual/Structure ????
    public static final Item HELPER_SPAWN_EGG = registerItem("helper_spawn_egg", new SpawnEggItem(ModEntities.HELPER, 0x0000ff, 0x7a7a7a,new FabricItemSettings()));


    public static void addItemsToItemGroup() {
        addToItemGroup(HELPER_SPAWN_EGG);
    }


    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(Concacity.ID, name), item);
    }

    private static void addToItemGroup(Item item) {
        ItemGroupEvents.modifyEntriesEvent(ModItemGroup.CONCACITY).register(entries -> entries.add(item));
    }

    public static void registerModItems() {
        Concacity.LOGGER.info("registering mod items");
        addItemsToItemGroup();
    }
}
