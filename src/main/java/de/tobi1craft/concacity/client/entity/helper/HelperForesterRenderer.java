package de.tobi1craft.concacity.client.entity.helper;

import de.tobi1craft.concacity.Concacity;
import de.tobi1craft.concacity.entity.helper.HelperForesterEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.BlockAndItemGeoLayer;
import software.bernie.geckolib.renderer.layer.ItemArmorGeoLayer;

public class HelperForesterRenderer extends GeoEntityRenderer<HelperForesterEntity> {

    private static final String LEFT_HAND = "left_hand";
    private static final String RIGHT_HAND = "right_hand";
    private static final String LEFT_BOOT = "left_foot";
    private static final String RIGHT_BOOT = "right_foot";
    private static final String LEFT_ARMOR_LEG = "left_leg";
    private static final String RIGHT_ARMOR_LEG = "right_leg";
    private static final String CHESTPLATE = "body";
    private static final String RIGHT_SLEEVE = "right_arm";
    private static final String LEFT_SLEEVE = "left_arm";
    private static final String HELMET = "head";

    protected ItemStack mainHandItem;
    protected ItemStack offhandItem;

    //TODO: Rüstung (bis auf Schuhe) dreht sich 2fach so weit, wie Model
    public HelperForesterRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new HelperForesterModel());
        // Add some armor rendering
        addRenderLayer(new ItemArmorGeoLayer<>(this) {

            @Nullable
            @Override
            protected ItemStack getArmorItemForBone(GeoBone bone, HelperForesterEntity animatable) {
                // Return the items relevant to the bones being rendered for additional rendering
                return switch (bone.getName()) {
                    case LEFT_BOOT, RIGHT_BOOT -> this.bootsStack;
                    case LEFT_ARMOR_LEG, RIGHT_ARMOR_LEG -> this.leggingsStack;
                    case CHESTPLATE, RIGHT_SLEEVE, LEFT_SLEEVE -> this.chestplateStack;
                    case HELMET -> this.helmetStack;
                    default -> null;
                };
            }

            // Return the equipment slot relevant to the bone we're using
            @Override
            protected @NotNull EquipmentSlot getEquipmentSlotForBone(GeoBone bone, ItemStack stack, HelperForesterEntity animatable) {
                return switch (bone.getName()) {
                    case LEFT_BOOT, RIGHT_BOOT -> EquipmentSlot.FEET;
                    case LEFT_ARMOR_LEG, RIGHT_ARMOR_LEG -> EquipmentSlot.LEGS;
                    case RIGHT_SLEEVE -> !animatable.isLeftHanded() ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
                    case LEFT_SLEEVE -> animatable.isLeftHanded() ? EquipmentSlot.OFFHAND : EquipmentSlot.MAINHAND;
                    case CHESTPLATE -> EquipmentSlot.CHEST;
                    case HELMET -> EquipmentSlot.HEAD;
                    default -> super.getEquipmentSlotForBone(bone, stack, animatable);
                };
            }

            // Return the ModelPart responsible for the armor pieces we want to render
            @Override
            protected @NotNull ModelPart getModelPartForBone(GeoBone bone, EquipmentSlot slot, ItemStack stack, HelperForesterEntity animatable, BipedEntityModel<?> baseModel) {
                return switch (bone.getName()) {
                    case LEFT_BOOT, LEFT_ARMOR_LEG -> baseModel.leftLeg;
                    case RIGHT_BOOT, RIGHT_ARMOR_LEG -> baseModel.rightLeg;
                    case RIGHT_SLEEVE -> baseModel.rightArm;
                    case LEFT_SLEEVE -> baseModel.leftArm;
                    case CHESTPLATE -> baseModel.body;
                    case HELMET -> baseModel.head;
                    default -> super.getModelPartForBone(bone, slot, stack, animatable, baseModel);
                };
            }
        });
        // Add some held item rendering
        addRenderLayer(new BlockAndItemGeoLayer<>(this) {
            @Nullable
            @Override
            protected ItemStack getStackForBone(GeoBone bone, HelperForesterEntity animatable) {
                // Retrieve the items in the entityUUID's hands for the relevant bone
                return switch (bone.getName()) {
                    case LEFT_HAND ->
                            animatable.isLeftHanded() ? HelperForesterRenderer.this.mainHandItem : HelperForesterRenderer.this.offhandItem;
                    case RIGHT_HAND ->
                            animatable.isLeftHanded() ? HelperForesterRenderer.this.offhandItem : HelperForesterRenderer.this.mainHandItem;
                    default -> null;
                };
            }


            @Override
            protected ModelTransformationMode getTransformTypeForStack(GeoBone bone, ItemStack stack, HelperForesterEntity animatable) {
                // Apply the camera transform for the given hand
                return switch (bone.getName()) {
                    case LEFT_HAND, RIGHT_HAND -> ModelTransformationMode.THIRD_PERSON_RIGHT_HAND;
                    default -> ModelTransformationMode.NONE;
                };
            }

            @Override
            public void renderForBone(MatrixStack poseStack, HelperForesterEntity animatable, GeoBone bone, RenderLayer renderType, VertexConsumerProvider bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
                if (bone.getName().equals(HELMET)) {
                    //TODO: Kopf vorher drehen aus HelperForesterModel.java auch für Helm
                }

                super.renderForBone(poseStack, animatable, bone, renderType, bufferSource, buffer, partialTick, packedLight, packedOverlay);
            }

            // Do some quick render modifications depending on what the item is
            @Override
            protected void renderStackForBone(MatrixStack poseStack, GeoBone bone, ItemStack stack, HelperForesterEntity animatable, VertexConsumerProvider bufferSource, float partialTick, int packedLight, int packedOverlay) {
                if (stack == HelperForesterRenderer.this.mainHandItem) {
                    poseStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90f));

                    if (stack.getItem() instanceof ShieldItem) poseStack.translate(0, 0.125, -0.25);
                } else if (stack == HelperForesterRenderer.this.offhandItem) {
                    poseStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90f));

                    if (stack.getItem() instanceof ShieldItem) {
                        poseStack.translate(0, 0.125, 0.25);
                        poseStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180));
                    }
                }

                super.renderStackForBone(poseStack, bone, stack, animatable, bufferSource, partialTick, packedLight, packedOverlay);
            }
        });
    }

    @Override
    public Identifier getTextureLocation(HelperForesterEntity animatable) {
        return new Identifier(Concacity.ID, "textures/entity/helper_forester.png");
    }

    @Override
    public void preRender(MatrixStack poseStack, HelperForesterEntity animatable, BakedGeoModel model, VertexConsumerProvider bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        this.mainHandItem = animatable.getMainHandStack();
        this.offhandItem = animatable.getOffHandStack();
    }
}
