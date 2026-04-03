package org.teamvoided.starborn_soundscape.init

import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectType
import net.minecraft.registry.Holder
import net.minecraft.registry.Registries
import org.teamvoided.starborn_soundscape.StarbornSoundscape.id
import org.teamvoided.starborn_soundscape.effects.StarbornSoundscapeBasicEffect
import org.teamvoided.starborn_soundscape.effects.ToxicEffect
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

    val SHRED_OF_TOXICITY = register(
        "shred_of_toxicity", StarbornSoundscapeBasicEffect(StatusEffectType.BENEFICIAL, 0x347a37)
            .addAttributeModifier(
                EntityAttributes.GENERIC_MOVEMENT_SPEED, id("effect.shred_of_toxicity"),
                -0.0005, EntityAttributeModifier.Operation.ADD_VALUE
            )
    )

    val DEEP_TOXICITY = register(
        "deep_toxicity", StarbornSoundscapeBasicEffect(StatusEffectType.BENEFICIAL, 0x347a37)
            .addAttributeModifier(
                EntityAttributes.GENERIC_ATTACK_SPEED, id("effect.deep_toxicity"),
                -0.005, EntityAttributeModifier.Operation.ADD_VALUE
            )
    )

    val SOUNDSICK = register("soundsick", ToxicEffect(0x347a37))

    val CORROSION = register(
        "corrosion", StarbornSoundscapeBasicEffect(StatusEffectType.BENEFICIAL, 0x347a37)
            .addAttributeModifier(
                EntityAttributes.GENERIC_ARMOR, id("effect.corrosion"),
                -2.0, EntityAttributeModifier.Operation.ADD_VALUE
            )
    )

    private fun register(id: String, entry: StatusEffect): Holder<StatusEffect> =
        Registries.STATUS_EFFECT.registerHolder(id(id), entry)
}