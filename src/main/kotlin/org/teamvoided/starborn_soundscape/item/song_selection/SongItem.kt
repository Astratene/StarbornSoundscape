package org.teamvoided.starborn_soundscape.item.song_selection

import net.minecraft.client.item.TooltipConfig
import net.minecraft.entity.LivingEntity
import net.minecraft.item.Item
import net.minecraft.item.Item.Settings
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.util.Color
import net.minecraft.util.Formatting
import net.minecraft.world.World

open class SongItem(settings: Settings) : Item(settings) {

    open fun useSong(user: LivingEntity, world: World){
    }

    open fun addDescription(tooltip: MutableList<Text?>,) {
        tooltip.add(
            Text.translatable("tooltip.soundscape.nullSong.tooltip")
                .formatted(Formatting.RED).formatted(Formatting.ITALIC).formatted(Formatting.BOLD)
        )
    }

    override fun appendTooltip(
        stack: ItemStack?,
        context: TooltipContext?,
        tooltip: MutableList<Text?>,
        config: TooltipConfig?
    ) {
        tooltip.add(
            Text.translatable("tooltip.soundscape.song_can_be_inserted.tooltip").formatted(Formatting.DARK_PURPLE)
        )
        addDescription(tooltip)
        super.appendTooltip(stack, context, tooltip, config)
    }

    open fun getNameColor(): Formatting {
        return Formatting.LIGHT_PURPLE
    }

    open fun getBarColor(): java.awt.Color {
        return java.awt.Color.MAGENTA
    }

    open fun getOverArchIeverChargeReduction(user: LivingEntity): Int {
        return 10000
    }
}