package org.teamvoided.starborn_soundscape.util

import net.minecraft.entity.LivingEntity
import net.minecraft.util.Hand
import org.teamvoided.starborn_soundscape.components.BanjolectricData
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeDataComponents
import org.teamvoided.starborn_soundscape.item.BanjolectricItem
import org.teamvoided.starborn_soundscape.item.astra_only_no_snooping.UniverseEdgeItem
import org.teamvoided.starborn_soundscape.item.songs.BreakRightThroughSongItem

fun disablesShields(entity: LivingEntity): Boolean{
    val stack = entity.getStackInHand(Hand.MAIN_HAND)
    if (stack.item is BanjolectricItem && (stack.item as BanjolectricItem).hasASongToSing(stack)){
        val item = stack.item as BanjolectricItem
        val data = stack.getOrDefault(StarbornSoundscapeDataComponents.BANJOLECTRIC_DATA, BanjolectricData.DEFAULT)
        if (item.getSongItem(stack) is BreakRightThroughSongItem && data.charge >= item.getBanjoChargeReduction(stack, entity)){
            stack.set(StarbornSoundscapeDataComponents.BANJOLECTRIC_DATA, BanjolectricData(data.charge - item.getBanjoChargeReduction(stack, entity)))
            return true
        }
    }
    if (stack.item is UniverseEdgeItem){
        return true
    }
    return false
}