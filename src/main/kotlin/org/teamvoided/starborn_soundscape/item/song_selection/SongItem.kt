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

    open fun useSong(user: LivingEntity, world: World){}

    open fun passiveSong(user: LivingEntity, world: World) {}

    open fun passiveAxSong(user: LivingEntity, world: World) {
        passiveSong(user, world)
    }

    open fun useAxSong(user: LivingEntity, world: World) {
        useSong(user, world)
    }

    open fun isPassive() : Boolean {
        return false
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

    // this is now also used as a songs modifier to charge speed
    open fun getOverArchIeverPassiveDrain(user: LivingEntity): Int {
        return 4
    }
}