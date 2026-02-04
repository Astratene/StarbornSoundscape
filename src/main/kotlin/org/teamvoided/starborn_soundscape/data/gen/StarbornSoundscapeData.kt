package org.teamvoided.starborn_soundscape.data.gen

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistrySetBuilder
import org.teamvoided.starborn_soundscape.StarbornSoundscape.log
import org.teamvoided.starborn_soundscape.data.registry.RegistryBootstrapper
import org.teamvoided.starborn_soundscape.data.gen.StarbornSoundscapeRegistryBootstrapper
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeDamageTypes

@Suppress("unused")
object StarbornSoundscapeData : DataGeneratorEntrypoint {
    internal val registriesToGenerate = mutableListOf<RegistryKey<out Registry<*>>>()

    override fun onInitializeDataGenerator(gen: FabricDataGenerator) {
        log.info("Hello from DataGen")
        val pack = gen.createPack()

//        pack.addProvider(::TemplateWorldGenerator)

        // Data
        pack.addProvider(::StarbornSoundscapeRegistryBootstrapper)
    }

    override fun buildRegistry(gen: RegistrySetBuilder) {
        println("pain")
        gen.bootstrapRegistry(StarbornSoundscapeDamageTypes)
    }

    internal fun <T> RegistrySetBuilder.bootstrapRegistry(bootstrapper: RegistryBootstrapper<T>) {
        add(bootstrapper.registryKey, bootstrapper::bootstrap)
        registriesToGenerate.add(bootstrapper.registryKey)
    }
}
