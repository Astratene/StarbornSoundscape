package org.teamvoided.starborn_soundscape.init

import net.minecraft.component.type.AttributeModifiersComponent
import net.minecraft.item.Item
import net.minecraft.item.MiningToolItem
import net.minecraft.item.SwordItem
import net.minecraft.item.ToolMaterials
import net.minecraft.registry.Holder
import net.minecraft.registry.HolderLookup.RegistryLookup
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Rarity
import org.teamvoided.starborn_soundscape.StarbornSoundscape
import org.teamvoided.starborn_soundscape.StarbornSoundscape.id
import org.teamvoided.starborn_soundscape.item.AxeBassItem
import org.teamvoided.starborn_soundscape.item.BandStampItem
import org.teamvoided.starborn_soundscape.item.BanjolectricItem
import org.teamvoided.starborn_soundscape.item.BigSpeakerTestItem
import org.teamvoided.starborn_soundscape.item.TesterItem
import org.teamvoided.starborn_soundscape.item.astra_only_no_snooping.UniverseEdgeItem
import org.teamvoided.starborn_soundscape.item.overarchieverItem
import org.teamvoided.starborn_soundscape.item.songs.AllEyesSongItem
import org.teamvoided.starborn_soundscape.item.songs.BreakRightThroughSongItem
import org.teamvoided.starborn_soundscape.item.songs.BreakingTheCoreSongItem
import org.teamvoided.starborn_soundscape.item.songs.BurningAndBlazeSongItem
import org.teamvoided.starborn_soundscape.item.songs.FoundDeadSongItem
import org.teamvoided.starborn_soundscape.item.songs.InMyElementSongItem
import org.teamvoided.starborn_soundscape.item.songs.KeepUpSongItem
import org.teamvoided.starborn_soundscape.item.songs.SentYouReelingSongItem
import org.teamvoided.starborn_soundscape.item.songs.SkiHiSongItem
import java.util.stream.Stream

@Suppress("unused")
object StarbornSoundscapeItems {
    fun init() = Unit

    val OVERARCHIEVER =
        register(
            "overarchiever", overarchieverItem(
                Item.Settings().fireproof().rarity(Rarity.EPIC).maxCount(1)

            )
        )

    //    val TESTITEM = register("testitem", TesterItem(Item.Settings().fireproof().rarity(Rarity.EPIC).maxCount(1)))
//    val BIGSPEAKERITEM = register("bigspeakeritem",
//        BigSpeakerTestItem(Item.Settings().fireproof().rarity(Rarity.EPIC).maxCount(1))
//    )
    val METRONOME = register(
        "metronome",
        AxeBassItem(
            (Item.Settings()).fireproof().rarity(Rarity.EPIC)
                .attributeModifiersComponent(AxeBassItem.createAttributes(ToolMaterials.NETHERITE, 4, -2.4F))
        )
    )
    val EDGE_OF_THE_UNIVERSE = register(
        "edge_of_the_universe",
        UniverseEdgeItem(
            (Item.Settings()).fireproof().rarity(Rarity.EPIC)
                .attributeModifiersComponent(AxeBassItem.createAttributes(ToolMaterials.NETHERITE, 5, -3.1F))
        )
    )

    val BANJOLECTRIC = register(
        "banjolectric",
        BanjolectricItem((Item.Settings()).fireproof().rarity(Rarity.EPIC)
            .attributeModifiersComponent(AxeBassItem.createAttributes(ToolMaterials.NETHERITE, 5, -3F)))
    )

    val FOUND_DEAD_FT_DEF4N = register(
        "found_dead_ft._deaf4n",
        FoundDeadSongItem(Item.Settings().fireproof().rarity(Rarity.EPIC).maxCount(1))
    )
    val BREAKING_THE_CORE_FT_LOOK0UT = register(
        "breaking_the_core_ft._look0ut",
        BreakingTheCoreSongItem(Item.Settings().fireproof().rarity(Rarity.EPIC).maxCount(1))
    )
    val GOING_UP_IN_THE_WORLD_FT_SKI_HI = register(
        "going_up_in_the_world_ft._ski_hi",
        SkiHiSongItem(Item.Settings().fireproof().rarity(Rarity.EPIC).maxCount(1))
    )
    val KEEP_UP_FT_STEP2IT =
        register("keep_up_ft._step2it", KeepUpSongItem(Item.Settings().fireproof().rarity(Rarity.EPIC).maxCount(1)))
    val THROUGH_THE_BURNING_AND_THE_BLAZE = register(
        "through_the_burning_and_the_blaze",
        BurningAndBlazeSongItem(Item.Settings().fireproof().rarity(Rarity.EPIC).maxCount(1))
    )
    val CANT_TAKE_MY_EYES_OFF_YOU = register(
        "cant_take_my_eyes_off_you",
        AllEyesSongItem(Item.Settings().fireproof().rarity(Rarity.EPIC).maxCount(1))
    )
    val BREAK_RIGHT_THROUGH = register(
        "break_right_through",
        BreakRightThroughSongItem(Item.Settings().fireproof().rarity(Rarity.EPIC).maxCount(1))
    )
    val IN_MY_ELEMENT =
        register("in_my_element", InMyElementSongItem(Item.Settings().fireproof().rarity(Rarity.EPIC).maxCount(1)))

    val SENT_YOU_REELING =
        register("sent_you_reeling", SentYouReelingSongItem(Item.Settings().fireproof().rarity(Rarity.EPIC).maxCount(1)))

    val BAND_STAMP = register("band_stamp", BandStampItem(Item.Settings().fireproof().rarity(Rarity.EPIC).maxCount(1)))

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