package org.teamvoided.starborn_soundscape.client

import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry
import org.teamvoided.starborn_soundscape.StarbornSoundscape
import org.teamvoided.starborn_soundscape.client.init.StarbornSoundscapeRenderers
import org.teamvoided.starborn_soundscape.client.renderer.CosmicBoltEntityRenderer
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeEntities

@Suppress("unused")
object StarbornSoundscapeClient {
    fun init() {
        StarbornSoundscape.log.info("Hello from Client")
        StarbornSoundscapeRenderers.init()
    }
}