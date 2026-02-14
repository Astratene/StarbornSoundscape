package org.teamvoided.starborn_soundscape.init

import com.mojang.serialization.Codec
import net.minecraft.component.DataComponentType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import org.teamvoided.starborn_soundscape.StarbornSoundscape.id
import org.teamvoided.starborn_soundscape.components.OverarchieverData
import org.teamvoided.starborn_soundscape.components.SimpleStorageComponent

object StarbornSoundscapeDataComponents {
    fun init() = Unit

    val OVERARCHIEVER_DATA = registerSimple("overarchiever_data", OverarchieverData.CODEC)

    fun <T : SimpleStorageComponent> registerSimple(name: String, codec: Codec<T>): DataComponentType<T> =
        Registry.register(Registries.DATA_COMPONENT_TYPE, id(name), DataComponentType.builder<T>().codec(codec).build())

    fun <T> register(
        name: String, build: (DataComponentType.Builder<T>) -> DataComponentType<T>
    ): DataComponentType<T> =
        Registry.register(Registries.DATA_COMPONENT_TYPE, id(name), build(DataComponentType.builder()))
}