package org.teamvoided.starborn_soundscape.data.gen.tags

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags
import net.minecraft.registry.HolderLookup
import net.minecraft.registry.tag.EnchantmentTags
import net.minecraft.registry.tag.ItemTags
import org.teamvoided.starborn_soundscape.data.tags.StarbornSoundscapeItemTags
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeItems
import java.util.concurrent.CompletableFuture

class StarbornSoundscapeItemTagProvider(
    output: FabricDataOutput,
    registriesFuture: CompletableFuture<HolderLookup.Provider>
) : FabricTagProvider.ItemTagProvider(output, registriesFuture) {
    override fun configure(wrapperLookup: HolderLookup.Provider) {
        modTags()
        enchantTags()
        conventionalTags()
    }

    private fun modTags() {
        getOrCreateTagBuilder(StarbornSoundscapeItemTags.SONG_ITEMS)
            .add(StarbornSoundscapeItems.CONSPIRACY_AND_MURDER)
            .add(StarbornSoundscapeItems.TOXIC_CITY)
            .add(StarbornSoundscapeItems.IN_MY_ELEMENT)
            .add(StarbornSoundscapeItems.BREAK_IN)
            .add(StarbornSoundscapeItems.FOUND_DEAD)
            .add(StarbornSoundscapeItems.SENT_OFF_TRACK)
            .add(StarbornSoundscapeItems.BURNING_TESTIMONY)
            .add(StarbornSoundscapeItems.BALCONY_SUICIDE)
            .add(StarbornSoundscapeItems.CLEAN_ESCAPE)
            .add(StarbornSoundscapeItems.EYES_ON_THE_LIES)
    }

    private fun conventionalTags() {
        getOrCreateTagBuilder(ConventionalItemTags.ENCHANTABLES).add(StarbornSoundscapeItems.OVERARCHIEVER)
        getOrCreateTagBuilder(ConventionalItemTags.TOOLS).add(StarbornSoundscapeItems.OVERARCHIEVER)
        getOrCreateTagBuilder(ConventionalItemTags.RANGED_WEAPON_TOOLS).add(StarbornSoundscapeItems.OVERARCHIEVER)
        getOrCreateTagBuilder(ConventionalItemTags.BOW_TOOLS).add(StarbornSoundscapeItems.OVERARCHIEVER)
        getOrCreateTagBuilder(ItemTags.VANISHING_ENCHANTABLE).add(StarbornSoundscapeItems.OVERARCHIEVER)
    }

    private fun enchantTags() {
        getOrCreateTagBuilder(StarbornSoundscapeItemTags.ORVERARCHIEVER_ENCHANTABLE).add(StarbornSoundscapeItems.OVERARCHIEVER)
        getOrCreateTagBuilder(ItemTags.DURABILITY_ENCHANTABLE).add(StarbornSoundscapeItems.OVERARCHIEVER)
    }

}