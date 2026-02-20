package org.teamvoided.starborn_soundscape.item.songs

import net.minecraft.entity.LivingEntity
import net.minecraft.item.Item.Settings
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.world.World
import org.teamvoided.starborn_soundscape.item.song_selection.SongItem

class BurningAndBlazeSongItem(settings: Settings) : SongItem(settings) {

    override fun getOverArchIeverChargeReduction(user: LivingEntity): Int {
        return 10000
    }

    override fun isPassive(): Boolean {
        return true
    }

    override fun getOverArchIeverPassiveDrain(user: LivingEntity): Int {
        return 25
    }

    override fun getNameColor(): Formatting {
        return Formatting.GOLD
    }

    override fun getBarColor(): java.awt.Color {
        return java.awt.Color.ORANGE
    }

    override fun addDescription(tooltip: MutableList<Text?>) {
        tooltip.add(
            Text.translatable("tooltip.soundscape.burnblaze.tooltip")
                .formatted(Formatting.GRAY)
        )
    }
}