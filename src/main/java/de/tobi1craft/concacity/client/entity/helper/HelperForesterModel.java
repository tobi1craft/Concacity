package de.tobi1craft.concacity.client.entity.helper;

import de.tobi1craft.concacity.Concacity;
import de.tobi1craft.concacity.entity.helper.HelperForesterEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class HelperForesterModel extends GeoModel<HelperForesterEntity> {
    @Override
    public Identifier getModelResource(HelperForesterEntity animatable) {
        return new Identifier(Concacity.ID, "geo/helper_forester.geo.json");
    }

    @Override
    public Identifier getTextureResource(HelperForesterEntity animatable) {
        return new Identifier(Concacity.ID, "textures/entity/helper_forester.png");
    }

    @Override
    public Identifier getAnimationResource(HelperForesterEntity animatable) {
        return new Identifier(Concacity.ID, "animations/helper_forester.animation.json");
    }

    @Override
    public void setCustomAnimations(HelperForesterEntity animatable, long instanceId, AnimationState<HelperForesterEntity> animationState) {
        CoreGeoBone head = getAnimationProcessor().getBone("head");
        if (head != null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
            //head.setRotX(entityData.headPitch() * MathHelper.RADIANS_PER_DEGREE);         <- TODO: siehe HelperForesterRenderer.java
            //head.setRotY(entityData.netHeadYaw() * MathHelper.RADIANS_PER_DEGREE);        <-
        }
        CoreGeoBone hammer = getAnimationProcessor().getBone("axe");
        CoreGeoBone belt = getAnimationProcessor().getBone("belt");
        CoreGeoBone knife = getAnimationProcessor().getBone("knife");
        if (!animatable.getEquippedStack(EquipmentSlot.CHEST).isEmpty() && animatable.getEquippedStack(EquipmentSlot.CHEST).getItem().getTranslationKey().contains("chest")) {
            if(hammer != null) hammer.setPosZ(hammer.getInitialSnapshot().getOffsetZ() + 0.75f);
            if(knife != null) knife.setPosZ(knife.getInitialSnapshot().getOffsetZ() + 0.75f);
            if(belt != null) {
                belt.setScaleZ(belt.getInitialSnapshot().getScaleZ() + 0.5f);
                belt.setScaleX(belt.getInitialSnapshot().getScaleX() + 0.25f);
            }
        }
    }
}