package org.teamvoided.starborn_soundscape

import me.fzzyhmstrs.fzzy_config.api.ConfigApi
import net.minecraft.util.Identifier
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.teamvoided.starborn_soundscape.config.StarbornSoundscapeConfig
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeDamageTypes
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeEntities
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeItems

@Suppress("unused")
object StarbornSoundscape {
    const val MODID = "starborn_soundscape"

    @JvmField
    val log: Logger = LoggerFactory.getLogger(StarbornSoundscape::class.simpleName)

    @JvmField
    var config = ConfigApi.registerAndLoadConfig(::StarbornSoundscapeConfig)

    fun init() {
        log.info("Stars coming straight to your ears!")
        StarbornSoundscapeEntities.init()
        StarbornSoundscapeItems.init()
        StarbornSoundscapeDamageTypes
    }

    fun id(namespace: String, path: String): Identifier = Identifier.of(namespace, path)
    fun mc(path: String): Identifier = Identifier.parse(path)
    fun id(path: String) = id(MODID, path)
}
