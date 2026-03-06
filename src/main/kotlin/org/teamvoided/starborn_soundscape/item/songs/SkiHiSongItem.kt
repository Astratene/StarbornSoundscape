package org.teamvoided.starborn_soundscape.item.songs

import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.item.Item.Settings
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.world.World
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeEffects
import org.teamvoided.starborn_soundscape.item.song_selection.SongItem

class SkiHiSongItem(settings: Settings) : SongItem(settings) {

    override fun useSong(user: LivingEntity, world: World) {
        if (user.hasStatusEffect(StarbornSoundscapeEffects.HOVER)) {
            user.removeStatusEffect(StarbornSoundscapeEffects.HOVER)
            return
        } else {
            world.playSound(
                null,
                user.x,
                user.y,
                user.z,
                SoundEvents.ENTITY_BREEZE_WIND_BURST.value(),
                SoundCategory.PLAYERS,
                1.0F,
                2.0f
            )
            user.setVelocity(0.0, 0.5, 0.0)
            if (world is ServerWorld) {
                user.addStatusEffect(
                    StatusEffectInstance(
                        StarbornSoundscapeEffects.HOVER,
                        100, 0,
                        false, false, true
                    )
                )
                world.spawnParticles(
                    ParticleTypes.GLOW, user.x, user.y, user.z,
                    25,
                    0.0, 0.0, 0.0,
                    0.25)
                world.spawnParticles(
                    ParticleTypes.GLOW_SQUID_INK, user.x, user.y, user.z,
                    25,
                    0.0, 0.0, 0.0,
                    0.1)
            }
            return
        }

    }

    override fun getOverarchieverChargeReduction(user: LivingEntity): Int {
        if (user.hasStatusEffect(StarbornSoundscapeEffects.HOVER)) {
            return 0
        }
        return 50000

    }

    override fun getNameColor(): Formatting {
        return Formatting.WHITE
    }

    override fun getBarColor(): java.awt.Color {
        return java.awt.Color.WHITE
    }

    override fun addDescription(tooltip: MutableList<Text?>) {
        tooltip.add(
            Text.translatable("tooltip.soundscape.goingUp.tooltip")
                .formatted(Formatting.GRAY)
        )
    }

}