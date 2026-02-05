package org.teamvoided.starborn_soundscape.init

import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.sound.SoundEvent
import net.minecraft.util.Identifier
import org.teamvoided.starborn_soundscape.StarbornSoundscape.id

object StarbornSoundscapeSounds {
    fun init() = Unit

    val METRONOME_1 = register("metronome_1")
    val METRONOME_2 = register("metronome_2")

    private fun register(id: String): SoundEvent = register(id(id))
    private fun register(id: Identifier): SoundEvent = register(id, id)
    private fun register(id: Identifier, soundId: Identifier): SoundEvent {
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(soundId))
    }
}