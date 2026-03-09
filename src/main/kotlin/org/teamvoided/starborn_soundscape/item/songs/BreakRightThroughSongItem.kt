package org.teamvoided.starborn_soundscape.item.songs

import net.minecraft.entity.LivingEntity
import net.minecraft.item.Item.Settings
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import org.teamvoided.starborn_soundscape.item.song_selection.SongItem

class BreakRightThroughSongItem(settings: Settings) : SongItem(settings) {



    override fun isPassive(): Boolean {
        return true
    }

    override fun getOverArchIeverPassiveDrain(user: LivingEntity): Int {
        return 250
    }

    override fun getNameColor(): Formatting {
        return Formatting.GREEN
    }

    override fun getBarColor(): java.awt.Color {
        return java.awt.Color.GREEN
    }

    override fun isBanjoPassive(): Boolean {
        return true
    }

    override fun getBanjoChargeReduction(user: LivingEntity): Int {
        return 12
    }

    override fun addDescription(tooltip: MutableList<Text?>) {
        tooltip.add(
            Text.translatable("tooltip.soundscape.breakRightThrough.tooltip")
                .formatted(Formatting.GRAY)
        )
    }
}