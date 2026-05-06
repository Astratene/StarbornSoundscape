package org.teamvoided.starborn_soundscape.data.gen.tags

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.Enchantments
import net.minecraft.registry.HolderLookup
import net.minecraft.registry.Registries
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.EnchantmentTags
import org.teamvoided.starborn_soundscape.data.StarbornSoundscapeEnchantments
import java.util.concurrent.CompletableFuture

class StarbornSoundscapeEnchantmentTagProvider(o: FabricDataOutput, r: CompletableFuture<HolderLookup.Provider>) :
    FabricTagProvider<Enchantment>(o, RegistryKeys.ENCHANTMENT, r) {

    override fun configure(wrapperLookup: HolderLookup.Provider?) {
        getOrCreateTagBuilder(EnchantmentTags.IN_ENCHANTING_TABLE)
            .add(StarbornSoundscapeEnchantments.TRI_THIS)
            .add(StarbornSoundscapeEnchantments.WELL_WELL_WELL)
            .add(StarbornSoundscapeEnchantments.GRIZZLY_FATE)
            .add(StarbornSoundscapeEnchantments.BOLT_RAIN)
        getOrCreateTagBuilder(EnchantmentTags.NON_TREASURE)
            .add(StarbornSoundscapeEnchantments.TRI_THIS)
            .add(StarbornSoundscapeEnchantments.WELL_WELL_WELL)
            .add(StarbornSoundscapeEnchantments.GRIZZLY_FATE)
            .add(StarbornSoundscapeEnchantments.BOLT_RAIN)
        getOrCreateTagBuilder(EnchantmentTags.BOW_EXCLUSIVE_SET)
            .add(StarbornSoundscapeEnchantments.TRI_THIS)
            .add(StarbornSoundscapeEnchantments.WELL_WELL_WELL)
            .add(StarbornSoundscapeEnchantments.GRIZZLY_FATE)
            .add(StarbornSoundscapeEnchantments.BOLT_RAIN)
    }
}