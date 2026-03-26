package org.teamvoided.starborn_soundscape

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.minecraft.util.Identifier
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.teamvoided.starborn_soundscape.event.AxeBassEvents
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeDamageTypes
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeDataComponents
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeEffects
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeEntities
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeItems
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeParticles
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeSounds
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeTabs
import org.teamvoided.starborn_soundscape.item.AxeBassItem

@Suppress("unused")
object StarbornSoundscape {
    const val MODID = "starborn_soundscape"

    @JvmField
    val log: Logger = LoggerFactory.getLogger(StarbornSoundscape::class.simpleName)

    fun init() {
        log.info("Stars coming straight to your ears!")
        StarbornSoundscapeEntities.init()
        StarbornSoundscapeItems.init()
        StarbornSoundscapeDamageTypes
        StarbornSoundscapeSounds.init()
        AxeBassEvents.init()
        StarbornSoundscapeDataComponents.init()
        StarbornSoundscapeEffects.init()
        StarbornSoundscapeTabs.init()
        StarbornSoundscapeParticles.init()

        ServerTickEvents.END_SERVER_TICK.register { server ->
            for (player in server.playerManager.playerList) {
                val stack = player.mainHandStack

                if (stack.item is AxeBassItem) {
                    (stack.item as AxeBassItem).tick(player)
                }
            }
        }
    }

    fun id(namespace: String, path: String): Identifier = Identifier.of(namespace, path)
    fun mc(path: String): Identifier = Identifier.parse(path)
    @JvmStatic
    fun id(path: String) = id(MODID, path)
}
