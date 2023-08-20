package de.tobi1craft.concacity.client.entity;

import de.tobi1craft.concacity.Concacity;
import de.tobi1craft.concacity.entity.custom.HelperEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class HelperModel extends GeoModel<HelperEntity> {
    @Override
    public Identifier getModelResource(HelperEntity animatable) {
        return new Identifier(Concacity.ID, "geo/helper.geo.json");
    }

    @Override
    public Identifier getTextureResource(HelperEntity animatable) {
        return new Identifier(Concacity.ID, "textures/entity/helper.png");
    }

    @Override
    public Identifier getAnimationResource(HelperEntity animatable) {
        return new Identifier(Concacity.ID, "animations/helper.animation.json");
    }

    @Override
    public void setCustomAnimations(HelperEntity animatable, long instanceId, AnimationState<HelperEntity> animationState) {
        CoreGeoBone head = getAnimationProcessor().getBone("head");
        if (head != null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
            head.setRotX(entityData.headPitch() * MathHelper.RADIANS_PER_DEGREE);
            head.setRotY(entityData.netHeadYaw() * MathHelper.RADIANS_PER_DEGREE);
        }
    }
}
