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
import org.teamvoided.starborn_soundscape.item.BandStampItem
import org.teamvoided.starborn_soundscape.item.BanjolectricItem
import org.teamvoided.starborn_soundscape.item.astra_only_no_snooping.UniverseEdgeItem
import org.teamvoided.starborn_soundscape.item.OverarchieverItem
import org.teamvoided.starborn_soundscape.item.TesterItem
import org.teamvoided.starborn_soundscape.item.songs.AllEyesSongItem
import org.teamvoided.starborn_soundscape.item.songs.BreakRightThroughSongItem
import org.teamvoided.starborn_soundscape.item.songs.BreakingTheCoreSongItem
import org.teamvoided.starborn_soundscape.item.songs.BurningAndBlazeSongItem
import org.teamvoided.starborn_soundscape.item.songs.FoundDeadSongItem
import org.teamvoided.starborn_soundscape.item.songs.InMyElementSongItem
import org.teamvoided.starborn_soundscape.item.songs.KeepUpSongItem
import org.teamvoided.starborn_soundscape.item.songs.SentYouReelingSongItem
import org.teamvoided.starborn_soundscape.item.songs.SkiHiSongItem
import org.teamvoided.starborn_soundscape.item.songs.ToxicitySongItem
import java.util.stream.Stream

@Suppress("unused")
object StarbornSoundscapeItems {
    fun init() = Unit

    val OVERARCHIEVER =
        register(
            "overarchiever", OverarchieverItem(
                Item.Settings().fireproof().rarity(Rarity.EPIC).maxCount(1).maxDamage(1)
            )
        )

    //val TESTITEM = register("testitem", TesterItem(Item.Settings().fireproof().rarity(Rarity.EPIC).maxCount(1)))

    val METRONOME = register(
        "metronome",
        AxeBassItem(
            (Item.Settings()).fireproof().rarity(Rarity.EPIC)
                .attributeModifiersComponent(AxeBassItem.createAttributes(ToolMaterials.NETHERITE, 3, -2.4F))
        )
    )

    val BANJOLECTRIC = register(
        "banjolectric",
        BanjolectricItem(
            (Item.Settings()).fireproof().rarity(Rarity.EPIC)
                .attributeModifiersComponent(AxeBassItem.createAttributes(ToolMaterials.NETHERITE, 5, -3F))
        )
    )

    val CONSPIRACY_AND_MURDER = register(
        "conspiracy_and_murder",
        BreakingTheCoreSongItem(Item.Settings().fireproof().maxCount(1))
    )

    val TOXIC_CITY =
        register("toxic_city", ToxicitySongItem(Item.Settings().fireproof().rarity(Rarity.EPIC).maxCount(1)))

    val IN_MY_ELEMENT =
        register("in_my_element", InMyElementSongItem(Item.Settings().fireproof().rarity(Rarity.EPIC).maxCount(1)))

    val BREAK_IN = register(
        "break_in",
        BreakRightThroughSongItem(Item.Settings().fireproof().rarity(Rarity.EPIC).maxCount(1))
    )

    val FOUND_DEAD = register(
        "found_dead",
        FoundDeadSongItem(Item.Settings().fireproof().rarity(Rarity.EPIC).maxCount(1))
    )

    val SENT_OFF_TRACK =
        register("sent_off_track", SentYouReelingSongItem(Item.Settings().fireproof().rarity(Rarity.EPIC).maxCount(1)))

    val BURNING_TESTIMONY = register(
        "burning_testimony",
        BurningAndBlazeSongItem(Item.Settings().fireproof().rarity(Rarity.EPIC).maxCount(1))
    )

    val BALCONY_SUICIDE = register(
        "balcony_suicide",
        SkiHiSongItem(Item.Settings().fireproof().rarity(Rarity.EPIC).maxCount(1))
    )

    val CLEAN_ESCAPE =
        register("clean_escape", KeepUpSongItem(Item.Settings().fireproof().rarity(Rarity.EPIC).maxCount(1)))

    val EYES_ON_THE_LIES = register(
        "eyes_on_the_lies",
        AllEyesSongItem(Item.Settings().fireproof().rarity(Rarity.EPIC).maxCount(1))
    )

    val UNPRINTED_RECORD = register(
        "unprinted_record",
        Item(Item.Settings().maxCount(1))
    )

    val DISC_WAX = register(
        "disc_wax",
        Item(Item.Settings().maxCount(64))
    )

    //val BAND_STAMP = register("band_stamp", BandStampItem(Item.Settings().fireproof().rarity(Rarity.EPIC).maxCount(1))) Kept in case i can think of ways to make it work in future

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