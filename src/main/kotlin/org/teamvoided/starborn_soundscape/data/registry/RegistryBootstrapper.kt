package org.teamvoided.starborn_soundscape.data.registry

import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider.Entries
import net.minecraft.registry.BootstrapContext
import net.minecraft.registry.DynamicRegistryManager
import net.minecraft.registry.Holder
import net.minecraft.registry.HolderLookup
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.util.Identifier
import org.teamvoided.starborn_soundscape.StarbornSoundscape.id
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.forEach
import kotlin.jvm.optionals.getOrNull

abstract class RegistryBootstrapper<V>(val registryKey: RegistryKey<Registry<V>>) {
    protected val toRegister: MutableMap<RegistryKey<V>, Bootstrapper<V>> = mutableMapOf()

    protected fun register(name: String, bootstrapper: Bootstrapper<V>): RegistryKey<V> {
        val key = registryKey(id(name))
        toRegister[key] = bootstrapper
        return key
    }

    protected fun registryKey(id: Identifier) = RegistryKey.of(registryKey, id)
    protected fun mcRegistryKey(path: String) = registryKey(Identifier.of("minecraft", path))

    open fun bootstrap(ctx: BootstrapContext<V>) {
        toRegister.forEach { (key, value) ->
            ctx.register(key, value.bootstrap(ctx))
        }

        toRegister.clear()
    }

    fun getHolder(manager: DynamicRegistryManager, key: RegistryKey<V>): Holder<V>? {
        return manager.get(registryKey).getHolder(key).getOrNull()
    }

    fun interface Bootstrapper<V> {
        fun bootstrap(ctx: BootstrapContext<V>): V
    }
}