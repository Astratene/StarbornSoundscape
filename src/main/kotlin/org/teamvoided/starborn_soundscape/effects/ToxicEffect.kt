package org.teamvoided.starborn_soundscape.effects

import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffectType
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeDamageTypes
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeDamageTypes.customDamage

class ToxicEffect(color: Int) : StarbornSoundscapeBasicEffect(StatusEffectType.HARMFUL, color) {
    override fun shouldApplyUpdateEffect(tick: Int, amplifier: Int): Boolean {
        return true
    }

    override fun applyUpdateEffect(entity: LivingEntity?, amplifier: Int): Boolean {
        if (entity != null && entity.world != null && (entity.world.time % 20 == 0L)) {
            entity.customDamage(StarbornSoundscapeDamageTypes.TOXICED, 1.0f)
        }
        return true
    }
}