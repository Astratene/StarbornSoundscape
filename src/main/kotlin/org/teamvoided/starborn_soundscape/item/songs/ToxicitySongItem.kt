package org.teamvoided.starborn_soundscape.item.songs

import net.minecraft.entity.LivingEntity
import net.minecraft.item.Item.Settings
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.world.World
import org.teamvoided.starborn_soundscape.entity.ToxicCloudEntity
import org.teamvoided.starborn_soundscape.item.song_selection.SongItem

class ToxicitySongItem(settings: Settings) : SongItem(settings) {


    override fun isPassive(): Boolean {
        return true
    }

    override fun useSong(user: LivingEntity, world: World) {
        val boo = 0.1
        val boost = user.rotationVector.multiply(1.0, 1.0, 1.0).normalize().multiply(boo)
        val cloud = ToxicCloudEntity(world, user)
        cloud.setPosition(user.eyePos)
        cloud.isBanjo = true
        cloud.cloudLifespan = 100
        cloud.setVelocity(
            boost.x,
            boost.y,
            boost.z
        )
        world.spawnEntity(cloud)
    }

    override fun useMetronomeSong(user: LivingEntity, world: World) {
        return
    }

    override fun getOverArchIeverPassiveDrain(user: LivingEntity): Int {
        return 250
    }

    override fun getBanjoChargeReduction(user: LivingEntity): Int {
        return 16
    }

    override fun getNameColor(): Formatting {
        return Formatting.DARK_GREEN
    }

    override fun getBarColor(): java.awt.Color {
        return java.awt.Color.GREEN
    }

    override fun addDescription(tooltip: MutableList<Text?>) {
        tooltip.add(
            Text.translatable("tooltip.soundscape.toxicity.tooltip")
                .formatted(Formatting.GRAY)
        )
    }

    override fun addArchieverDescription(tooltip: MutableList<Text?>) {
        tooltip.add(
            Text.translatable("tooltip.soundscape.toxicityArchiever.tooltip")
                .formatted(Formatting.GRAY)
        )
    }

    override fun addBanjoDescription(tooltip: MutableList<Text?>) {
        tooltip.add(
            Text.translatable("tooltip.soundscape.toxicityBanjo.tooltip")
                .formatted(Formatting.GRAY)
        )
    }
}