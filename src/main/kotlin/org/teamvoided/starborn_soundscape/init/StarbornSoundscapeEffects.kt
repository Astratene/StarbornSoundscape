package org.teamvoided.starborn_soundscape.init

import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectType
import net.minecraft.registry.Holder
import net.minecraft.registry.Registries
import org.teamvoided.starborn_soundscape.StarbornSoundscape.id
import org.teamvoided.starborn_soundscape.effects.StarbornSoundscapeBasicEffect
import org.teamvoided.starborn_soundscape.util.registerHolder

object StarbornSoundscapeEffects {
    fun init() = Unit

    val HOVER = register(
        "hover", StarbornSoundscapeBasicEffect(StatusEffectType.BENEFICIAL, 6684672)
            .addAttributeModifier(
                EntityAttributes.GENERIC_GRAVITY, id("effect.hover"),
                -0.08, EntityAttributeModifier.Operation.ADD_VALUE
            )
            .addAttributeModifier(
                EntityAttributes.GENERIC_ARMOR_TOUGHNESS, id("effect.hover"),
                -4.0, EntityAttributeModifier.Operation.ADD_VALUE
            )
    )

    val CLOSED_EYES = register(
        "closed_eyes", StarbornSoundscapeBasicEffect(StatusEffectType.HARMFUL, 6684672)
    )

    val BAND_APPROVED = register(
        "band_approved", StarbornSoundscapeBasicEffect(StatusEffectType.BENEFICIAL, 6684672)
    )

    private fun register(id: String, entry: StatusEffect): Holder<StatusEffect> =
        Registries.STATUS_EFFECT.registerHolder(id(id), entry)
}