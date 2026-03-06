package org.teamvoided.starborn_soundscape.item.songs

import net.minecraft.entity.LivingEntity
import net.minecraft.item.Item
import net.minecraft.item.Item.Settings
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import org.joml.Math
import org.teamvoided.starborn_soundscape.StarbornSoundscape
import org.teamvoided.starborn_soundscape.entity.BigSpeakerEntity
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeSounds
import org.teamvoided.starborn_soundscape.item.song_selection.SongItem

class FoundDeadSongItem(settings: Settings) : SongItem(settings) {

    override fun useSong(user: LivingEntity, world: World) {
        if (!world.isClient) {
            val serverWorld = world as ServerWorld
            serverWorld.spawnParticles(
                ParticleTypes.GLOW,
                user.x,
                user.y + 1,
                user.z,
                50,
                1.0,
                2.0,
                1.0,
                0.0
            )
            if (world.random.range(0, 100) == 100) {
                world.playSound(
                    null,
                    user.pos.x,
                    user.pos.y,
                    user.pos.z,
                    StarbornSoundscapeSounds.YOU_REALLY_GOT_ME,
                    SoundCategory.PLAYERS,
                    1.0F,
                    1.0f
                )
            }
            else {
                world.playSound(
                    null,
                    user.pos.x,
                    user.pos.y,
                    user.pos.z,
                    StarbornSoundscapeSounds.SPEAKER_STARTUP,
                    SoundCategory.PLAYERS,
                    1.0F,
                    1.0f
                )
            }
        }
        val speaker = BigSpeakerEntity(world, user)
        val relativeVec = Vec3d(0.0, 3.0, 3.0).rotateY((user.yaw) * (Math.PI.toFloat() / 180) * -1)
        if (!world.isClient) {
            val serverWorld = world as ServerWorld
            serverWorld.spawnParticles(
                ParticleTypes.GLOW,
                user.x + relativeVec.x,
                user.y + 1 + relativeVec.y,
                user.z + relativeVec.z,
                100,
                1.0,
                1.5,
                1.0,
                0.0
            )
        }
        speaker.setPosition(user.pos.add(relativeVec))
        speaker.yaw = user.yaw
        world.spawnEntity(speaker)

    }

    override fun getOverArchIeverChargeUp(user: LivingEntity): Int {
        return 30
    }

    override fun getNameColor(): Formatting {
        return Formatting.DARK_GRAY
    }

    override fun getBarColor(): java.awt.Color {
        return java.awt.Color.DARK_GRAY
    }

    override fun addDescription(tooltip: MutableList<Text?>) {
        tooltip.add(
            Text.translatable("tooltip.soundscape.foundDeadDesc.tooltip")
                .formatted(Formatting.GRAY)
        )
    }
}