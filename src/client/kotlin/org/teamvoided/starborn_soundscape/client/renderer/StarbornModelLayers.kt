package org.teamvoided.starborn_soundscape.client.renderer

import net.minecraft.client.render.entity.model.EntityModelLayer
import net.minecraft.util.Identifier
import org.teamvoided.starborn_soundscape.StarbornSoundscape

object StarbornModelLayers {

    val COSMIC_BOLT = EntityModelLayer(
        Identifier.of(StarbornSoundscape.MODID, "cosmic_bolt"),
        "main"
    )

    val SMALL_SPEAKER = EntityModelLayer(
        Identifier.of(StarbornSoundscape.MODID, "small_speaker"),
        "main"
    )
}