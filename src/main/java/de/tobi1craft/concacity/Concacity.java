package de.tobi1craft.concacity;

import de.tobi1craft.concacity.item.ModItems;
import net.fabricmc.api.ModInitializer;

public class Concacity implements ModInitializer {
    public static String ID = "concacity";
    @Override
    public void onInitialize() {
        ModItems.registerModItems();
    }
}
