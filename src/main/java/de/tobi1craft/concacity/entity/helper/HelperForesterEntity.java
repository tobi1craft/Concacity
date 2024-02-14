package de.tobi1craft.concacity.entity.helper;

import de.tobi1craft.concacity.client.gui.forester.HelperForesterInventoryHandler;
import de.tobi1craft.concacity.entity.goal.MineTreeGoal;
import de.tobi1craft.concacity.util.enums.HelperInventoryTabs;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.World;

public class HelperForesterEntity extends HelperEntity {
    public int mode;
    public int upgrade_mode;
    public int upgrade_searchRadius;

    public HelperForesterEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world, 3);
    }


    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(0, new MineTreeGoal(this, 0.8D));

        //this.goalSelector.add(3, new MeleeAttackGoal(this, 1.2D, false)); TODO: vlt für Guard
    }


    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        mode = nbt.getInt("mode");
        upgrade_mode = nbt.getInt("upgrade_mode");
        upgrade_searchRadius = nbt.getInt("upgrade_searchRadius");
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.putInt("mode", mode);
        nbt.putInt("upgrade_mode", upgrade_mode);
        nbt.putInt("upgrade_searchRadius", upgrade_searchRadius);
        return super.writeNbt(nbt);
    }


    @Override
    public boolean isValid(int slot, ItemStack stack) {
        boolean isValid = true;
        switch (slot) {
            case 0 -> isValid = stack.isIn(ItemTags.AXES);
            case 1 -> isValid = stack.isIn(ItemTags.PICKAXES);
        }
        return isValid;
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        ScreenHandler handler = null;
        switch (tabToOpen) {
            case SETTINGS, UPGRADES, INVENTORY ->
                    handler = new HelperForesterInventoryHandler(syncId, playerInventory, this, ScreenHandlerContext.create(player.getWorld(), player.getBlockPos()), this);
        }
        return handler;
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("text.concacity.gui.helper.miner");
    }


    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeInt(upgrade_mode);
        buf.writeInt(mode);
    }


}
