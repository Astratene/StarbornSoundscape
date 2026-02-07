package org.teamvoided.starborn_soundscape.util

import net.minecraft.entity.player.PlayerEntity
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class PlayerAxeMeter private constructor() {
    var value = 0f
    val max = 100f

    fun add(amount: Float) {
        value = (value + amount).coerceAtMost(max)
    }

    fun isFull() = value >= max
    fun consume() { value = 0f }

    companion object {
        private val ACTIVE = ConcurrentHashMap<UUID, PlayerAxeMeter>()

        fun get(player: PlayerEntity): PlayerAxeMeter {
            return ACTIVE.getOrPut(player.uuid) { PlayerAxeMeter() }
        }

        fun clear(player: PlayerEntity) {
            ACTIVE.remove(player.uuid)
        }
    }
}
