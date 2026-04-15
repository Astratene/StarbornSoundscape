package org.teamvoided.starborn_soundscape.data

import net.minecraft.enchantment.Enchantment
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import org.teamvoided.starborn_soundscape.StarbornSoundscape

object StarbornSoundscapeEnchantments {
    val ENCHANTMENTS = mutableSetOf<RegistryKey<Enchantment>>()
    val TRI_THIS = create("tri_this")
    val WELL_WELL_WELL = create("well_well_well")
    val GRIZZLY_FATE = create("grizzly_fate")
    //val TRACER = create("tracer")
    val BOLT_RAIN = create("bolt_rain")

    private fun create(id: String): RegistryKey<Enchantment> {
        val enchantment = RegistryKey.of(RegistryKeys.ENCHANTMENT, StarbornSoundscape.id(id))
        ENCHANTMENTS.add(enchantment)
        return enchantment
    }
}