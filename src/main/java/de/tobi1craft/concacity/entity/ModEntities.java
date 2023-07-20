package de.tobi1craft.concacity.entity;

import de.tobi1craft.concacity.Concacity;
import de.tobi1craft.concacity.entity.custom.HelperEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {

    public static final EntityType<HelperEntity> HELPER = Registry.register(Registries.ENTITY_TYPE, new Identifier(Concacity.ID, "helper"), FabricEntityTypeBuilder.create(SpawnGroup.MISC, HelperEntity::new)
            .dimensions(EntityDimensions.fixed(1.5f, 1.75f)).build());


    public static void registerModEntities() {
        Concacity.LOGGER.info("registering mod entities");
        FabricDefaultAttributeRegistry.register(ModEntities.HELPER, HelperEntity.createMobAttributes());
    }
}
