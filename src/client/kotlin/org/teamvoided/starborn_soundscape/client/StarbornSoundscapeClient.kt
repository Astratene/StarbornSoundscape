package org.teamvoided.starborn_soundscape.client

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.minecraft.client.render.BackgroundRenderer
import net.minecraft.util.Identifier
import net.mokus.mokuslib.api.MokusLibClientAPI
import org.teamvoided.starborn_soundscape.StarbornSoundscape
import org.teamvoided.starborn_soundscape.client.fog.LockedEyesFogEffect
import org.teamvoided.starborn_soundscape.client.init.StarbornHudRendering
import org.teamvoided.starborn_soundscape.client.init.StarbornSoundscapeParticlesClient
import org.teamvoided.starborn_soundscape.client.init.StarbornSoundscapeRenderers
import org.teamvoided.starborn_soundscape.mixin.client.FogEffectAccessor
import org.teamvoided.starborn_soundscape.util.StarbornSoundscapeModelPredicates

@Suppress("unused")
object StarbornSoundscapeClient {
    fun init() {
        StarbornSoundscape.log.info("Hello from Client")
        StarbornSoundscapeRenderers.init()
        StarbornSoundscapeModelPredicates.init()
        StarbornHudRendering.init()
        StarbornSoundscapeParticlesClient.init()
        fogEffect()
        MokusLibClientAPI.registerItemModel(Identifier.of(StarbornSoundscape.MODID, "edge_of_the_universe_inv"))
        MokusLibClientAPI.registerItemModel(Identifier.of(StarbornSoundscape.MODID, "overarchiever_inv"))
        MokusLibClientAPI.registerItemModel(Identifier.of(StarbornSoundscape.MODID, "banjolectric_inv"))
        MokusLibClientAPI.registerItemModel(Identifier.of(StarbornSoundscape.MODID, "metronome_inv"))
    }

    fun fogEffect() {
        (FogEffectAccessor.starbornFogEffects() as ArrayList<BackgroundRenderer.FogEffect>).add(LockedEyesFogEffect())
    }
}