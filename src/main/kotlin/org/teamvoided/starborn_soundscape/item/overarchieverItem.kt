package org.teamvoided.starborn_soundscape.item

import net.minecraft.block.BlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.PersistentProjectileEntity.PickupPermission
import net.minecraft.item.ItemStack
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.UseAction
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import net.mokus.mokuslib.itemskin.CustomItemModel
import org.teamvoided.starborn_soundscape.components.CurrentUseTime
import org.teamvoided.starborn_soundscape.components.OverarchieverData
import org.teamvoided.starborn_soundscape.components.OverarchieverDatav2
import org.teamvoided.starborn_soundscape.data.StarbornSoundscapeEnchantments
import org.teamvoided.starborn_soundscape.entity.CosmicBoltEntity
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeDataComponents
import org.teamvoided.starborn_soundscape.item.song_selection.SongHoldingItem
import org.teamvoided.starborn_soundscape.item.songs.BreakRightThroughSongItem
import org.teamvoided.starborn_soundscape.item.songs.BurningAndBlazeSongItem
import org.teamvoided.starborn_soundscape.item.songs.InMyElementSongItem
import org.teamvoided.starborn_soundscape.util.getPlayerLookingDirectionPos
import org.teamvoided.starborn_soundscape.util.hasEnchantment
import org.teamvoided.starborn_soundscape.util.setPropertiesBasedOnPlayerLookingDirection
import org.teamvoided.starborn_soundscape.util.setPropertiesTwo
import java.lang.Math.clamp
import kotlin.math.max
import kotlin.math.min
import kotlin.math.round

class overarchieverItem(settings: Settings) : SongHoldingItem(settings), CustomItemModel {

    override fun use(world: World, player: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        player.setCurrentHand(hand)
        return TypedActionResult(ActionResult.CONSUME_PARTIAL, player.getStackInHand(hand))
    }

    override fun hasInventoryModel(): Boolean {
        return true
    }

    //override fun post

    fun getChargeTicks(user: LivingEntity, stack: ItemStack): Int {
        return if (isEnchantedTri(user, stack)) 30 else if (isEnchantedWell(user, stack)) 40
        else if (isEnchantedGrizz(user, stack)) 60 else 20
    }

    fun getMinChargeTicks(user: LivingEntity, stack: ItemStack): Int {
        return if (isEnchantedTri(user, stack)) 20 else if (isEnchantedWell(user, stack)) 25
        else if (isEnchantedGrizz(user, stack)) 40 else 15
    }

    fun getExtraFlareTicks(user: LivingEntity, stack: ItemStack): Int {
        return if (isEnchantedGrizz(user, stack)) -1 else -1
    }

    fun getAngleBetweenTriBolts(ticks: Int): Float {
        return (20 - ((ticks - 20) * 2)).plus(2).toFloat()
    }

    fun getAngleBetweenWellBolts(ticks: Int): Float {
        return (ticks - 20).times(0.15f).plus(3f)
    }

    fun getMaxSpread(ticks: Int): Float {
        return 20 - (0.5f * max(ticks - 30, 0))
    }

    fun getLaunchVelocity(ticks: Int, user: LivingEntity, stack: ItemStack): Float {
        if (isEnchantedWell(user, stack)){
            return (ticks / getChargeTicks(user, stack).toFloat()).times(4f)
        }
        if (!isEnchantedWell(user, stack) && !isEnchantedGrizz(user, stack) && !isEnchantedTri(user, stack)){
            if (ticks < 20){
                return (ticks / getChargeTicks(user, stack).toFloat()).times(3f)
            }
            else {
                return (ticks / getChargeTicks(user, stack).toFloat()).times(5f)
            }
        }
        return (ticks / getChargeTicks(user, stack).toFloat()).times(5f)
    }

