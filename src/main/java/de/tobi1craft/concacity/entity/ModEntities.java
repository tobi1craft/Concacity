package de.tobi1craft.concacity.entity;

import de.tobi1craft.concacity.Concacity;
import de.tobi1craft.concacity.entity.helper.HelperForesterEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.poi.PointOfInterestType;

import java.util.Optional;

public class ModEntities {

    public static final EntityType<HelperForesterEntity> HELPER = Registry.register(Registries.ENTITY_TYPE, new Identifier(Concacity.ID, "helper"), FabricEntityTypeBuilder.create(SpawnGroup.MISC, HelperForesterEntity::new)
            .dimensions(EntityDimensions.fixed(0.6f, 1.8f)).build());
    @SuppressWarnings("unused")
    public static final PointOfInterestType TREE = PointOfInterestHelper.register(new Identifier(Concacity.ID, "tree"), 32, 100, Blocks.OAK_LOG);
    public static final MemoryModuleType<GlobalPos> TREE_POINT = Registry.register(Registries.MEMORY_MODULE_TYPE, new Identifier(Concacity.ID, "tree_point"), new MemoryModuleType<>(Optional.of(GlobalPos.CODEC)));


    public static void registerModEntities() {
        Concacity.LOGGER.info("registering mod entities");
        FabricDefaultAttributeRegistry.register(ModEntities.HELPER, HelperForesterEntity.setAttributes());
    }
}
