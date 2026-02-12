package org.teamvoided.starborn_soundscape.client.renderer

import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.model.ModelData
import net.minecraft.client.model.ModelPart
import net.minecraft.client.model.ModelPartBuilder
import net.minecraft.client.model.ModelTransform
import net.minecraft.client.model.TexturedModelData
import net.minecraft.client.render.entity.model.EntityModel
import net.minecraft.client.render.entity.model.EntityModelLayer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.Entity
import net.minecraft.util.Identifier
import org.teamvoided.starborn_soundscape.StarbornSoundscape

class BigSpeakerEntityModel(
    root: ModelPart
) : EntityModel<Entity>() {

    public val diaphragm: ModelPart = root.getChild("diaphragm")
    public val base: ModelPart = root.getChild("base")
    public val bottomDecor: ModelPart = root.getChild("bottomdecor")

    companion object {

        val LAYER_LOCATION = EntityModelLayer(
            Identifier.of(StarbornSoundscape.MODID, "big_speaker"),
            "main"
        )

        fun getTexturedModelData(): TexturedModelData {
            val modelData = ModelData()
            val root = modelData.root

            val diaphragm = root.addChild(
                "diaphragm",
                ModelPartBuilder.create(),
                ModelTransform.pivot(0.0f, 24.0f, 0.0f)
            )

            diaphragm.addChild(
                "inner",
                ModelPartBuilder.create()
                    .uv(146, 144)
                    .cuboid(-2.0f, -17.5f, -15.0f, 4.0f, 4.0f, 1.0f)
                    .uv(116, 143)
                    .cuboid(-3.0f, -17.5f, -14.5f, 6.0f, 4.0f, 1.0f)
                    .uv(64, 145)
                    .cuboid(-2.0f, -18.5f, -14.5f, 4.0f, 6.0f, 1.0f),
                ModelTransform.NONE
            )

            diaphragm.addChild(
                "middle",
                ModelPartBuilder.create()
                    .uv(128, 43)
                    .cuboid(-11.0f, -26.5f, -14.0f, 22.0f, 22.0f, 0.0f),
                ModelTransform.NONE
            )

            diaphragm.addChild(
                "outer",
                ModelPartBuilder.create()
                    .uv(74, 145)
                    .cuboid(-11.0f, -9.0f, -15.0f, 4.0f, 4.0f, 1.0f)
                    .uv(84, 145)
                    .cuboid(7.0f, -9.0f, -15.0f, 4.0f, 4.0f, 1.0f)
                    .uv(94, 145)
                    .cuboid(7.0f, -26.5f, -15.0f, 4.0f, 4.0f, 1.0f)
                    .uv(146, 139)
                    .cuboid(-11.0f, -26.5f, -15.0f, 4.0f, 4.0f, 1.0f)
                    .uv(64, 134)
                    .cuboid(-7.0f, -8.0f, -14.5f, 14.0f, 3.0f, 1.0f)
                    .uv(108, 139)
                    .cuboid(-7.0f, -26.5f, -14.5f, 14.0f, 3.0f, 1.0f)
                    .uv(138, 139)
                    .cuboid(-11.0f, -22.5f, -14.5f, 3.0f, 14.0f, 1.0f)
                    .uv(108, 143)
                    .cuboid(8.0f, -22.5f, -14.5f, 3.0f, 14.0f, 1.0f),
                ModelTransform.NONE
            )

            root.addChild(
                "base",
                ModelPartBuilder.create()
                    .uv(0, 0)
                    .cuboid(-16.0f, -5.0f, -16.0f, 32.0f, 5.0f, 32.0f)
                    .uv(128, 0)
                    .cuboid(-16.0f, -43.0f, 11.0f, 32.0f, 38.0f, 5.0f)
                    .uv(0, 37)
                    .cuboid(-16.0f, -48.0f, -16.0f, 32.0f, 5.0f, 32.0f)
                    .uv(98, 74)
                    .cuboid(-16.0f, -43.0f, -16.0f, 5.0f, 38.0f, 27.0f)
                    .uv(0, 106)
                    .cuboid(11.0f, -43.0f, -16.0f, 5.0f, 38.0f, 27.0f)
                    .uv(0, 74)
                    .cuboid(-11.0f, -31.5f, -16.0f, 22.0f, 5.0f, 27.0f),
                ModelTransform.pivot(0.0f, 24.0f, 0.0f)
            )

            val bottomDecor = root.addChild(
                "bottomdecor",
                ModelPartBuilder.create(),
                ModelTransform.pivot(0.0f, 24.0f, 0.0f)
            )

            bottomDecor.addChild(
                "cube_r1",
                ModelPartBuilder.create()
                    .uv(64, 139)
                    .cuboid(-10.0f, 0.0f, -1.0f, 20.0f, 4.0f, 2.0f),
                ModelTransform.of(0.0f, -44.0f, -15.0f, 1.2217f, 0.0f, 0.0f)
            )

            bottomDecor.addChild(
                "cube_r2",
                ModelPartBuilder.create()
                    .uv(128, 65)
                    .cuboid(-10.0f, -4.0f, -1.0f, 20.0f, 4.0f, 2.0f),
                ModelTransform.of(0.0f, -30.5f, -15.0f, -1.2217f, 0.0f, 0.0f)
            )

            bottomDecor.addChild(
                "cube_r3",
                ModelPartBuilder.create()
                    .uv(64, 120)
                    .cuboid(-12.0f, -12.0f, -1.0f, 13.0f, 12.0f, 2.0f),
                ModelTransform.of(10.5f, -31.5f, -14.5f, 0.0f, 0.3491f, 0.0f)
            )

            bottomDecor.addChild(
                "cube_r4",
                ModelPartBuilder.create()
                    .uv(64, 106)
                    .cuboid(-1.0f, -12.0f, -1.0f, 13.0f, 12.0f, 2.0f),
                ModelTransform.of(-10.5f, -31.5f, -14.5f, 0.0f, -0.3491f, 0.0f)
            )

            return TexturedModelData.of(modelData, 256, 256)
        }
    }

    override fun setAngles(
        entity: Entity,
        limbSwing: Float,
        limbSwingAmount: Float,
        ageInTicks: Float,
        netHeadYaw: Float,
        headPitch: Float
    ) {
        // Add animation here later if needed
    }

    override fun method_2828(
        matrices: MatrixStack?,
        vertexConsumer: VertexConsumer?,
        i: Int,
        j: Int,
        k: Int
    ) {
        diaphragm.render(matrices, vertexConsumer, i, j)
        base.render(matrices, vertexConsumer, i, j)
        bottomDecor.render(matrices, vertexConsumer, i, j)
    }
}
