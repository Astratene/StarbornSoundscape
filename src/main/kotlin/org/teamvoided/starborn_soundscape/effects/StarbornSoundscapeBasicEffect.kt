package org.teamvoided.starborn_soundscape.effects

import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectType

open class StarbornSoundscapeBasicEffect  : StatusEffect {


    constructor(type: StatusEffectType, color: Int) : super(type, color) {
    }
}