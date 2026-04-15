package org.teamvoided.starborn_soundscape.util

import net.minecraft.component.DataComponentTypes
import net.minecraft.item.AxeItem
import net.minecraft.item.BowItem
import net.minecraft.item.BrushItem
import net.minecraft.item.CrossbowItem
import net.minecraft.item.ItemStack
import net.minecraft.item.MaceItem
import net.minecraft.item.PotionItem
import net.minecraft.item.ShieldItem
import net.minecraft.item.SwordItem
import org.teamvoided.starborn_soundscape.data.StarbornSoundscapeEnchantments
import org.teamvoided.starborn_soundscape.item.OverarchieverItem

fun modifyItemUseSlowdown(stack: ItemStack): Float {
    return when (stack.item) {
        is OverarchieverItem -> {
            if(stack.hasEnchantment(StarbornSoundscapeEnchantments.TRI_THIS)) 0.45f
            else if(stack.hasEnchantment(StarbornSoundscapeEnchantments.WELL_WELL_WELL)) 0.4f
            else if(stack.hasEnchantment(StarbornSoundscapeEnchantments.BOLT_RAIN)) 0.05f
            else if(stack.hasEnchantment(StarbornSoundscapeEnchantments.GRIZZLY_FATE)) 0.2f
            else 0.55f
        }
        else -> 0.2f
    }
}