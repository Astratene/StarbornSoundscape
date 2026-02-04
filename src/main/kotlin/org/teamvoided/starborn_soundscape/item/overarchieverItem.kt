package org.teamvoided.starborn_soundscape.item

import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.Item.Settings
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.UseAction
import net.minecraft.world.World
import org.teamvoided.starborn_soundscape.entity.CosmicBoltEntity
import org.teamvoided.starborn_soundscape.util.setPropertiesTwo
import kotlin.math.min

class overarchieverItem(settings: Settings) : Item(settings) {

    override fun use(world: World, player: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        player.setCurrentHand(hand)
        return TypedActionResult(ActionResult.CONSUME_PARTIAL, player.getStackInHand(hand))
    }

    fun getChargeTicks() : Int {
        return if (false) 40 else if (false) 60 else if (false) 80 else 20 // will change based on enchantments
    }

    fun getAngleBetweenTriBolts(ticks: Int) : Float {
        return (20 - (ticks - 20)).plus(5).toFloat()
    }

    fun getAngleBetweenWellBolts(ticks: Int) : Float {
        return (ticks - 20).times(0.3f)
    }

    fun getMaxSpread(ticks: Int) : Float {
        return 70 - (ticks - 20f)
    }

    fun getLaunchVelocity(ticks: Int) : Float {
        return (ticks / getChargeTicks().toFloat()).times(5f)
    }

    override fun usageTick(world: World?, user: LivingEntity?, stack: ItemStack?, remainingUseTicks: Int) {
        val usedTicks = min(USE_TICKS - remainingUseTicks, getChargeTicks())
        println(usedTicks)
        super.usageTick(world, user, stack, remainingUseTicks)
    }

    override fun onStoppedUsing(stack: ItemStack?, world: World, user: LivingEntity, remainingUseTicks: Int) {
        val usedTickes = min(USE_TICKS - remainingUseTicks, getChargeTicks())
        if (usedTickes >= MIN_TICKS_TO_FIRE){
            fire(world, user, usedTickes)
        }
        super.onStoppedUsing(stack, world, user, remainingUseTicks)
    }

    fun fire(world: World, user: LivingEntity, ticks: Int) {
        if (false){
            fireTriBolts(world, user, ticks)
        }
        else if (false){
            fireWellBolts(world, user, ticks)
        }
        else if (false){
            fireSoManyFuckingBolts(world, user, ticks)
        }
        else {
            val entity = CosmicBoltEntity(world, user)
            entity.setPosition(user.eyePos)
            setPropertiesTwo(entity, user.pitch, user.yaw, 0.0f, getLaunchVelocity(ticks), 0.0f)
            world.spawnEntity(entity)
        }
    }

    fun fireTriBolts(world: World, user: LivingEntity, ticks: Int) {
        val entity = CosmicBoltEntity(world, user)
        entity.setPosition(user.eyePos)
        setPropertiesTwo(entity, user.pitch, user.yaw, 0.0f, getLaunchVelocity(ticks), 0.0f)
        world.spawnEntity(entity)
        if (user.isOnGround) {
            val entity2 = CosmicBoltEntity(world, user)
            entity2.setPosition(user.eyePos)
            setPropertiesTwo(entity2, user.pitch, user.yaw + getAngleBetweenTriBolts(ticks), 0.0f, getLaunchVelocity(ticks), 0.0f)
            world.spawnEntity(entity2)
            val entity3 = CosmicBoltEntity(world, user)
            entity3.setPosition(user.eyePos)
            setPropertiesTwo(entity3, user.pitch, user.yaw - getAngleBetweenTriBolts(ticks), 0.0f, getLaunchVelocity(ticks), 0.0f)
            world.spawnEntity(entity3)
        }
        else {
            val entity2 = CosmicBoltEntity(world, user)
            entity2.setPosition(user.eyePos)
            setPropertiesTwo(entity2, user.pitch + getAngleBetweenTriBolts(ticks) , user.yaw, 0.0f, getLaunchVelocity(ticks), 0.0f)
            world.spawnEntity(entity2)
            val entity3 = CosmicBoltEntity(world, user)
            entity3.setPosition(user.eyePos)
            setPropertiesTwo(entity3, user.pitch - getAngleBetweenTriBolts(ticks), user.yaw, 0.0f, getLaunchVelocity(ticks), 0.0f)
            world.spawnEntity(entity3)
        }
    }

