package de.tobi1craft.concacity.client.entity;

import de.tobi1craft.concacity.Concacity;
import de.tobi1craft.concacity.entity.custom.HelperEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class HelperRenderer extends GeoEntityRenderer<HelperEntity> {
    public HelperRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new HelperModel());
    }

    @Override
    public Identifier getTextureLocation(HelperEntity animatable) {
        return new Identifier(Concacity.ID, "textures/entity/helper.png");
    }

    @Override
    public void render(HelperEntity entity, float entityYaw, float partialTick, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight) {
        if (entity.isBaby()) poseStack.scale(0.4f, 0.4f, 0.4f);
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
