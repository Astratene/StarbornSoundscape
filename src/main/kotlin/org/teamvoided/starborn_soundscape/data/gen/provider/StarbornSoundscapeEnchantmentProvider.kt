package org.teamvoided.starborn_soundscape.data.gen.provider

import net.minecraft.enchantment.Enchantment
import net.minecraft.entity.EquipmentSlotGroup
import net.minecraft.item.Item
import net.minecraft.registry.BootstrapContext
import net.minecraft.registry.HolderProvider
import net.minecraft.registry.Registries
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.DamageTypeTags
import net.minecraft.registry.tag.EnchantmentTags
import net.minecraft.registry.tag.ItemTags
import net.minecraft.util.Identifier
import org.teamvoided.starborn_soundscape.StarbornSoundscape.MODID
import org.teamvoided.starborn_soundscape.data.StarbornSoundscapeEnchantments
import org.teamvoided.starborn_soundscape.data.tags.StarbornSoundscapeItemTags
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeItems
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeItems.items

object StarbornSoundscapeEnchantmentProvider {

    fun bootstrap(c: BootstrapContext<Enchantment>) {
        val item: HolderProvider<Item> = c.getRegistryLookup(RegistryKeys.ITEM)

        c.register(
            StarbornSoundscapeEnchantments.TRI_THIS,
            Enchantment.builder(
                Enchantment.createProperties(
                    item.getTagOrThrow(StarbornSoundscapeItemTags.ORVERARCHIEVER_ENCHANTABLE),
                    2,
                    1,
                    Enchantment.cost(10, 20),
                    Enchantment.cost(60, 20),
                    4,
                    EquipmentSlotGroup.ANY
                )
            )
        )
        c.register(
            StarbornSoundscapeEnchantments.WELL_WELL_WELL,
            Enchantment.builder(
                Enchantment.createProperties(
                    item.getTagOrThrow(StarbornSoundscapeItemTags.ORVERARCHIEVER_ENCHANTABLE),
                    2,
                    1,
                    Enchantment.cost(10, 20),
                    Enchantment.cost(60, 20),
                    4,
                    EquipmentSlotGroup.ANY
                )
            )
        )
        c.register(
            StarbornSoundscapeEnchantments.GRIZZLY_FATE,
            Enchantment.builder(
                Enchantment.createProperties(
                    item.getTagOrThrow(StarbornSoundscapeItemTags.ORVERARCHIEVER_ENCHANTABLE),
                    2,
                    1,
                    Enchantment.cost(10, 20),
                    Enchantment.cost(60, 20),
                    4,
                    EquipmentSlotGroup.ANY
                )
            )
        )
        c.register(
            StarbornSoundscapeEnchantments.BOLT_RAIN,
            Enchantment.builder(
                Enchantment.createProperties(
                    item.getTagOrThrow(StarbornSoundscapeItemTags.ORVERARCHIEVER_ENCHANTABLE),
                    2,
                    1,
                    Enchantment.cost(10, 20),
                    Enchantment.cost(60, 20),
                    4,
                    EquipmentSlotGroup.ANY
                )
            )
        )
        c.register(
            StarbornSoundscapeEnchantments.TRACER,
            Enchantment.builder(
                Enchantment.createProperties(
                    item.getTagOrThrow(StarbornSoundscapeItemTags.ORVERARCHIEVER_ENCHANTABLE),
                    2,
                    1,
                    Enchantment.cost(10, 20),
                    Enchantment.cost(60, 20),
                    4,
                    EquipmentSlotGroup.ANY
                )
            )
        )
    }


    fun BootstrapContext<Enchantment>.register(
        registryKey: RegistryKey<Enchantment>,
        builder: Enchantment.Builder
    ) { this.register(registryKey, builder.build(registryKey.value)) }
}