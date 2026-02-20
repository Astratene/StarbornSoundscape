package org.teamvoided.starborn_soundscape.item.songs

import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
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
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeEffects
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeSounds
import org.teamvoided.starborn_soundscape.item.song_selection.SongItem

class AllEyesSongItem(settings: Settings) : SongItem(settings) {

    override fun useSong(user: LivingEntity, world: World) {
        val entities = mutableListOf<Entity>()
        entities.addAll(
            world.getOtherEntities(
                user, Box(
                    user.pos.x + 5.0,
                    user.pos.y + 3.0,
                    user.pos.z + 5.0,
                    user.pos.x - 5.0,
                    user.pos.y - 1.0,
                    user.pos.z - 5.0
                )
            ).filter { it is LivingEntity && it != user }
        )
        for (entity in entities){
            (entity as LivingEntity).addStatusEffect(
                StatusEffectInstance(
                    StarbornSoundscapeEffects.CLOSED_EYES,
                    200, 0,
                    false, false, true
                )
            )
        }
        user.addStatusEffect(
            StatusEffectInstance(
                StatusEffects.GLOWING,
                200, 0,
                false, false, true
            )
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

    override fun getOverArchIeverChargeReduction(user: LivingEntity): Int {
        return 10000

    }

    override fun getNameColor(): Formatting {
        return Formatting.DARK_GRAY
    }

    override fun getBarColor(): java.awt.Color {
        return java.awt.Color.DARK_GRAY
    }

    override fun addDescription(tooltip: MutableList<Text?>) {
        tooltip.add(
            Text.translatable("tooltip.soundscape.eyes.tooltip")
                .formatted(Formatting.GRAY)
        )
    }
}