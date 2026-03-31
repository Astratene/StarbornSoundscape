package org.teamvoided.starborn_soundscape.item.songs

import net.minecraft.entity.LivingEntity
import net.minecraft.item.Item.Settings
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.world.World
import org.teamvoided.starborn_soundscape.item.song_selection.SongItem

class BurningAndBlazeSongItem(settings: Settings) : SongItem(settings) {


    override fun isPassive(): Boolean {
        return true
    }

    override fun getOverArchIeverPassiveDrain(user: LivingEntity): Int {
        return 250
    }

    override fun getBanjoChargeReduction(user: LivingEntity): Int {
        return 16
    }

    override fun getMetronomeChargeReduction(user: LivingEntity): Int {
        return 64
    }

    override fun givesMetronomeCooldown(): Boolean {
        return true
    }

    override fun getMetronomeCooldown(): Int {
        return 200
    }

    override fun getNameColor(): Formatting {
        return Formatting.GOLD
    }

    override fun getBarColor(): java.awt.Color {
        return java.awt.Color.ORANGE
    }

    override fun isBanjoPassive(): Boolean {
        return true
    }

    override fun addDescription(tooltip: MutableList<Text?>) {
        tooltip.add(
            Text.translatable("tooltip.soundscape.burnBlaze.tooltip")
                .formatted(Formatting.GRAY)
        )
    }

    override fun addArchieverDescription(tooltip: MutableList<Text?>) {
        tooltip.add(
            Text.translatable("tooltip.soundscape.burnBlazeArchiever.tooltip")
                .formatted(Formatting.GRAY)
        )
    }

    override fun addBanjoDescription(tooltip: MutableList<Text?>) {
        tooltip.add(
            Text.translatable("tooltip.soundscape.burnBlazeBanjo.tooltip")
                .formatted(Formatting.GRAY)
        )
    }
}