package org.teamvoided.starborn_soundscape

import me.fzzyhmstrs.fzzy_config.api.ConfigApi
import net.minecraft.resources.ResourceLocation
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.teamvoided.starborn_soundscape.config.StarbornSoundscapeConfig

@Suppress("unused")
object StarbornSoundscape {
    const val MODID = "starborn_soundscape"

    @JvmField
    val log: Logger = LoggerFactory.getLogger(StarbornSoundscape::class.simpleName)

    @JvmField
    var config = ConfigApi.registerAndLoadConfig(::StarbornSoundscapeConfig)

    fun init() {
        log.info("Stars coming straight to your ears!")
    }

    fun id(namespace: String, path: String): ResourceLocation = ResourceLocation.fromNamespaceAndPath(namespace, path)
    fun mc(path: String): ResourceLocation = ResourceLocation.withDefaultNamespace(path)
    fun id(path: String) = id(MODID, path)
}
