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
    val SOUND_SO_LOUD_IT_KILLS_YA = register("sound_so_loud_it_kills_ya")
    val RAW_DEADLY_SOUND = register("raw_deadly_sound")
    val SMALL_SPEAKER_STARTUP = register("small_speaker_startup")
    val SPEAKER_STARTUP = register("speaker_startup")
    val YOU_REALLY_GOT_ME = register("you_really_got_me")
    val HIT_BANJO = register("hit_banjo")

    private fun register(id: String): SoundEvent = register(id(id))
    private fun register(id: Identifier): SoundEvent = register(id, id)
    private fun register(id: Identifier, soundId: Identifier): SoundEvent {
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(soundId))
    }
}