    val maxCharge = 100000
    val chargePerTick = 40
    override fun inventoryTick(stack: ItemStack, world: World?, entity: Entity, slot: Int, selected: Boolean) {
        val data = stack.getOrDefault(StarbornSoundscapeDataComponents.OVERARCHIEVER_DATA, OverarchieverData.DEFAULT)
        val data2 =
            stack.getOrDefault(StarbornSoundscapeDataComponents.OVERARCHIEVER_DATAV2, OverarchieverDatav2.DEFAULT)
        if (!data2.passivelyDraining) {
            if (data.charge < maxCharge) {
                val newCharge =
                    data.charge + if (this.hasASongToSing(stack) && entity is LivingEntity) getOverarchieverChargeUp(
                        stack,
                        entity
                    ) else chargePerTick
                stack.set(StarbornSoundscapeDataComponents.OVERARCHIEVER_DATA, OverarchieverData(newCharge))
            }
        } else {
            if (data.charge > 0 && hasASongToSing(stack) && entity is LivingEntity) {
                val newCharge = data.charge - getOverarchieverPassiveDrain(stack, entity)
                stack.set(StarbornSoundscapeDataComponents.OVERARCHIEVER_DATA, OverarchieverData(newCharge))
            } else {
                stack.set(StarbornSoundscapeDataComponents.OVERARCHIEVER_DATA, OverarchieverData(0))
                stack.set(StarbornSoundscapeDataComponents.OVERARCHIEVER_DATAV2, OverarchieverDatav2(false))
            }
        }
        super.inventoryTick(stack, world, entity, slot, selected)
    }

    override fun usageTick(world: World, user: LivingEntity, stack: ItemStack, remainingUseTicks: Int) {
        val usedTicks = min(USE_TICKS - remainingUseTicks, getChargeTicks(user, stack))
        stack.set(StarbornSoundscapeDataComponents.CURRENT_USE_TIME, CurrentUseTime(usedTicks))
        if (user.handSwingTicks in 1..<10) {
            val data =
                stack.getOrDefault(StarbornSoundscapeDataComponents.OVERARCHIEVER_DATA, OverarchieverData.DEFAULT)
            if (data.charge >= getOverarchieverUseCharge(stack, user)) {
                if (isPassive(stack)) {
                    stack.set(StarbornSoundscapeDataComponents.OVERARCHIEVER_DATAV2, OverarchieverDatav2(true))
                } else {
                    val newCharge = data.charge - getOverarchieverUseCharge(stack, user)
                    useSong(stack, user, world)
                    stack.set(StarbornSoundscapeDataComponents.OVERARCHIEVER_DATA, OverarchieverData(newCharge))
                }
                if (user is PlayerEntity) {
                    user.itemCooldownManager.set(stack.item, 5)
                }
                stack.set(StarbornSoundscapeDataComponents.CURRENT_USE_TIME, CurrentUseTime(0))
                user.handSwingTicks = 11
                user.stopUsingItem()
            }
        }

        if (usedTicks == (getMinChargeTicks(user, stack) - 1) || (usedTicks + 1) == (getChargeTicks(user, stack)) || usedTicks == getExtraFlareTicks(
                user,
                stack
            )
        ) {
            val vec3d: Vec3d = user.getLerpedEyePos(1f)
            val vec3d2: Vec3d = user.getRotationVec(1f)
            val vec3d3 = vec3d.add(vec3d2.x * 1, vec3d2.y * 1, vec3d2.z * 1)
            if (world is ServerWorld) {
                world.spawnParticles(
                    ParticleTypes.GLOW,
                    vec3d3.x,
                    vec3d3.y - 0.25,
                    vec3d3.z,
                    5,
                    0.0,
                    0.0,
                    0.0,
                    0.2
                )
                val pitch = if (usedTicks + 1 == getChargeTicks(user, stack)) 2.0f else 1.75f
                world.playSound(
                    null,
                    user.x,
                    user.y,
                    user.z,
                    SoundEvents.BLOCK_VAULT_CLOSE_SHUTTER,
                    SoundCategory.PLAYERS,
                    2.0F,
                    pitch
                )
            }
        }
        super.usageTick(world, user, stack, remainingUseTicks)
    }

    override fun onStoppedUsing(stack: ItemStack, world: World, user: LivingEntity, remainingUseTicks: Int) {
        val usedTickes = min(USE_TICKS - remainingUseTicks, getChargeTicks(user, stack))
        stack.set(StarbornSoundscapeDataComponents.CURRENT_USE_TIME, CurrentUseTime(0))
        if (usedTickes >= getMinChargeTicks(user, stack)) {
            fire(world, user, usedTickes, stack)
        }
        super.onStoppedUsing(stack, world, user, remainingUseTicks)
    }

