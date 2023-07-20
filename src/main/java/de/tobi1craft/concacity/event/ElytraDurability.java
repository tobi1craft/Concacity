package de.tobi1craft.concacity.event;

import de.tobi1craft.concacity.Concacity;
import de.tobi1craft.concacity.client.gui.ModGUIs;
import net.fabricmc.fabric.api.entity.event.v1.EntityElytraEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ElytraDurability implements EntityElytraEvents.Custom {
    private final HashMap<UUID, Map.Entry<Integer, Item>> last = new HashMap<>();

    @Override
    public boolean useCustomElytra(LivingEntity entity, boolean tickElytra) {
        if (Concacity.CONFIG.elytra_durability_loss()) return false;
        PlayerEntity player = (PlayerEntity) entity;
        ItemStack chestSlot = player.getInventory().armor.get(2);
        if (chestSlot.isDamaged()) {
            if (last.containsKey(player.getUuid())) {
                Map.Entry<Integer, Item> map = last.get(player.getUuid());
                if (map.getValue() == chestSlot.getItem()) chestSlot.setDamage(map.getKey());
            }
        }
        last.put(player.getUuid(), new AbstractMap.SimpleEntry<>(chestSlot.getDamage(), chestSlot.getItem()));
        return false;
    }
}
