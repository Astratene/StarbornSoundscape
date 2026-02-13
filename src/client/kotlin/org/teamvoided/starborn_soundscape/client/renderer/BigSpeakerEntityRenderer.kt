package org.teamvoided.starborn_soundscape.client.renderer

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.EntityRenderer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier
import net.minecraft.util.math.Axis
import org.teamvoided.starborn_soundscape.StarbornSoundscape
import org.teamvoided.starborn_soundscape.entity.BigSpeakerEntity

@Environment(EnvType.CLIENT)
class BigSpeakerEntityRenderer(
    context: EntityRendererFactory.Context
) : EntityRenderer<BigSpeakerEntity>(context) {

    private val model = BigSpeakerEntityModel(
        context.getPart(BigSpeakerEntityModel.LAYER_LOCATION)
    )

    init {
        shadowRadius = 2f
    }

    override fun render(
        entity: BigSpeakerEntity,
        yaw: Float,
        tickDelta: Float,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int
    ) {
        matrices.push()

        matrices.translate(0.0, 1.5, 0.0)

        val pitch = entity.dataTracker.get(BigSpeakerEntity.TRACKED_PITCH)
        val yaw = entity.dataTracker.get(BigSpeakerEntity.TRACKED_YAW)

        matrices.rotateAround(
            Axis.Y_POSITIVE.rotationDegrees(-yaw),
            0f,
            0f,
            0f
        )
        matrices.rotateAround(
            Axis.X_POSITIVE.rotationDegrees(pitch -180),
            0f,
            0f,
            0f
        )

        val vertexConsumer = vertexConsumers.getBuffer(
            model.getLayer(getTexture(entity))
        )

        model.setAngles(entity, 0f, 0f, entity.age + tickDelta, 0f, 0f)
        model.diaphragm.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV)
        model.base.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV)
        model.bottomDecor.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV)


        matrices.pop()

        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light)
    }

    override fun getTexture(entity: BigSpeakerEntity): Identifier {
        return Identifier.of(StarbornSoundscape.MODID, "textures/entity/big_speaker.png")
    }
}
