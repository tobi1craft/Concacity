package de.tobi1craft.concacity.util;

import de.tobi1craft.concacity.data.ModModelProvider;
import de.tobi1craft.concacity.data.ModRecipeGenerator;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class ConcacityDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(ModRecipeGenerator::new);
        pack.addProvider(ModModelProvider::new);
    }
}
