package org.teamvoided.starborn_soundscape.item.songs

import net.minecraft.entity.LivingEntity
import net.minecraft.item.Item.Settings
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import org.teamvoided.starborn_soundscape.item.song_selection.SongItem

class InMyElementSongItem(settings: Settings) : SongItem(settings) {

    override fun isPassive(): Boolean {
        return true
    }

    override fun getOverArchIeverPassiveDrain(user: LivingEntity): Int {
        return 500
    }

    override fun getOverArchIeverChargeUp(user: LivingEntity): Int {
        return 60
    }

    override fun getBanjoChargeReduction(user: LivingEntity): Int {
        return 16
    }

    override fun givesMetronomeCooldown(): Boolean {
        return true
    }

    override fun getMetronomeCooldown(): Int {
        return 600
    }

    override fun getNameColor(): Formatting {
        return Formatting.DARK_AQUA
    }

    override fun getBarColor(): java.awt.Color {
        return java.awt.Color.CYAN
    }

    override fun isBanjoPassive(): Boolean {
        return true
    }

    override fun addDescription(tooltip: MutableList<Text?>) {
        tooltip.add(
            Text.translatable("tooltip.soundscape.inMyElement.tooltip")
                .formatted(Formatting.GRAY)
        )
    }

    override fun addArchieverDescription(tooltip: MutableList<Text?>) {
        tooltip.add(
            Text.translatable("tooltip.soundscape.inMyElementArchiever.tooltip")
                .formatted(Formatting.GRAY)
        )
    }

    override fun addBanjoDescription(tooltip: MutableList<Text?>) {
        tooltip.add(
            Text.translatable("tooltip.soundscape.inMyElementBanjo.tooltip")
                .formatted(Formatting.GRAY)
        )
    }
}