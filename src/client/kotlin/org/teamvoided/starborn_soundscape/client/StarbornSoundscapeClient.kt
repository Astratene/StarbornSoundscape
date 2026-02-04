package org.teamvoided.starborn_soundscape.client

import org.teamvoided.starborn_soundscape.StarbornSoundscape
import org.teamvoided.starborn_soundscape.client.init.StarbornSoundscapeRenderers

@Suppress("unused")
object StarbornSoundscapeClient {
    fun init() {
        StarbornSoundscape.log.info("Hello from Client")
        StarbornSoundscapeRenderers.init()
    }
}