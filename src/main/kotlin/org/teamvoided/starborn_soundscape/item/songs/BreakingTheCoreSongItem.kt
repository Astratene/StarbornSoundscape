package org.teamvoided.starborn_soundscape.item.songs

import net.minecraft.entity.LivingEntity
import net.minecraft.item.Item.Settings
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import org.teamvoided.starborn_soundscape.entity.SmallSpeakerEntity
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
            world.spawnEntity(speaker)
        }
    }
    override fun getNameColor(): Formatting {
        return Formatting.RED
    }

    override fun getBarColor(): java.awt.Color {
        return java.awt.Color.RED
    }

    override fun addDescription(tooltip: MutableList<Text?>) {
        tooltip.add(
            Text.translatable("tooltip.soundscape.breakingCoreDesc.tooltip")
                .formatted(Formatting.GRAY)
        )
    }
}