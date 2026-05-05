package org.teamvoided.starborn_soundscape.data.gen

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider.Entries
import net.minecraft.registry.HolderLookup
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.RegistrySetBuilder
import org.teamvoided.starborn_soundscape.StarbornSoundscape.MODID
import org.teamvoided.starborn_soundscape.StarbornSoundscape.log
import org.teamvoided.starborn_soundscape.data.StarbornSoundscapeEnchantments
import org.teamvoided.starborn_soundscape.data.gen.StarbornSoundscapeData.DynamicRegistryProvider
import org.teamvoided.starborn_soundscape.data.registry.RegistryBootstrapper
import org.teamvoided.starborn_soundscape.data.gen.StarbornSoundscapeRegistryBootstrapper
import org.teamvoided.starborn_soundscape.data.gen.provider.StarbornSoundscapeEnTranslationProvider
import org.teamvoided.starborn_soundscape.data.gen.provider.StarbornSoundscapeEnchantmentProvider
import org.teamvoided.starborn_soundscape.data.gen.provider.StarbornSoundscapeModelProvider
import org.teamvoided.starborn_soundscape.data.gen.provider.StarbornSoundscapeRecipeProvider
import org.teamvoided.starborn_soundscape.data.gen.tags.StarbornSoundscapeDamageTypeTagProvider
import org.teamvoided.starborn_soundscape.data.gen.tags.StarbornSoundscapeEnchantmentTagProvider
import org.teamvoided.starborn_soundscape.data.gen.tags.StarbornSoundscapeItemTagProvider
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeDamageTypes
import java.util.concurrent.CompletableFuture

@Suppress("unused")
object StarbornSoundscapeData : DataGeneratorEntrypoint {
    internal val registriesToGenerate = mutableListOf<RegistryKey<out Registry<*>>>()

    override fun onInitializeDataGenerator(gen: FabricDataGenerator) {
        log.info("Hello from DataGen")
        val pack = gen.createPack()

//        pack.addProvider(::TemplateWorldGenerator)

        pack.addProvider(::StarbornSoundscapeDamageTypeTagProvider)
        pack.addProvider(::StarbornSoundscapeModelProvider)
        pack.addProvider(::StarbornSoundscapeEnTranslationProvider)
        pack.addProvider(::StarbornSoundscapeRegistryBootstrapper)
        pack.addProvider(::StarbornSoundscapeItemTagProvider)
        pack.addProvider(::DynamicRegistryProvider)
        pack.addProvider(::StarbornSoundscapeEnchantmentTagProvider)
        pack.addProvider(::StarbornSoundscapeRecipeProvider)

    }

    override fun buildRegistry(gen: RegistrySetBuilder) {
        println("Start build registry")
        gen.bootstrapRegistry(StarbornSoundscapeDamageTypes)
        gen.add(RegistryKeys.ENCHANTMENT, StarbornSoundscapeEnchantmentProvider::bootstrap)
        println("End build registry")
    }

    internal fun <T> RegistrySetBuilder.bootstrapRegistry(bootstrapper: RegistryBootstrapper<T>) {
        add(bootstrapper.registryKey, bootstrapper::bootstrap)
        registriesToGenerate.add(bootstrapper.registryKey)
    }

    class DynamicRegistryProvider(o: FabricDataOutput, r: CompletableFuture<HolderLookup.Provider>) :
        FabricDynamicRegistryProvider(o, r) {

        override fun getName(): String = "$MODID/dyn_data"

        override fun configure(reg: HolderLookup.Provider, e: Entries) {
            e.addAll(reg.getLookupOrThrow(RegistryKeys.ENCHANTMENT))
        }
    }
}
