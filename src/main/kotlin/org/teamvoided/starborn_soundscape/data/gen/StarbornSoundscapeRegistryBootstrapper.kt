package org.teamvoided.starborn_soundscape.data.gen

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider
import net.minecraft.registry.HolderLookup
import java.util.concurrent.CompletableFuture

class StarbornSoundscapeRegistryBootstrapper(
    output: FabricDataOutput, registriesFuture: CompletableFuture<HolderLookup.Provider>
) : FabricDynamicRegistryProvider(output, registriesFuture) {
    override fun getName() = "starborn_soundscape::generic_registry_provider"

    override fun configure(registries: HolderLookup.Provider, entries: Entries) {
        StarbornSoundscapeData.registriesToGenerate.forEach { key ->
            entries.addAll(registries.getLookupOrThrow(key))
        }
    }
}