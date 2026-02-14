package org.teamvoided.starborn_soundscape.init

import net.minecraft.item.Item
import net.minecraft.item.ToolMaterials
import net.minecraft.registry.Holder
import net.minecraft.registry.HolderLookup.RegistryLookup
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Rarity
import org.teamvoided.starborn_soundscape.StarbornSoundscape
import org.teamvoided.starborn_soundscape.StarbornSoundscape.id
import org.teamvoided.starborn_soundscape.item.AxeBassItem
import org.teamvoided.starborn_soundscape.item.BigSpeakerTestItem
import org.teamvoided.starborn_soundscape.item.TesterItem
import org.teamvoided.starborn_soundscape.item.overarchieverItem
import org.teamvoided.starborn_soundscape.item.songs.BreakingTheCoreSongItem
import org.teamvoided.starborn_soundscape.item.songs.FoundDeadSongItem
import java.util.stream.Stream

@Suppress("unused")
object StarbornSoundscapeItems {
        fun init() = Unit

    val OVERARCHIEVER = register("overarchiever", overarchieverItem(Item.Settings().fireproof().rarity(Rarity.EPIC).maxCount(1)))
//    val TESTITEM = register("testitem", TesterItem(Item.Settings().fireproof().rarity(Rarity.EPIC).maxCount(1)))
//    val BIGSPEAKERITEM = register("bigspeakeritem",
//        BigSpeakerTestItem(Item.Settings().fireproof().rarity(Rarity.EPIC).maxCount(1))
//    )
    val THE_AX = register(
        "the_ax",
        AxeBassItem(
            (Item.Settings()).fireproof().rarity(Rarity.EPIC)
                .attributeModifiersComponent(AxeBassItem.createAttributes(ToolMaterials.NETHERITE, 4, -2.4F))
        )
    )
    val FOUND_DEAD_FT_DEF4N = register("found_dead_ft._deaf4n", FoundDeadSongItem(Item.Settings().fireproof().rarity(Rarity.EPIC).maxCount(1)))
    val BREAKING_THE_CORE_FT_LOOK0UT = register("breaking_the_core_ft._look0ut", BreakingTheCoreSongItem(Item.Settings().fireproof().rarity(Rarity.EPIC).maxCount(1)))


    fun items(): Set<Item> {
        return Registries.ITEM.holders().astItems()
    }

    fun items(lookup: RegistryLookup<Item>): Set<Item> {
        return lookup.holders().astItems()
    }

    private fun Stream<Holder.Reference<Item>>.astItems(): Set<Item> {
        return this.filter { it.registryKey.value.namespace == StarbornSoundscape.MODID }
            .toList()
            .distinctBy { it.registryKey.value }
            .map { it.value() }
            .toSet()
    }

    fun <T : Item> register(id: String, item: T): T {
        return Registry.register(Registries.ITEM, id(id), item)
    }
}