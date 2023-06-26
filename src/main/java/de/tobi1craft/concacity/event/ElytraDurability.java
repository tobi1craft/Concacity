package de.tobi1craft.concacity.event;

import net.fabricmc.fabric.api.entity.event.v1.EntityElytraEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class ElytraDurability implements EntityElytraEvents.Custom {
    @Override
    public boolean useCustomElytra(LivingEntity entity, boolean tickElytra) {
        PlayerEntity player = (PlayerEntity) entity;
        ItemStack chestSlot = player.getInventory().armor.get(2);
        if (chestSlot.isDamaged()) chestSlot.setDamage(0);
        return true;
    }
}
