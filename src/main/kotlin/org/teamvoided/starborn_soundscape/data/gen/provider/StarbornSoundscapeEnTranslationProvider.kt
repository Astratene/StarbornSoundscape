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

        //dude this pisses me off im fixing it

        //status effects
        gen.add("effect.starborn_soundscape.hover",
            "Hover")
        gen.add("effect.starborn_soundscape.closed_eyes",
            "Watching the Stars!")
        gen.add("effect.starborn_soundscape.band_approved",
            "Band Approved")
        gen.add("effect.starborn_soundscape.shred_of_toxicity",
            "Shred of Toxicity")
        gen.add("effect.starborn_soundscape.deep_toxicity",
            "Deep Toxicity")
        gen.add("effect.starborn_soundscape.irradiated",
            "Irradiated")
        gen.add("effect.starborn_soundscape.corrosion",
            "corrosion")

        gen.add("effect.starborn_soundscape.hover.desc",
            "Removes a persons gravity")
        gen.add("effect.starborn_soundscape.closed_eyes.desc",
            "Blinds you, but doesn't inhibit your legs.")
        gen.add("effect.starborn_soundscape.band_approved.desc",
            "Protects you from the harm the band may hit you with")
        gen.add("effect.starborn_soundscape.shred_of_toxicity.desc",
            "Reduces movement speed")
        gen.add("effect.starborn_soundscape.deep_toxicity.desc",
            "Reduces attack speed")
        gen.add("effect.starborn_soundscape.irradiated.desc",
            "Deals damage over time")
        gen.add("effect.starborn_soundscape.corrosion.desc",
            "Reduces armor points")

        //captions
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
        gen.add("sounds.starborn_soundscape.hit_banjo",
            "Silly banjo plays")
        gen.add("sounds.starborn_soundscape.fizz",
            "Toxic cloud fizzes")

        //item grouup
        gen.add("itemgroup.starborn_soundscape.tab",
            "Starborn Soundscape")

        //death messages
        gen.add("death.attack.small_soundwaves", "%s couldn't handle %s's vibes")
        gen.add("death.attack.bolt_direct", "%s was pierced straight through by %s")
        gen.add("death.attack.bolt_explosion", "%s was pierced by millions of %s's shrapnel")
        gen.add("death.attack.big_soundwaves", "%s was violently shown %s's music taste")
        gen.add("death.attack.crushed", "%s was squished by %s's big ass speaker")
        gen.add("death.attack.starstruck", "%s watched %s's stars")
        gen.add("death.attack.shocked", "%s was shocked by %s's performance")
        gen.add("death.attack.toxiced", "%s had a meltdown")

        gen.add("death.attack.small_soundwaves.item", "%s got blasted by %s's %s")
        gen.add("death.attack.bolt_direct.item", "%s was pierced straight through by %s's %s")
        gen.add("death.attack.bolt_explosion.item", "%s was pierced by millions of %s's %s's shrapnel")
        gen.add("death.attack.big_soundwaves.item", "%s was silenced by %s's %s's deafening roar")
        gen.add("death.attack.crushed.item", "%s was squished by %s's %s's unreasonably large speaker")
        gen.add("death.attack.starstruck.item", "%s was struck by awe while watching %s's %s's stars")
        gen.add("death.attack.shocked.item", "%s was shown god by %s's %s")

        gen.add("death.attack.toxiced.player", "%s was melted to bone while fighting %s")

        //enchantments start

        //Tri This!
        gen.add("enchantment.starborn_soundscape.tri_this", "Tri This!")
        gen.add(
            "enchantment.starborn_soundscape.tri_this.desc",
            "Increases the number of bolts fired to 3, decreasing bolt spread depending on charge."
        )
        //Well Well Well...
        gen.add("enchantment.starborn_soundscape.well_well_well", "Well Well Well...")
        gen.add(
            "enchantment.starborn_soundscape.well_well_well.desc",
            "Increases the number of bolts fired to 5, increasing bolt spread depending on charge."
        )
        //A Grizzly Fate
        gen.add("enchantment.starborn_soundscape.grizzly_fate", "A Grizzly Fate")
        gen.add(
            "enchantment.starborn_soundscape.grizzly_fate.desc",
            "Increases the number of bolts to 9, decreasing bolt spread depending on charge. Much higher charge time."
        )
        //Tracer Round
        gen.add("enchantment.starborn_soundscape.tracer", "Tracer Round")
        gen.add(
            "enchantment.starborn_soundscape.tracer.desc",
            "The bolt fired is a tracer round, causing targets hit to glow."
        )
        //Bolt rain
        gen.add("enchantment.starborn_soundscape.bolt_rain", "Rain for your Sorrows")
        gen.add(
            "enchantment.starborn_soundscape.bolt_rain.desc",
            "Fires up to 18 bolts, depending on charge. Bolts gain a random spread"
        )

        //descriptions start, as astra said "// oh god think of the descripdren!"

        gen.add("tooltip.soundscape.requires_song.tooltip", // insert song tooltip
            "Insert a song and get your groove on!")
        gen.add("tooltip.soundscape.song.tooltip", //song inserted. p obvious
            "Song inserted:")
        gen.add("tooltip.soundscape.overarchiever_activate.tooltip", //tooltip for the overarchiever
            "left then right click to activate the song!")
        gen.add("tooltip.soundscape.song_can_be_inserted.tooltip", //tooltip on all songs
            "Insert this song into a weapon and get schmovin!")
        gen.add("tooltip.soundscape.stamp1.tooltip", //tooltip stamp
            "Right click to toggle your safety in the band, and hit others to make them safe or not")

        //song tooltips
        gen.add("tooltip.soundscape.nullSong.tooltip", //dummy tooltip
            "Someone forgot to give this song a description, guess you'll have to find out what it does")
        gen.add("tooltip.soundscape.foundDead.tooltip", //found dead tooltip
            " - Summons a giant speaker to slam onto your foes! Let it sit and it starts blasting music louder then you thought could be handled! And you'd be right about that...")
        gen.add("tooltip.soundscape.breakingTheCore.tooltip", //breaking the core tooltip
            " - Summons a wave of 6 speakers that will target players and deal constant damage to them.")
        gen.add("tooltip.soundscape.goingUp.tooltip", //going up in the world tooltip
            " - Launches you up in the air and lets you hover until you play the song again, or until a short period is over.")
        gen.add("tooltip.soundscape.keepUp.tooltip", //keep up! tooltip
            " - Launches you in the direction you are facing.")
        gen.add("tooltip.soundscape.burnBlaze.tooltip", //through the burning and the blaze tooltip
            " - Lights opponents on fire.")
        gen.add("tooltip.soundscape.eyes.tooltip", //cant take my eyes off you tooltip
            " - Blinds nearby opponents and makes you glow. You're the star of the show!")
        gen.add("tooltip.soundscape.breakRightThrough.tooltip", //break right through tooltip
            " - Lets weapons break right through shields!")
        gen.add("tooltip.soundscape.inMyElement.tooltip", //in my element tooltip
            " - Does different effects based on the primary element of the user")
        gen.add("tooltip.soundscape.ripMeOut.tooltip", //rip me out tooltip
            " - Grappling hook...")
        gen.add("tooltip.soundscape.sentYouReeling.tooltip", //sent you reeling through tooltip
            " - Launches you backwards and surrounding players forwards")
        gen.add("tooltip.soundscape.toxicity.tooltip", //toxicity tooltip
            " - Creates clouds of toxic mist that slow, damage, or weaken opponents armor")


        //Other tooltips
        gen.add("tooltip.soundscape.wip.tooltip", //Work in progress
            "This item is currently a work in progress and is not yet intended for gameplay.")
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