    fun fireWellBolts(world: World, user: LivingEntity, ticks: Int) {
        val entity = CosmicBoltEntity(world, user)
        entity.setPosition(user.eyePos)
        setPropertiesTwo(entity, user.pitch, user.yaw, 0.0f, getLaunchVelocity(ticks), 0.0f)
        world.spawnEntity(entity)
        if (user.isOnGround) {
            val entity2 = CosmicBoltEntity(world, user)
            entity2.setPosition(user.eyePos)
            setPropertiesTwo(entity2, user.pitch, user.yaw + getAngleBetweenWellBolts(ticks), 0.0f, getLaunchVelocity(ticks), 0.0f)
            world.spawnEntity(entity2)
            val entity3 = CosmicBoltEntity(world, user)
            entity3.setPosition(user.eyePos)
            setPropertiesTwo(entity3, user.pitch, user.yaw - getAngleBetweenWellBolts(ticks), 0.0f, getLaunchVelocity(ticks), 0.0f)
            world.spawnEntity(entity3)
            val entity4 = CosmicBoltEntity(world, user)
            entity4.setPosition(user.eyePos)
            setPropertiesTwo(entity4, user.pitch, user.yaw + getAngleBetweenWellBolts(ticks).times(2), 0.0f, getLaunchVelocity(ticks), 0.0f)
            world.spawnEntity(entity4)
            val entity5 = CosmicBoltEntity(world, user)
            entity5.setPosition(user.eyePos)
            setPropertiesTwo(entity5, user.pitch, user.yaw - getAngleBetweenWellBolts(ticks).times(2), 0.0f, getLaunchVelocity(ticks), 0.0f)
            world.spawnEntity(entity5)
        }
        else {
            val entity2 = CosmicBoltEntity(world, user)
            entity2.setPosition(user.eyePos)
            setPropertiesTwo(entity2, user.pitch + getAngleBetweenWellBolts(ticks) , user.yaw, 0.0f, getLaunchVelocity(ticks), 0.0f)
            world.spawnEntity(entity2)
            val entity3 = CosmicBoltEntity(world, user)
            entity3.setPosition(user.eyePos)
            setPropertiesTwo(entity3, user.pitch - getAngleBetweenWellBolts(ticks), user.yaw, 0.0f, getLaunchVelocity(ticks), 0.0f)
            world.spawnEntity(entity3)
            val entity4 = CosmicBoltEntity(world, user)
            entity4.setPosition(user.eyePos)
            setPropertiesTwo(entity4, user.pitch + getAngleBetweenWellBolts(ticks).times(2), user.yaw, 0.0f, getLaunchVelocity(ticks), 0.0f)
            world.spawnEntity(entity4)
            val entity5 = CosmicBoltEntity(world, user)
            entity5.setPosition(user.eyePos)
            setPropertiesTwo(entity5, user.pitch - getAngleBetweenWellBolts(ticks).times(2), user.yaw, 0.0f, getLaunchVelocity(ticks), 0.0f)
            world.spawnEntity(entity5)
        }
    }

    fun fireSoManyFuckingBolts(world: World, user: LivingEntity, ticks: Int) {
        repeat(9){
            val entity = CosmicBoltEntity(world, user)
            entity.setPosition(user.eyePos)
            setPropertiesTwo(entity, user.pitch, user.yaw, 0.0f, getLaunchVelocity(ticks), getMaxSpread(ticks))
            world.spawnEntity(entity)
        }
    }

    override fun getUseAction(stack: ItemStack): UseAction = UseAction.BLOCK

    override fun getUseTicks(stack: ItemStack, livingEntity: LivingEntity): Int = USE_TICKS

    companion object {
        const val USE_TICKS = 72000
        const val MIN_TICKS_TO_FIRE = 20
    }
}