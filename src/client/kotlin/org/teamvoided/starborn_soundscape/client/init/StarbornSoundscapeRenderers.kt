package org.teamvoided.starborn_soundscape.client.init

import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry
import net.minecraft.client.render.entity.EmptyEntityRenderer
import net.minecraft.client.render.entity.FlyingItemEntityRenderer
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeEntities

object StarbornSoundscapeRenderers {
    fun init() {
        EntityRendererRegistry.register(StarbornSoundscapeEntities.COSMIC_BOLT, ::EmptyEntityRenderer)
    }
}