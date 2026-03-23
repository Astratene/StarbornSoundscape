package org.teamvoided.starborn_soundscape.item.tracker

import net.minecraft.entity.player.PlayerEntity
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

object AxeBassTracker {

    data class Data(
        var beatIndex: Int = 0,
        var lastBeatTick: Long = 0L,
        var metronomeEnabled: Boolean = true
    ) {
        fun isOnBeat(currentTick: Long, window: Int): Boolean {
            val delta = currentTick - lastBeatTick
            return kotlin.math.abs(currentTick - lastBeatTick) <= window
        }
    }

    private val ACTIVE = ConcurrentHashMap<UUID, Data>()

    fun get(player: PlayerEntity): Data =
        ACTIVE.getOrPut(player.uuid) { Data() }

    fun clear(player: PlayerEntity) {
        ACTIVE.remove(player.uuid)
    }
}
