package de.tobi1craft.concacity.item;

import de.tobi1craft.concacity.Concacity;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {

    public static final Item test_item = registerItem("test_item", new Item(new FabricItemSettings()));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(Concacity.ID, name), item);
    }

    public static void registerModItems() {

    }
}
