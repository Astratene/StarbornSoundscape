package org.teamvoided.starborn_soundscape.client.renderer

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.render.entity.EntityRendererFactory
import software.bernie.geckolib.renderer.GeoEntityRenderer
import org.teamvoided.starborn_soundscape.entity.SmallSpeakerEntity

@Environment(EnvType.CLIENT)
class SmallSpeakerEntityRenderer(
    ctx: EntityRendererFactory.Context
) : GeoEntityRenderer<SmallSpeakerEntity>(
    ctx,
    SmallSpeakerEntityModel()
) {

    init {
        this.shadowRadius = 0.25f
    }
}

