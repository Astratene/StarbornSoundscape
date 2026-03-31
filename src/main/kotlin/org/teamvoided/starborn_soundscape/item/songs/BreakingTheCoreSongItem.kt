package org.teamvoided.starborn_soundscape.item.songs

import net.minecraft.entity.LivingEntity
import net.minecraft.item.Item.Settings
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import org.teamvoided.starborn_soundscape.entity.SmallSpeakerEntity
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeSounds
import org.teamvoided.starborn_soundscape.item.song_selection.SongItem

class BreakingTheCoreSongItem(settings: Settings) : SongItem(settings) {

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
        val positions =
            mutableListOf(Vec3d(1.0, 2.5, -0.5),
                Vec3d(-1.0, 2.5, -0.5),
                Vec3d(1.25, 1.5, -0.5),
                Vec3d(-1.25, 1.5, -0.5),
                Vec3d(1.0, 0.5, -0.5),
                Vec3d(-1.0, 0.5, -0.5))
        repeat(6) {
            positions.add(Vec3d(world.random.nextDouble().plus(-0.5).times(10), world.random.nextDouble().times(3), -1.0))
            val speaker = SmallSpeakerEntity(world, user)
            speaker.relativeVec = positions[it]
            speaker.ticksTillTrackTarget = ((it.floorDiv(2)) * 10) + 20
            speaker.lifetimeTicks = 200 - ((it.floorDiv(2)) * 10)
            speaker.postStopTicks = ((it.floorDiv(2)) * 10) + 20
            speaker.setPosition(user.pos)
            speaker.pitch2 = 1.25f + ((it.floorDiv(2)) * 0.1f)
            if (it < 3){
                speaker.shouldPlayStartup = true
            }
            world.spawnEntity(speaker)
        }
    }

    override fun getOverArchIeverChargeUp(user: LivingEntity): Int {
        return 30
    }
    override fun getBanjoChargeReduction(user: LivingEntity): Int {
        return 60
    }

    override fun givesBanjoCooldown(): Boolean {
        return true
    }
    override fun getBanjoCooldown(): Int {
        return 2400
    }

    override fun givesMetronomeCooldown(): Boolean {
        return true
    }

    override fun getMetronomeCooldown(): Int {
        return 1800
    }

    override fun getNameColor(): Formatting {
        return Formatting.RED
    }

    override fun getBarColor(): java.awt.Color {
        return java.awt.Color.RED
    }

    override fun addDescription(tooltip: MutableList<Text?>) {
        tooltip.add(
            Text.translatable("tooltip.soundscape.breakingTheCore.tooltip")
                .formatted(Formatting.GRAY)
        )
    }

}