package org.teamvoided.starborn_soundscape.client.renderer

import com.ibm.icu.util.CodePointTrie
import com.mojang.blaze3d.vertex.VertexConsumer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.Axis
import net.minecraft.util.math.MathHelper
import org.joml.Matrix4f
import software.bernie.geckolib.renderer.GeoEntityRenderer
import org.teamvoided.starborn_soundscape.entity.SmallSpeakerEntity
import software.bernie.geckolib.cache.`object`.BakedGeoModel

@Environment(EnvType.CLIENT)
class SmallSpeakerEntityRenderer(
    ctx: EntityRendererFactory.Context
) : GeoEntityRenderer<SmallSpeakerEntity>(
    ctx,
    SmallSpeakerEntityModel()
) {

    init {
        shadowRadius = 0.25f
    }

    override fun preRender(
        matrices: MatrixStack,
        entity: SmallSpeakerEntity,
        model: BakedGeoModel,
        vertexConsumers: VertexConsumerProvider?,
        vertices: VertexConsumer?,
        isReRender: Boolean,
        tick: Float,
        light: Int,
        overlay: Int,
        colour: Int
    ) {
        matrices.translate(0.0, -0.25, 0.0)

        val pitch = entity.dataTracker.get(SmallSpeakerEntity.TRACKED_PITCH)
        val yaw = entity.dataTracker.get(SmallSpeakerEntity.TRACKED_YAW)


        matrices.rotateAround(
            Axis.Y_POSITIVE.rotationDegrees(-yaw),
            0f,
            0f,
            0f
        )
        matrices.rotateAround(
            Axis.X_POSITIVE.rotationDegrees(pitch),
            0f,
            0f,
            0f
        )

        super.preRender(
            matrices,
            entity,
            model,
            vertexConsumers,
            vertices,
            isReRender,
            tick,
            light,
            overlay,
            colour
        )
    }
}
