package org.teamvoided.starborn_soundscape.data.gen.provider

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider.TranslationBuilder
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.block.Block
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.item.Item
import net.minecraft.registry.Holder
import net.minecraft.registry.HolderLookup
import net.minecraft.registry.Registries
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.Identifier
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeItems
import java.util.concurrent.CompletableFuture

class StarbornSoundscapeEnTranslationProvider(o: FabricDataOutput, r: CompletableFuture<HolderLookup.Provider>) :
    FabricLanguageProvider(o, r) {
    override fun generateTranslations(lookup: HolderLookup.Provider, gen: TranslationBuilder) {
        StarbornSoundscapeItems.items(lookup.getLookupOrThrow(RegistryKeys.ITEM))
            .distinctBy { it.translationKey }
            .forEach { gen.add(it, genLang(it.id)) }

        gen.add("death.attack.small_soundwaves", "%s couldn't handle %s's vibes")
        gen.add("death.attack.bolt_direct", "%s was pierced straight through by %s")
        gen.add("death.attack.bolt_explosion", "%s was pierced by millions of %s's shrapnel")
        gen.add("death.attack.big_soundwaves", "%s was violently shown %s's music taste")
        gen.add("death.attack.crushed", "%s was squished by %s's big ass speaker")

        gen.add("death.attack.small_soundwaves.item", "%s got blasted by %s's %s")
        gen.add("death.attack.bolt_direct.item", "%s was pierced straight through by %s's %s")
        gen.add("death.attack.bolt_explosion.item", "%s was pierced by millions of %s's %s's shrapnel")
        gen.add("death.attack.big_soundwaves.item", "%s was silenced by %s's %s's deafening roar")
        gen.add("death.attack.crushed.item", "%s was squished by %s's %s's unreasonably large speaker")

        gen.add("enchantment.starborn_soundscape.tri_this", "Tri This!")
        gen.add(
            "enchantment.starborn_soundscape.tri_this.desc",
            "Increases the number of bolts fired to 3, slightly reduces each bolts damage, and increases the charge time. Bolts spread is reduced the longer the weapon is charged."
        )
        gen.add("enchantment.starborn_soundscape.well_well_well", "Well Well Well...")
        gen.add(
            "enchantment.starborn_soundscape.well_well_well.desc",
            "Increases the number of bolts fired to 5, reduces the damage of each bolt, and increases the charge time heavily. Bolt spread is increased the longer the weapon is charged."
        )
        gen.add("enchantment.starborn_soundscape.grizzly_fate", "A Grizzly Fate")
        gen.add(
            "enchantment.starborn_soundscape.grizzly_fate.desc",
            "Increases the number of bolts to 18 and gives them a random spread. Bolts do almost no direct damage but keep decent indirect damage. Increases the charge time severely. Spread is reduced the longer the weapon is charged."
        )
        gen.add("enchantment.starborn_soundscape.tracer", "Tracer Round")
        gen.add(
            "enchantment.starborn_soundscape.tracer.desc",
            "The bolt fired is a tracer round, causing targets hit to glow."
        )

        // oh god think of the descripdren!
        gen.add("tooltip.soundscape.requires_song.tooltip",
            "Insert a song and get your groove on!")
        gen.add("tooltip.soundscape.song.tooltip",
            "Song inserted:")
        gen.add("tooltip.soundscape.song_can_be_inserted.tooltip",
            "Insert this song into a weapon and get schmovin!")
        gen.add("tooltip.soundscape.nullSong.tooltip",
            "Someone forgot to give this song a description, guess you'll have to find out what it does")
        gen.add("tooltip.soundscape.foundDeadDesc.tooltip",
            " - Summons a giant speaker to slam onto your foes! Let it sit and it starts blasting music louder then you thought could be handled! And you'd be right about that...")
        gen.add("tooltip.soundscape.breakingCoreDesc.tooltip",
            " - Summons a wave of 6 speakers that will target players and deal constant damage to them.")
        gen.add("tooltip.soundscape.skihi.tooltip",
            " - Launches you up in the air and lets you hover until you play the song again, or until a short period is over.")
        gen.add("tooltip.soundscape.keepup.tooltip",
            " - Launches you in the direction you are facing.")


        gen.add("effect.starborn_soundscape.hover",
            "Hover")

        gen.add("sounds.starborn_soundscape.metronome_1",
            "Metronome ticks")
        gen.add("sounds.starborn_soundscape.metronome_2",
            "Metronome ticks")

        gen.add("sounds.starborn_soundscape.sound_so_loud_it_kills_ya",
            "Loud speaker plays like, really loudly")
        gen.add("sounds.starborn_soundscape.raw_deadly_sound",
            "Small speaker plays really loud sound")
        gen.add("sounds.starborn_soundscape.small_speaker_startup",
            "Small speakers get ready")

        gen.add("sounds.starborn_soundscape.speaker_starup",
            "Really epic electric guitar chord")

        gen.add("sounds.starborn_soundscape.you_really_got_me",
            "You really got me riff plays")
    }


    private fun genLang(identifier: Identifier): String = identifier.path.titleCase()

    private fun String.titleCase(del: String = "_"): String {
        return split(del).joinToString(" ") { it.replaceFirstChar(Char::uppercaseChar) }
    }

    val Item.id get() = Registries.ITEM.getId(this)
    //val Block.id get() = Registries.BLOCK.getId(this)
    //val StatusEffect.id get() = Registries.STATUS_EFFECT.getId(this)

    companion object {
        private val keybinds = mutableMapOf<Identifier, String>()

        fun registerKeybindForDataGen(id: Identifier, name: String) {
            if (FabricLoader.getInstance().isDevelopmentEnvironment) keybinds.putIfAbsent(id, name)
        }


        // helpers
        fun TranslationBuilder.effect(effect: Holder<StatusEffect>, name: String) =
            this.add(effect.value().translationKey, name)
    }
}