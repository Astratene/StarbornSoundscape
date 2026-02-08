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

        gen.add("death.attack.small_soundwaves.item", "%s got blasted by %s's %s")
        gen.add("death.attack.bolt_direct.item", "%s was pierced straight through by %s's %s")
        gen.add("death.attack.bolt_explosion.item", "%s was pierced by millions of %s's %s's shrapnel")
        gen.add("death.attack.big_soundwaves.item", "%s was silenced by %s's %s's deafening roar")
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