package org.teamvoided.starborn_soundscape.item.songs

import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.player.PlayerEntity
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

class KeepUpSongItem(settings: Settings) : SongItem(settings) {

    val FORWARD_BOOST = 1.0
    override fun useSong(user: LivingEntity, world: World) {
        world.playSound(
            null,
            user.x,
            user.y,
            user.z,
            SoundEvents.ENTITY_BREEZE_WIND_BURST.value(),
            SoundCategory.PLAYERS,
            1.0F,
            1.5f
        )
        if (world is ServerWorld) {
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
        if (user is PlayerEntity) {
            if (world !is ServerWorld) {
                val boo = FORWARD_BOOST
                val boost = user.rotationVector.multiply(1.0, 1.0, 1.0).normalize().multiply(boo)
                user.setVelocity(user.velocity.x + boost.x, user.velocity.y + boost.y, user.velocity.z + boost.z)
                user.velocityModified = true
            }
        }
    }

    override fun getOverarchieverChargeReduction(user: LivingEntity): Int {
        return 25000

    }

    override fun getBanjoChargeReduction(user: LivingEntity): Int {
        return 30
    }

    override fun getNameColor(): Formatting {
        return Formatting.BLUE
    }

    override fun getBarColor(): java.awt.Color {
        return java.awt.Color.BLUE
    }

    override fun addDescription(tooltip: MutableList<Text?>) {
        tooltip.add(
            Text.translatable("tooltip.soundscape.keepUp.tooltip")
                .formatted(Formatting.GRAY)
        )
    }

}