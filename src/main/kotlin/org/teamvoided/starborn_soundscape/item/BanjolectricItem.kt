package org.teamvoided.starborn_soundscape.item

import net.minecraft.block.BlockState
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.mokus.mokuslib.itemskin.CustomItemModel
import org.teamvoided.starborn_soundscape.StarbornSoundscape
import org.teamvoided.starborn_soundscape.components.BanjolectricData
import org.teamvoided.starborn_soundscape.components.CurrentUseTime
import org.teamvoided.starborn_soundscape.components.OverarchieverData
import org.teamvoided.starborn_soundscape.components.OverarchieverDatav2
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeDataComponents
import org.teamvoided.starborn_soundscape.item.overarchieverItem.Companion.BAR_LIMIT
import org.teamvoided.starborn_soundscape.item.overarchieverItem.Companion.USE_TICKS
import org.teamvoided.starborn_soundscape.item.overarchieverItem.Companion.funnyMath
import org.teamvoided.starborn_soundscape.item.song_selection.ToolSongHoldingItem
import org.teamvoided.starborn_soundscape.item.songs.BreakRightThroughSongItem
import org.teamvoided.starborn_soundscape.item.songs.BreakingTheCoreSongItem
import org.teamvoided.starborn_soundscape.item.songs.BurningAndBlazeSongItem
import org.teamvoided.starborn_soundscape.item.songs.FoundDeadSongItem
import org.teamvoided.starborn_soundscape.item.songs.InMyElementSongItem
import software.bernie.geckolib.util.Color
import kotlin.math.min

class BanjolectricItem(settings: Settings) : ToolSongHoldingItem(settings), CustomItemModel {
    override fun hasInventoryModel(): Boolean {
        return true
    }

    override fun use(world: World, player: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        player.setCurrentHand(hand)
        return TypedActionResult(ActionResult.CONSUME_PARTIAL, player.getStackInHand(hand))
    }

    // i dont know if this works because dummys dont work to test it
    override fun postDamageEntity(stack: ItemStack, target: LivingEntity, attacker: LivingEntity) {
        if(getSongItem(stack) is BreakRightThroughSongItem && target is PlayerEntity) {
            target.itemCooldownManager.set(Items.SHIELD, 100); target.stopUsingItem()
        }
        super.postDamageEntity(stack, target, attacker)
    }

    val maxAbilityCharge = 60

    override fun postHit(stack: ItemStack, target: LivingEntity, attacker: LivingEntity): Boolean {
        val data = stack.getOrDefault(StarbornSoundscapeDataComponents.BANJOLECTRIC_DATA, BanjolectricData.DEFAULT)
        if (attacker.fallDistance>0 && !attacker.isOnGround) {
            StarbornSoundscape.log.info("banjo CRIT")
        } else if(getSongItem(stack) !is BreakRightThroughSongItem && data.charge < maxAbilityCharge) {
            StarbornSoundscape.log.info("banjo hit")
        }
        val newAbilityCharge = data.charge +
                if((attacker.fallDistance>0 && !attacker.isOnGround)
                    && getSongItem(stack) !is BreakRightThroughSongItem && data.charge < maxAbilityCharge) 4 //crit
                else if(getSongItem(stack) !is BreakRightThroughSongItem && data.charge < maxAbilityCharge) 1 //hit
                else 0 // just in case

        if (data.charge <= maxAbilityCharge && target is PlayerEntity) {
            stack.set(StarbornSoundscapeDataComponents.BANJOLECTRIC_DATA, BanjolectricData(newAbilityCharge))
        }
        if (getSongItem(stack) is BurningAndBlazeSongItem && data.charge>=getSongItem(stack)?.getBanjoChargeReduction(attacker)!!) {
            target.setOnFireFor(120)
            stack.set(StarbornSoundscapeDataComponents.BANJOLECTRIC_DATA, BanjolectricData(data.charge - (getSongItem(stack)?.getBanjoChargeReduction(attacker)!!)))
        } else if(getSongItem(stack) is InMyElementSongItem && target.frozenTicks<=200 && data.charge>=getSongItem(stack)?.getBanjoChargeReduction(attacker)!!) {
            target.frozenTicks += 350
            stack.set(StarbornSoundscapeDataComponents.BANJOLECTRIC_DATA, BanjolectricData(data.charge - (getSongItem(stack)?.getBanjoChargeReduction(attacker)!!)))
        }

        return super.postHit(stack, target, attacker)
    }

    override fun usageTick(world: World, user: LivingEntity, stack: ItemStack, remainingUseTicks: Int) {
        val data =
            stack.getOrDefault(StarbornSoundscapeDataComponents.BANJOLECTRIC_DATA, BanjolectricData.DEFAULT)
        if (getSongItem(stack) != null && data.charge >= getSongItem(stack)?.getBanjoChargeReduction(user)!!) {
            val newCharge = data.charge - getSongItem(stack)?.getBanjoChargeReduction(user)!!
            getSongItem(stack)?.useSong(user, world)
            stack.set(StarbornSoundscapeDataComponents.BANJOLECTRIC_DATA, BanjolectricData(newCharge))
            if (user is PlayerEntity && getSongItem(stack) is FoundDeadSongItem) {
                user.itemCooldownManager.set(stack.item, 240)
            }
            user.stopUsingItem()
        }

        super.usageTick(world, user, stack, remainingUseTicks)
    }



    override fun getItemBarStep(stack: ItemStack): Int {
        val data =
            stack.getOrDefault(StarbornSoundscapeDataComponents.BANJOLECTRIC_DATA, BanjolectricData.DEFAULT)
        return data?.let {
            funnyMath(
                maxAbilityCharge - it.charge,
                maxAbilityCharge
            )
        } ?: BAR_LIMIT.toInt()
    }

    override fun getItemBarColor(stack: ItemStack): Int {
        if (hasASongToSing(stack)) {
            return getBarColor(stack)
        }
        return Color.BLUE.color
    }

    override fun canMine(state: BlockState?, world: World?, pos: BlockPos?, miner: PlayerEntity?): Boolean {
        return false
    }

    override fun isItemBarVisible(stack: ItemStack): Boolean {
        return true
    }
}