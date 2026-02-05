package org.teamvoided.starborn_soundscape.client.renderer

import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.model.*
import net.minecraft.client.render.entity.model.EntityModel
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.Entity

class CosmicBoltEntityModel(
    root: ModelPart
) : EntityModel<Entity>() {

    private val bbMain: ModelPart = root.getChild("bb_main")

    companion object {
        fun getTexturedModelData(): TexturedModelData {
            val modelData = ModelData()
            val root = modelData.root

            val bbMain = root.addChild(
                "bb_main",
                ModelPartBuilder.create()
                    .uv(4, 0)
                    .cuboid(-1.0f, -9.0f, -1.0f, 2.0f, 9.0f, 2.0f)
                    .uv(0, 0)
                    .cuboid(-0.5f, -15.0f, -0.5f, 1.0f, 15.0f, 1.0f),
                ModelTransform.pivot(0.0f, 24.0f, 0.0f)
            )

            bbMain.addChild(
                "cube_r1",
                ModelPartBuilder.create()
                    .uv(12, 12)
                    .cuboid(-1.0f, -4.0f, -1.0f, 2.0f, 4.0f, 2.0f),
                ModelTransform.of(0.0f, 0.0f, 0.0f, -0.3927f, 0.0f, 0.0f)
            )

            bbMain.addChild(
                "cube_r2",
                ModelPartBuilder.create()
                    .uv(12, 6)
                    .cuboid(-1.0f, -4.0f, -1.0f, 2.0f, 4.0f, 2.0f),
                ModelTransform.of(0.0f, 0.0f, 0.0f, 0.4363f, 0.0f, 0.0f)
            )

            bbMain.addChild(
                "cube_r3",
                ModelPartBuilder.create()
                    .uv(12, 0)
                    .cuboid(-1.0f, -4.0f, -1.0f, 2.0f, 4.0f, 2.0f),
                ModelTransform.of(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.4363f)
            )

            bbMain.addChild(
                "cube_r4",
                ModelPartBuilder.create()
                    .uv(4, 11)
                    .cuboid(-1.0f, -4.0f, -1.0f, 2.0f, 4.0f, 2.0f),
                ModelTransform.of(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, -0.4363f)
            )

            return TexturedModelData.of(modelData, 32, 32)
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
    }

    override fun method_2828(
        matrices: MatrixStack?,
        vertexConsumer: VertexConsumer?,
        i: Int,
        j: Int,
        k: Int
    ) {
        bbMain.render(matrices, vertexConsumer, i, j)
    }
}
