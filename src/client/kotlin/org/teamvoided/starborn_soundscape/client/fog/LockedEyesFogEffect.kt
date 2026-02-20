package org.teamvoided.starborn_soundscape.client.fog

import net.minecraft.client.render.BackgroundRenderer
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.registry.Holder
import net.minecraft.util.math.MathHelper
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeEffects
import kotlin.math.min

class LockedEyesFogEffect: BackgroundRenderer.FogEffect {
    override fun getStatusEffect(): Holder<StatusEffect> {
        return StarbornSoundscapeEffects.CLOSED_EYES
    }

    override fun applyFogEffects(
        parameters: BackgroundRenderer.FogParameters,
        entity: LivingEntity?,
        effect: StatusEffectInstance?,
        viewDistance: Float,
        tickDelta: Float
    ) {
        val f = if (effect!!.isInfinite()) 5.0f else MathHelper.lerp(
            min(1.0f, effect.getDuration().toFloat() / 20.0f),
            viewDistance,
            5.0f
        )
        if (parameters.fogType == BackgroundRenderer.FogType.FOG_SKY) {
            parameters.fogStart = 0.0f
            parameters.fogEnd = f * 0.4f
        } else {
            parameters.fogStart = f * 0.10f
            parameters.fogEnd = f
        }

    }
}