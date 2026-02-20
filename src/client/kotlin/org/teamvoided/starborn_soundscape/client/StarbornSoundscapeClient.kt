package org.teamvoided.starborn_soundscape.client

import net.minecraft.client.render.BackgroundRenderer
import org.teamvoided.starborn_soundscape.StarbornSoundscape
import org.teamvoided.starborn_soundscape.client.fog.LockedEyesFogEffect
import org.teamvoided.starborn_soundscape.client.init.StarbornSoundscapeRenderers
import org.teamvoided.starborn_soundscape.mixin.client.FogEffectAccessor
import org.teamvoided.starborn_soundscape.util.StarbornSoundscapeModelPredicates

@Suppress("unused")
object StarbornSoundscapeClient {
    fun init() {
        StarbornSoundscape.log.info("Hello from Client")
        StarbornSoundscapeRenderers.init()
        StarbornSoundscapeModelPredicates.init()
        fogEffect()
    }

    fun fogEffect() {
        (FogEffectAccessor.starbornFogEffects() as ArrayList<BackgroundRenderer.FogEffect>).add(LockedEyesFogEffect())
    }
}