    fun fire(world: World, user: LivingEntity, ticks: Int, stack: ItemStack) {
        val data =
            stack.getOrDefault(StarbornSoundscapeDataComponents.OVERARCHIEVER_DATAV2, OverarchieverDatav2.DEFAULT)
        if (isEnchantedTri(user, stack)) {
            fireTriBolts(world, user, ticks, stack)
        } else if (isEnchantedWell(user, stack)) {
            fireWellBolts(world, user, ticks, stack)
        } else if (isEnchantedGrizz(user, stack)) {
            fireSoManyFuckingBolts(world, user, ticks, stack)
        } else {
            val entity = CosmicBoltEntity(world, user)
            entity.setPosition(user.eyePos)
            //setPropertiesTwo(entity, user.pitch, user.yaw, 0.0f, getLaunchVelocity(ticks, user, stack), 0.0f)
            setPropertiesBasedOnPlayerLookingDirection(
                entity,
                user.eyePos,
                getPlayerLookingDirectionPos(user),
                getLaunchVelocity(ticks, user, stack),
                0.0f
            )
            entity.pickupType = PickupPermission.DISALLOWED
            if (ticks < 20) entity.directDamage = baseDamage.toFloat() * 0.75f else entity.directDamage = baseDamage.toFloat()
            if (isEnchantedTracer(user, stack)) {
                entity.tracerRound = true
            }
            if (data.passivelyDraining) {
                if (getSongItem(stack) is BurningAndBlazeSongItem) {
                    entity.fireRound = true
                } else if (getSongItem(stack) is BreakRightThroughSongItem) {
                    entity.breakRound = true
                } else if (getSongItem(stack) is InMyElementSongItem){
                    entity.sparkRound = true
                }
            }
            world.spawnEntity(entity)
        }
        world.playSoundFromEntity(user, SoundEvents.ITEM_CROSSBOW_SHOOT, SoundCategory.PLAYERS, 1.0f, 0.75f)
    }


    val TriDirectDamage = 7.0f
    val TriIndirectDamage = 5f
    fun fireTriBolts(world: World, user: LivingEntity, ticks: Int, stack: ItemStack) {
        val data =
            stack.getOrDefault(StarbornSoundscapeDataComponents.OVERARCHIEVER_DATAV2, OverarchieverDatav2.DEFAULT)
        var angle = getAngleBetweenTriBolts(ticks)
        val isOnGround = user.isOnGround
        repeat(3) {
            val entity = CosmicBoltEntity(world, user)
            entity.setPosition(user.eyePos)
            if (isOnGround) {
                setPropertiesTwo(
                    entity,
                    user.pitch,
                    user.yaw + angle,
                    0.0f,
                    getLaunchVelocity(ticks, user, stack),
                    0.0f
                )
            } else {
                setPropertiesTwo(
                    entity,
                    user.pitch + angle,
                    user.yaw,
                    0.0f,
                    getLaunchVelocity(ticks, user, stack),
                    0.0f
                )
            }
            entity.directDamage = TriDirectDamage
            entity.indirectDamage = TriIndirectDamage
            entity.pickupType = PickupPermission.DISALLOWED
            if (data.passivelyDraining) {
                if (getSongItem(stack) is BurningAndBlazeSongItem) {
                    entity.fireRound = true
                } else if (getSongItem(stack) is BreakRightThroughSongItem) {
                    entity.breakRound = true
                } else if (getSongItem(stack) is InMyElementSongItem){
                    entity.sparkRound = true
                }
            }
            world.spawnEntity(entity)
            angle -= getAngleBetweenTriBolts(ticks)
        }
    }

    val WellDirectDamage = 4.5f
    val WellIndirectDamage = 4.0f
    val nonFullMult = 0.75f
    fun fireWellBolts(world: World, user: LivingEntity, ticks: Int, stack: ItemStack) {
        val data =
            stack.getOrDefault(StarbornSoundscapeDataComponents.OVERARCHIEVER_DATAV2, OverarchieverDatav2.DEFAULT)
        var angle = getAngleBetweenWellBolts(ticks).times(2)
        val isOnGround = user.isOnGround
        val newDamage = if (ticks < 40) (WellIndirectDamage * nonFullMult) else WellIndirectDamage
        repeat(5) {
            val entity = CosmicBoltEntity(world, user)
            entity.setPosition(user.eyePos)
            if (isOnGround) {
                setPropertiesTwo(
                    entity,
                    user.pitch,
                    user.yaw + angle,
                    0.0f,
                    getLaunchVelocity(ticks, user, stack),
                    0.0f
                )
            } else {
                setPropertiesTwo(
                    entity,
                    user.pitch + angle,
                    user.yaw,
                    0.0f,
                    getLaunchVelocity(ticks, user, stack),
                    0.0f
                )
            }
            entity.directDamage = WellDirectDamage
            entity.indirectDamage = newDamage
            entity.pickupType = PickupPermission.DISALLOWED
            if (data.passivelyDraining) {
                if (getSongItem(stack) is BurningAndBlazeSongItem) {
                    entity.fireRound = true
                } else if (getSongItem(stack) is BreakRightThroughSongItem) {
                    entity.breakRound = true
                } else if (getSongItem(stack) is InMyElementSongItem){
                    entity.sparkRound = true
                }
            }
            world.spawnEntity(entity)
            angle -= getAngleBetweenWellBolts(ticks)
        }
    }

