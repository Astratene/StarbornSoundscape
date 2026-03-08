package org.teamvoided.starborn_soundscape.item.songs

import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item.Settings
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.math.Box
import net.minecraft.world.World
import org.joml.Math.lerp
import org.teamvoided.starborn_soundscape.entity.BigSpeakerEntity
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeEffects
import org.teamvoided.starborn_soundscape.item.song_selection.SongItem
import kotlin.math.roundToInt

class SentYouReelingSongItem(settings: Settings) : SongItem(settings) {

    val FORWARD_BOOST = -1.0
    val ENTITY_BOOST = 2.5
    override fun useBanjolectricSong(user: LivingEntity, world: World) {
        world.playSound(
            null,
            user.x,
            user.y,
            user.z,
            SoundEvents.ENTITY_BREEZE_WIND_BURST.value(),
            SoundCategory.PLAYERS,
            1.0F,
            1.0f
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
            val entities = collectEntitiesInBeamWithMinPos(3.0, user, 1.0, 0.0, world)
            for (entity in entities){
                val boo = ENTITY_BOOST
                val boost = user.rotationVector.multiply(1.0, 1.0, 1.0).normalize().multiply(boo)
                entity.setVelocity(user.velocity.x + boost.x, user.velocity.y + boost.y, user.velocity.z + boost.z)
                entity.velocityModified = true
            }
        }
    }

    override fun useSong(user: LivingEntity, world: World) {
        world.playSound(
            null,
            user.x,
            user.y,
            user.z,
            SoundEvents.ENTITY_BREEZE_WIND_BURST.value(),
            SoundCategory.PLAYERS,
            1.0F,
            1.0f
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
        val entities = mutableListOf<Entity>()
        entities.addAll(
            world.getOtherEntities(
                user, Box(
                    user.x + 5,
                    user.y + 3,
                    user.z + 5,
                    user.x - 5,
                    user.y,
                    user.z - 5
                )
            )
        )
        for (entity in entities){
            val boo = ENTITY_BOOST
            val boost = user.rotationVector.multiply(1.0, 1.0, 1.0).normalize().multiply(boo)
            entity.setVelocity(user.velocity.x + boost.x, user.velocity.y + boost.y, user.velocity.z + boost.z)
            entity.velocityModified = true
        }
    }

    override fun getOverarchieverChargeReduction(user: LivingEntity): Int {
        return 50000
    }

    override fun getBanjoChargeReduction(user: LivingEntity): Int {
        return 16
    }

    override fun getNameColor(): Formatting {
        return Formatting.LIGHT_PURPLE
    }

    override fun getBarColor(): java.awt.Color {
        return java.awt.Color.LIGHT_GRAY
    }

    override fun addDescription(tooltip: MutableList<Text?>) {
        tooltip.add(
            Text.translatable("tooltip.soundscape.sentYouReeling.tooltip")
                .formatted(Formatting.GRAY)
        )
    }

    fun collectEntitiesInBeamWithMinPos(size: Double, caster: LivingEntity, length: Double, minPos: Double, world: World): MutableList<Entity> {
        val entities = mutableListOf<Entity>()
        val endPos = caster.eyePos.add(caster.rotationVector.multiply(length)).add(0.0, 1.0, 0.0)
        val startPos = caster.eyePos.add(caster.rotationVector.multiply(minPos)).add(0.0, 1.0, 0.0)
        val interval = length / size
        for (i in 0..interval.roundToInt()) {
            entities.addAll(
                world.getOtherEntities(
                    caster, Box(
                        (lerp(startPos.x, endPos.x, i / interval)) + size,
                        (lerp(startPos.y + 2 - size, endPos.y, i / interval)) + size,
                        (lerp(startPos.z, endPos.z, i / interval)) + size,
                        (lerp(startPos.x, endPos.x, i / interval)) - size,
                        (lerp(startPos.y + 2 - size, endPos.y, i / interval)) - size,
                        (lerp(startPos.z, endPos.z, i / interval)) - size
                    )
                )
            )
        }
        return entities
    }

}