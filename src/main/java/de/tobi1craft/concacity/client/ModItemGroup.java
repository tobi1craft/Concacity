package de.tobi1craft.concacity.client;

import de.tobi1craft.concacity.Concacity;
import de.tobi1craft.concacity.item.ModItems;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroup {

    public static final RegistryKey<ItemGroup> CONCACITY = RegistryKey.of(RegistryKeys.ITEM_GROUP, new Identifier(Concacity.ID, "concacity"));

    public static void registerItemGroup() {
        Concacity.LOGGER.info("registering mod item group(s)");
        Registry.register(Registries.ITEM_GROUP, CONCACITY, FabricItemGroup.builder()
                .displayName(Text.translatable("group.concacity"))
                .icon(() -> new ItemStack(ModItems.CONCACITY))
                .build());
    }
}
