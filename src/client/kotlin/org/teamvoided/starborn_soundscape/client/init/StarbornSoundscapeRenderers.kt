package org.teamvoided.starborn_soundscape.client.init

import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry
import net.minecraft.client.render.entity.EmptyEntityRenderer
import net.minecraft.client.render.entity.EntityRenderer
import net.minecraft.client.render.entity.FlyingItemEntityRenderer
import org.teamvoided.starborn_soundscape.client.renderer.BeamRenderer
import org.teamvoided.starborn_soundscape.client.renderer.BigSpeakerEntityModel
import org.teamvoided.starborn_soundscape.client.renderer.BigSpeakerEntityRenderer
import org.teamvoided.starborn_soundscape.client.renderer.CosmicBoltEntityModel
import org.teamvoided.starborn_soundscape.client.renderer.CosmicBoltEntityRenderer
import org.teamvoided.starborn_soundscape.client.renderer.SmallSpeakerEntityRenderer
import org.teamvoided.starborn_soundscape.client.renderer.StarbornModelLayers
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeEntities

object StarbornSoundscapeRenderers {
    fun init() {
//        EntityRendererRegistry.register(StarbornSoundscapeEntities.SMALL_SPEAKER, ::SmallSpeakerEntityRenderer)
//        EntityRendererRegistry.register(StarbornSoundscapeEntities.BEAM_RENDERER, ::BeamRenderer)
        EntityRendererRegistry.register(
            StarbornSoundscapeEntities.COSMIC_BOLT
        ) { ctx ->
            CosmicBoltEntityRenderer(ctx)
        }

        EntityModelLayerRegistry.registerModelLayer(
            StarbornModelLayers.COSMIC_BOLT,
            CosmicBoltEntityModel::getTexturedModelData
        )

        EntityRendererRegistry.register(
            StarbornSoundscapeEntities.SMALL_SPEAKER,
            ::SmallSpeakerEntityRenderer)

        EntityRendererRegistry.register(
            StarbornSoundscapeEntities.BEAM_RENDERER,
            ::BeamRenderer)
        EntityRendererRegistry.register(StarbornSoundscapeEntities.BIG_SPEAKER, ::BigSpeakerEntityRenderer)
        EntityModelLayerRegistry.registerModelLayer(
            StarbornModelLayers.BIG_SPEAKER,
            BigSpeakerEntityModel::getTexturedModelData
        )
    }
}