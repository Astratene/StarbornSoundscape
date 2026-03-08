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
            .add(StarbornSoundscapeItems.FOUND_DEAD_FT_DEF4N)
            .add(StarbornSoundscapeItems.BREAKING_THE_CORE_FT_LOOK0UT)
            .add(StarbornSoundscapeItems.GOING_UP_IN_THE_WORLD_FT_SKI_HI)
            .add(StarbornSoundscapeItems.KEEP_UP_FT_STEP2IT)
            .add(StarbornSoundscapeItems.THROUGH_THE_BURNING_AND_THE_BLAZE)
            .add(StarbornSoundscapeItems.CANT_TAKE_MY_EYES_OFF_YOU)
            .add(StarbornSoundscapeItems.BREAK_RIGHT_THROUGH)
            .add(StarbornSoundscapeItems.IN_MY_ELEMENT)
            .add(StarbornSoundscapeItems.SENT_YOU_REELING)
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
        getOrCreateTagBuilder(ItemTags.SWORD_ENCHANTABLE).add(StarbornSoundscapeItems.BANJOLECTRIC)
    }
}