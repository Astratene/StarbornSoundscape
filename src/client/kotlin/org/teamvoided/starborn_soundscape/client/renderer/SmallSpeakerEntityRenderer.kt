package org.teamvoided.starborn_soundscape.client.renderer

import com.mojang.blaze3d.vertex.VertexConsumer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.util.math.MatrixStack
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

        val pitch = MathHelper.lerp(
            tick,
            entity.dataTracker.get(SmallSpeakerEntity.TRACKED_PITCH),
            entity.dataTracker.get(SmallSpeakerEntity.TRACKED_PITCH)
        )

        val yaw = MathHelper.lerp(
            tick,
            entity.dataTracker.get(SmallSpeakerEntity.TRACKED_YAW),
            entity.dataTracker.get(SmallSpeakerEntity.TRACKED_YAW)
        )

        matrices.multiply(
            Matrix4f().rotateY(
                Math.toRadians((entity.getYaw(-yaw)).toDouble()).toFloat()
            )
        )

        matrices.multiply(
            Matrix4f().rotateX(
                Math.toRadians((entity.getPitch(pitch)).toDouble()).toFloat()
            )
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
