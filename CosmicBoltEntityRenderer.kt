package org.teamvoided.starborn_soundscape.client.renderer
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.EntityRenderer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier
import org.joml.Matrix4f
import org.teamvoided.starborn_soundscape.StarbornSoundscape
import org.teamvoided.starborn_soundscape.entity.CosmicBoltEntity

class CosmicBoltEntityRenderer(context: EntityRendererFactory.Context) :
    EntityRenderer<CosmicBoltEntity>(context) {

    private val model = CosmicBoltEntityModel(
        context.getPart(StarbornModelLayers.COSMIC_BOLT)
    )

    override fun render(
        entity: CosmicBoltEntity?,
        yaw: Float,
        tickDelta: Float,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int
    ) {
        if (entity == null) return

        matrices.push()

        matrices.multiply(
            Matrix4f().rotateY(
                Math.toRadians((entity.getYaw(tickDelta) - 180f).toDouble()).toFloat()
            )
        )
        matrices.multiply(
            Matrix4f().rotateX(
                Math.toRadians((entity.getPitch(tickDelta) - 90).toDouble()).toFloat()
            )
        )

        matrices.translate(0.0, -1.4, 0.0)

        model.method_2828(
            matrices,
            vertexConsumers.getBuffer(model.getLayer(getTexture(entity))),
            light,
            OverlayTexture.DEFAULT_UV,
            -1
        )


        matrices.pop()

        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light)
    }

    override fun getTexture(entity: CosmicBoltEntity?): Identifier = TEXTURE

    companion object {
        private val TEXTURE = Identifier.of(
            StarbornSoundscape.MODID,
            "textures/entity/projectile/cosmic_bolt.png"
        )
    }
}
