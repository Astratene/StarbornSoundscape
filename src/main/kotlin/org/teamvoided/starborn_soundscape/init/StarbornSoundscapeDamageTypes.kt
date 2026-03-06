package org.teamvoided.starborn_soundscape.init

import net.minecraft.entity.Entity
import net.minecraft.entity.damage.DamageScalingType
import net.minecraft.entity.damage.DamageType
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import org.teamvoided.starborn_soundscape.data.registry.RegistryBootstrapper

object StarbornSoundscapeDamageTypes : RegistryBootstrapper<DamageType>(RegistryKeys.DAMAGE_TYPE)  {

    //example
    //val DAMAGE = register("damage") { DamageType("damage", DamageScalingType.NEVER, 0f) }
    val BOLT_DIRECT = register("bolt_direct") { DamageType("bolt_direct", DamageScalingType.NEVER, 0f) }
    val BOLT_EXPLOSION = register("bolt_explosion") { DamageType("bolt_explosion", DamageScalingType.NEVER, 0f) }
    val SMALL_SOUNDWAVES = register("small_soundwaves") { DamageType("small_soundwaves", DamageScalingType.NEVER, 0f) }
    val BIG_SOUNDWAVES = register("big_soundwaves") { DamageType("big_soundwaves", DamageScalingType.NEVER, 0f) }
    val CRUSHED = register("crushed") { DamageType("crushed", DamageScalingType.NEVER, 0f) }
    val STARSTRUCK = register("starstruck") { DamageType("starstruck", DamageScalingType.NEVER, 0f) }

    fun Entity.customDamage(
        type: RegistryKey<DamageType>, amount: Float, source: Entity? = null, attacker: Entity? = null
    ): Boolean = this.damage(this.damageSources.create(type, source, attacker), amount)
}