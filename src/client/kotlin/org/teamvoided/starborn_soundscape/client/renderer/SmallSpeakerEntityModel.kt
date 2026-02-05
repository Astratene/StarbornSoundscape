package org.teamvoided.starborn_soundscape.client.renderer

import net.minecraft.util.Identifier
import software.bernie.geckolib.model.GeoModel
import org.teamvoided.starborn_soundscape.entity.SmallSpeakerEntity

class SmallSpeakerEntityModel : GeoModel<SmallSpeakerEntity>() {

    override fun getModelResource(animatable: SmallSpeakerEntity): Identifier {
        return Identifier.of(
            "starborn_soundscape",
            "geo/small_speaker.geo.json"
        )
    }

    override fun getTextureResource(animatable: SmallSpeakerEntity): Identifier {
        return Identifier.of(
            "starborn_soundscape",
            "textures/entity/small_speaker.png"
        )
    }

    override fun getAnimationResource(animatable: SmallSpeakerEntity): Identifier {
        return Identifier.of(
            "starborn_soundscape",
            "animations/small_speaker.animation.json"
        )
    }
}
