package org.teamvoided.starborn_soundscape.init

import com.mojang.serialization.Codec
import net.minecraft.component.DataComponentType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import org.teamvoided.starborn_soundscape.StarbornSoundscape.id
import org.teamvoided.starborn_soundscape.components.BanjolectricData
import org.teamvoided.starborn_soundscape.components.CurrentUseTime
import org.teamvoided.starborn_soundscape.components.MetronomeChargeData
import org.teamvoided.starborn_soundscape.components.OverarchieverData
import org.teamvoided.starborn_soundscape.components.OverarchieverDatav2
import org.teamvoided.starborn_soundscape.components.SimpleStorageComponent

object StarbornSoundscapeDataComponents {
    fun init() = Unit

    val OVERARCHIEVER_DATA = registerSimple("overarchiever_data", OverarchieverData.CODEC)
    val BANJOLECTRIC_DATA = registerSimple("banjolectric_charge", BanjolectricData.CODEC)
    val OVERARCHIEVER_DATAV2 = registerSimple("overarchiever_datav2", OverarchieverDatav2.CODEC)
    val CURRENT_USE_TIME = registerSimple("current_use_time", CurrentUseTime.CODEC)
    val METRONOME_CHARGE_DATA = registerSimple("metronome_charge_data", MetronomeChargeData.CODEC)



    fun <T : SimpleStorageComponent> registerSimple(name: String, codec: Codec<T>): DataComponentType<T> =
        Registry.register(Registries.DATA_COMPONENT_TYPE, id(name), DataComponentType.builder<T>().codec(codec).build())

    fun <T> register(
        name: String, build: (DataComponentType.Builder<T>) -> DataComponentType<T>
    ): DataComponentType<T> =
        Registry.register(Registries.DATA_COMPONENT_TYPE, id(name), build(DataComponentType.builder()))
}