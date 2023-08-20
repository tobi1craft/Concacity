package de.tobi1craft.concacity.entity.goal;

import de.tobi1craft.concacity.entity.custom.HelperEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.MobEntity;

import java.util.EnumSet;

public class MineTreeGoal extends Goal {
    private final MobEntity mob;
    private double deltaX;
    private double deltaZ;
    private int lookTime;

    public MineTreeGoal(MobEntity mob) {
        this.mob = mob;
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
    }

    @Override
    public boolean canStart() {
        return true;
        //return this.mob.getRandom().nextFloat() < 0.02F;
    }

    @Override
    public boolean shouldContinue() {
        return true;
        //return this.lookTime >= 0;
    }

    @Override
    public boolean shouldRunEveryTick() {
        return true;
    }

    @Override
    public void start() {
        double d = 6.283185307179586 * this.mob.getRandom().nextDouble();
        this.deltaX = Math.cos(d);
        this.deltaZ = Math.sin(d);
        this.lookTime = 20 + this.mob.getRandom().nextInt(20);
    }

    @Override
    public void stop() {
        super.stop();
    }

    @Override
    public void tick() {
        --this.lookTime;
        this.mob.getLookControl().lookAt(this.mob.getX() + this.deltaX, this.mob.getEyeY(), this.mob.getZ() + this.deltaZ);
        HelperEntity helperEntity = (HelperEntity) mob;
    }
}
