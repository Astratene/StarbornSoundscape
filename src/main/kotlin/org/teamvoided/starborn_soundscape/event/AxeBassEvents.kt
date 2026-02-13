package org.teamvoided.starborn_soundscape.event

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.fabricmc.fabric.api.event.player.AttackEntityCallback
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import org.teamvoided.starborn_soundscape.item.tracker.AxeBassTracker
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeSounds
import org.teamvoided.starborn_soundscape.item.AxeBassItem
import org.teamvoided.starborn_soundscape.util.PlayerAxeMeter

object AxeBassEvents {

    private const val BEAT_INTERVAL = 18
    private const val BEAT_WINDOW = 2

    fun init() {
        ServerTickEvents.END_SERVER_TICK.register { server ->
            val tick = server.overworld.time

            for (player in server.playerManager.playerList) {
                val mainHandStack = player.mainHandStack
                if (mainHandStack.item !is AxeBassItem) continue
                val data = AxeBassTracker.get(player)

                if (tick - data.lastBeatTick >= BEAT_INTERVAL) {
                    data.lastBeatTick = tick
                    data.beatIndex++

                    val sound =
                        if (data.beatIndex % 4 == 0)
                            StarbornSoundscapeSounds.METRONOME_1
                        else
                            StarbornSoundscapeSounds.METRONOME_2

                    player.world.playSound(
                        null,
                        player.blockPos,
                        sound,
                        SoundCategory.PLAYERS,
                        0.9f,
                        1.0f
                    )
                }
            }
        }

        AttackEntityCallback.EVENT.register { player, world, hand, target, _ ->
            if (world.isClient) return@register ActionResult.PASS
            if (hand != Hand.MAIN_HAND) return@register ActionResult.PASS
            if (target !is LivingEntity) return@register ActionResult.PASS

            val stack = player.mainHandStack
            if (stack.item !is AxeBassItem) return@register ActionResult.PASS

            val tracker = AxeBassTracker.get(player)
            val tick = world.time

            if (tracker.isOnBeat(tick, BEAT_WINDOW)) {
                val damage = player.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE).toFloat()
                PlayerAxeMeter.get(player).add(damage * 0.5f)
            }

            ActionResult.PASS
        }
    }
}
