package de.tobi1craft.concacity.entity.goal;

import de.tobi1craft.concacity.Concacity;
import net.minecraft.entity.ai.goal.Goal;

public class TestGoal extends Goal {
    @Override
    public boolean canStart() {
        Concacity.LOGGER.error("canStart");
        return true;
    }

    @Override
    public void start() {
        Concacity.LOGGER.error("start");
    }

    @Override
    public void tick() {
        Concacity.LOGGER.error("tick");
    }

    @Override
    public void stop() {
        Concacity.LOGGER.error("stop");
    }

    @Override
    public boolean shouldRunEveryTick() {
        return true;
    }

    @Override
    public boolean shouldContinue() {
        Concacity.LOGGER.error("shouldContinue");
        return true;
    }

    @Override
    public boolean canStop() {
        Concacity.LOGGER.error("canStop");
        return true;
    }
}
