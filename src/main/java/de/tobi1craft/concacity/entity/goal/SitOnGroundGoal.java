package de.tobi1craft.concacity.entity.goal;

import de.tobi1craft.concacity.Concacity;
import de.tobi1craft.concacity.entity.helper.HelperEntity;
import de.tobi1craft.concacity.util.ModPackets;
import de.tobi1craft.concacity.util.enums.AnimationTypes;
import net.minecraft.entity.ai.goal.Goal;

public class SitOnGroundGoal extends Goal {

    private final HelperEntity entity;

    public SitOnGroundGoal(HelperEntity entity) {
        this.entity = entity;
    }

    @Override
    public boolean canStart() {
        return entity.getNavigation().isIdle() && !entity.hasActiveGoals();
    }

    @Override
    public void start() {
        Concacity.CHANNEL.serverHandle(entity.getServer()).send(new ModPackets.HelperAnimationPacket(entity.getUuid(), AnimationTypes.SIT, true));
    }

    @Override
    public void stop() {
        Concacity.CHANNEL.serverHandle(entity.getServer()).send(new ModPackets.HelperAnimationPacket(entity.getUuid(),AnimationTypes.SIT, false));
    }
}