    val GrizzDirectDamage = 0.2f
    val GrizzIndirectDamage = 5f
    fun fireSoManyFuckingBolts(world: World, user: LivingEntity, ticks: Int, stack: ItemStack) {
        val data =
            stack.getOrDefault(StarbornSoundscapeDataComponents.OVERARCHIEVER_DATAV2, OverarchieverDatav2.DEFAULT)
        repeat(9) {
            val entity = CosmicBoltEntity(world, user)
            entity.setPosition(user.eyePos)
            setPropertiesTwo(
                entity,
                user.pitch,
                user.yaw,
                0.0f,
                getLaunchVelocity(ticks, user, stack),
                getMaxSpread(ticks)
            )
            entity.directDamage = GrizzDirectDamage
            entity.indirectDamage = GrizzIndirectDamage
            entity.timeTillBoom = 20 + world.random.range(-5, 5)
            entity.pickupType = PickupPermission.DISALLOWED
            if (data.passivelyDraining) {
                if (getSongItem(stack) is BurningAndBlazeSongItem) {
                    entity.fireRound = true
                } else if (getSongItem(stack) is BreakRightThroughSongItem) {
                    entity.breakRound = true
                } else if (getSongItem(stack) is InMyElementSongItem){
                    entity.sparkRound = true
                }
            }
            //entity.airResOnDrop = 0.5 + world.random.nextFloat().times(0.25)
            world.spawnEntity(entity)
        }
    }

    override fun getUseAction(stack: ItemStack): UseAction = UseAction.BOW

    override fun getUseTicks(stack: ItemStack, livingEntity: LivingEntity): Int = USE_TICKS

    fun isEnchantedTri(user: LivingEntity, stack: ItemStack): Boolean {
        return stack.hasEnchantment(StarbornSoundscapeEnchantments.TRI_THIS)
    }

    fun isEnchantedTracer(user: LivingEntity, stack: ItemStack): Boolean {
        return stack.hasEnchantment(StarbornSoundscapeEnchantments.TRACER)
    }

    fun isEnchantedWell(user: LivingEntity, stack: ItemStack): Boolean {
        return stack.hasEnchantment(StarbornSoundscapeEnchantments.WELL_WELL_WELL)
    }

    fun isEnchantedGrizz(user: LivingEntity, stack: ItemStack): Boolean {
        return stack.hasEnchantment(StarbornSoundscapeEnchantments.GRIZZLY_FATE)
    }

    companion object {
        const val USE_TICKS = 72000
        const val MIN_TICKS_TO_FIRE = 20

        const val baseDamage = 10.0

        const val BAR_LIMIT = 13f
        fun funnyMath(x: Int, y: Int) = clamp(round(BAR_LIMIT - x * BAR_LIMIT / y).toLong(), 0, BAR_LIMIT.toInt())
    }

    override fun getItemBarColor(stack: ItemStack): Int {
        return getBarColor(stack)
    }

    override fun getItemBarStep(stack: ItemStack): Int {
        val data =
            stack.getOrDefault(StarbornSoundscapeDataComponents.OVERARCHIEVER_DATA, OverarchieverData.DEFAULT)
        return data?.let {
            funnyMath(
                maxCharge - it.charge,
                maxCharge
            )
        } ?: BAR_LIMIT.toInt()
    }

    override fun canMine(state: BlockState?, world: World?, pos: BlockPos?, miner: PlayerEntity?): Boolean {
        return false
    }

    override fun isItemBarVisible(stack: ItemStack): Boolean {
        return hasASongToSing(stack)
    }
}