package org.teamvoided.starborn_soundscape.item

import net.minecraft.block.BlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.mokus.mokuslib.itemskin.CustomItemModel
import org.teamvoided.starborn_soundscape.components.BanjolectricData
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeDataComponents
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeSounds
import org.teamvoided.starborn_soundscape.item.overarchieverItem.Companion.BAR_LIMIT
import org.teamvoided.starborn_soundscape.item.overarchieverItem.Companion.funnyMath
import org.teamvoided.starborn_soundscape.item.song_selection.SongItem
import org.teamvoided.starborn_soundscape.item.song_selection.ToolSongHoldingItem
import org.teamvoided.starborn_soundscape.item.songs.BreakRightThroughSongItem
import org.teamvoided.starborn_soundscape.item.songs.BurningAndBlazeSongItem
import org.teamvoided.starborn_soundscape.item.songs.InMyElementSongItem
import software.bernie.geckolib.util.Color

class BanjolectricItem(settings: Settings) : ToolSongHoldingItem(settings), CustomItemModel {
    override fun hasInventoryModel(): Boolean {
        return true
    }

    override fun use(world: World, player: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        player.setCurrentHand(hand)
        return TypedActionResult(ActionResult.CONSUME_PARTIAL, player.getStackInHand(hand))
    }

    val maxAbilityCharge = 60

    override fun postHit(stack: ItemStack, target: LivingEntity, attacker: LivingEntity): Boolean {
        val data = stack.getOrDefault(StarbornSoundscapeDataComponents.BANJOLECTRIC_DATA, BanjolectricData.DEFAULT)
        val newAbilityCharge = data.charge +
                if ((attacker.fallDistance > 0 && !attacker.isOnGround)
                    && data.charge < maxAbilityCharge
                ) 4 //crit
                else if (
                    data.charge < maxAbilityCharge) 1 //hit
                else 0 // just in case

        if (data.charge <= maxAbilityCharge) {
            stack.set(StarbornSoundscapeDataComponents.BANJOLECTRIC_DATA, BanjolectricData(newAbilityCharge))
        }
        if (getSongItem(stack) is BurningAndBlazeSongItem && data.charge >= getSongItem(stack)?.getBanjoChargeReduction(
                attacker
            )!!
        ) {
            target.setOnFireFor(120)
            stack.set(
                StarbornSoundscapeDataComponents.BANJOLECTRIC_DATA,
                BanjolectricData(data.charge - (getSongItem(stack)?.getBanjoChargeReduction(attacker)!!))
            )
        } else if (getSongItem(stack) is InMyElementSongItem && target.frozenTicks <= 200 && data.charge >= getSongItem(
                stack
            )?.getBanjoChargeReduction(attacker)!!
        ) {
            target.frozenTicks += 350
            stack.set(
                StarbornSoundscapeDataComponents.BANJOLECTRIC_DATA,
                BanjolectricData(data.charge - (getSongItem(stack)?.getBanjoChargeReduction(attacker)!!))
            )
        }

        val world = attacker.world
        if (world is ServerWorld) {
            world.playSound(
                null,
                attacker.x,
                attacker.y,
                attacker.z,
                StarbornSoundscapeSounds.HIT_BANJO,
                attacker.soundCategory,
                1.0f,
                1.5f
            )
        }
        return super.postHit(stack, target, attacker)
    }

    override fun usageTick(world: World, user: LivingEntity, stack: ItemStack, remainingUseTicks: Int) {
        val data =
            stack.getOrDefault(StarbornSoundscapeDataComponents.BANJOLECTRIC_DATA, BanjolectricData.DEFAULT)
        if (getSongItem(stack) != null && data.charge >= getSongItem(stack)!!.getBanjoChargeReduction(user) && !getSongItem(
                stack
            )!!.isBanjoPassive()
        ) {
            val song = getSongItem(stack) as SongItem
            val newCharge = data.charge - song.getBanjoChargeReduction(user)
            song.useBanjolectricSong(user, world)
            stack.set(StarbornSoundscapeDataComponents.BANJOLECTRIC_DATA, BanjolectricData(newCharge))
            if (user is PlayerEntity && song.givesBanjoCooldown()) {
                user.itemCooldownManager.set(stack.item, song.getBanjoCooldown())
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

    override fun inventoryTick(stack: ItemStack, world: World, entity: Entity?, slot: Int, selected: Boolean) {
        if (world.time % 75 == 0L){
            val data = stack.getOrDefault(StarbornSoundscapeDataComponents.BANJOLECTRIC_DATA, BanjolectricData.DEFAULT)
            stack.set(StarbornSoundscapeDataComponents.BANJOLECTRIC_DATA, BanjolectricData(data.charge + 1))
        }
        super.inventoryTick(stack, world, entity, slot, selected)
    }

    override fun canMine(state: BlockState?, world: World?, pos: BlockPos?, miner: PlayerEntity?): Boolean {
        return false
    }

    override fun isItemBarVisible(stack: ItemStack): Boolean {
        return true
    }
}