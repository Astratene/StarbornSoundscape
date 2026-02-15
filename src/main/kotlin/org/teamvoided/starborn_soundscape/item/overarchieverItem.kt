package org.teamvoided.starborn_soundscape.item

import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.PersistentProjectileEntity.PickupPermission
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.UseAction
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import org.teamvoided.starborn_soundscape.components.OverarchieverData
import org.teamvoided.starborn_soundscape.data.StarbornSoundscapeEnchantments
import org.teamvoided.starborn_soundscape.entity.CosmicBoltEntity
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeDataComponents
import org.teamvoided.starborn_soundscape.item.song_selection.SongHoldingItem
import org.teamvoided.starborn_soundscape.util.hasEnchantment
import org.teamvoided.starborn_soundscape.util.setPropertiesTwo
import java.awt.Color
import java.lang.Math.clamp
import kotlin.compareTo
import kotlin.math.max
import kotlin.math.min
import kotlin.math.round

class overarchieverItem(settings: Settings) : SongHoldingItem(settings) {

    override fun use(world: World, player: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        player.setCurrentHand(hand)
        return TypedActionResult(ActionResult.CONSUME_PARTIAL, player.getStackInHand(hand))
    }

    //override fun post

    fun getChargeTicks(user: LivingEntity, stack: ItemStack): Int {
        return if (isTestEnchantedTri(user, stack)) 40 else if (isTestEnchantedWell(
                user,
                stack
            )
        ) 60 else if (isTestEnchantedGrizz(user, stack)) 80 else 20 // will change based on enchantments
    }

    fun getExtraFlareTicks(user: LivingEntity, stack: ItemStack): Int {
        return if (isTestEnchantedGrizz(user, stack)) 40 else -1
    }

    fun getAngleBetweenTriBolts(ticks: Int): Float {
        return (20 - (ticks - 20)).plus(2).toFloat()
    }

    fun getAngleBetweenWellBolts(ticks: Int): Float {
        return (ticks - 20).times(0.2f).plus(1f)
    }

    fun getMaxSpread(ticks: Int): Float {
        return 30 - (0.375f * max(ticks - 40, 0))
    }

    fun getLaunchVelocity(ticks: Int, user: LivingEntity, stack: ItemStack): Float {
        return (ticks / getChargeTicks(user, stack).toFloat()).times(5f)
    }

    val maxCharge = 10000
    val chargePerTick = 4
    override fun inventoryTick(stack: ItemStack, world: World?, entity: Entity?, slot: Int, selected: Boolean) {
        val data = stack.getOrDefault(StarbornSoundscapeDataComponents.OVERARCHIEVER_DATA, OverarchieverData.DEFAULT)
        if (data.charge < maxCharge) {
            val newCharge = data.charge + chargePerTick
            stack.set(StarbornSoundscapeDataComponents.OVERARCHIEVER_DATA, OverarchieverData(newCharge))
        }
        super.inventoryTick(stack, world, entity, slot, selected)
    }

    override fun usageTick(world: World, user: LivingEntity, stack: ItemStack, remainingUseTicks: Int) {
        if (user.handSwingTicks in 1..<10) {
            val data =
                stack.getOrDefault(StarbornSoundscapeDataComponents.OVERARCHIEVER_DATA, OverarchieverData.DEFAULT)
            if (data.charge >= getOverarchieverUseCharge(stack, user)) {
                val newCharge = data.charge - getOverarchieverUseCharge(stack, user)
                useSong(stack, user, world)
                if (user is PlayerEntity) {
                    user.itemCooldownManager.set(stack.item, 5)
                }
                user.stopUsingItem()
                user.handSwingTicks = 11
                stack.set(StarbornSoundscapeDataComponents.OVERARCHIEVER_DATA, OverarchieverData(newCharge))
            }
        }

        val usedTicks = min(USE_TICKS - remainingUseTicks, getChargeTicks(user, stack))
        if (usedTicks == 19 || (usedTicks + 1) == (getChargeTicks(user, stack)) || usedTicks == getExtraFlareTicks(
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
                world.playSound(
                    null,
                    user.x,
                    user.y,
                    user.z,
                    SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME,
                    SoundCategory.PLAYERS,
                    6.0F,
                    1.0f
                )
            }
        }
        super.usageTick(world, user, stack, remainingUseTicks)
    }

    override fun onStoppedUsing(stack: ItemStack, world: World, user: LivingEntity, remainingUseTicks: Int) {
        val usedTickes = min(USE_TICKS - remainingUseTicks, getChargeTicks(user, stack))
        if (usedTickes >= MIN_TICKS_TO_FIRE) {
            fire(world, user, usedTickes, stack)
        }
        super.onStoppedUsing(stack, world, user, remainingUseTicks)
    }

    fun fire(world: World, user: LivingEntity, ticks: Int, stack: ItemStack) {
        if (isTestEnchantedTri(user, stack)) {
            fireTriBolts(world, user, ticks, stack)
        } else if (isTestEnchantedWell(user, stack)) {
            fireWellBolts(world, user, ticks, stack)
        } else if (isTestEnchantedGrizz(user, stack)) {
            fireSoManyFuckingBolts(world, user, ticks, stack)
        } else {
            val entity = CosmicBoltEntity(world, user)
            entity.setPosition(user.eyePos)
            setPropertiesTwo(entity, user.pitch, user.yaw, 0.0f, getLaunchVelocity(ticks, user, stack), 0.0f)
            entity.pickupType = PickupPermission.DISALLOWED
            entity.damage = baseDamage
            world.spawnEntity(entity)
        }
        world.playSoundFromEntity(user, SoundEvents.ITEM_CROSSBOW_SHOOT, SoundCategory.PLAYERS, 1.0f, 0.75f)
    }


    val TriDirectDamage = 7.0f
    val TriIndirectDamage = 5f
    fun fireTriBolts(world: World, user: LivingEntity, ticks: Int, stack: ItemStack) {
        val entity = CosmicBoltEntity(world, user)
        entity.setPosition(user.eyePos)
        setPropertiesTwo(entity, user.pitch, user.yaw, 0.0f, getLaunchVelocity(ticks, user, stack), 0.0f)
        entity.directDamage = TriDirectDamage
        entity.indirectDamage = TriIndirectDamage
        entity.pickupType = PickupPermission.DISALLOWED
        world.spawnEntity(entity)
        if (user.isOnGround) {
            val entity2 = CosmicBoltEntity(world, user)
            entity2.setPosition(user.eyePos)
            setPropertiesTwo(
                entity2,
                user.pitch,
                user.yaw + getAngleBetweenTriBolts(ticks),
                0.0f,
                getLaunchVelocity(ticks, user, stack),
                0.0f
            )
            entity2.directDamage = TriDirectDamage
            entity2.indirectDamage = TriIndirectDamage
            entity2.pickupType = PickupPermission.DISALLOWED
            world.spawnEntity(entity2)
            val entity3 = CosmicBoltEntity(world, user)
            entity3.setPosition(user.eyePos)
            setPropertiesTwo(
                entity3,
                user.pitch,
                user.yaw - getAngleBetweenTriBolts(ticks),
                0.0f,
                getLaunchVelocity(ticks, user, stack),
                0.0f
            )
            entity3.directDamage = TriDirectDamage
            entity3.indirectDamage = TriIndirectDamage
            entity3.pickupType = PickupPermission.DISALLOWED
            world.spawnEntity(entity3)
        } else {
            val entity2 = CosmicBoltEntity(world, user)
            entity2.setPosition(user.eyePos)
            setPropertiesTwo(
                entity2,
                user.pitch + getAngleBetweenTriBolts(ticks),
                user.yaw,
                0.0f,
                getLaunchVelocity(ticks, user, stack),
                0.0f
            )
            entity2.directDamage = TriDirectDamage
            entity2.indirectDamage = TriIndirectDamage
            entity2.pickupType = PickupPermission.DISALLOWED
            world.spawnEntity(entity2)
            val entity3 = CosmicBoltEntity(world, user)
            entity3.setPosition(user.eyePos)
            setPropertiesTwo(
                entity3,
                user.pitch - getAngleBetweenTriBolts(ticks),
                user.yaw,
                0.0f,
                getLaunchVelocity(ticks, user, stack),
                0.0f
            )
            entity3.directDamage = TriDirectDamage
            entity3.indirectDamage = TriIndirectDamage
            entity3.pickupType = PickupPermission.DISALLOWED
            world.spawnEntity(entity3)
        }
    }

    val WellDirectDamage = 4.5f
    val WellIndirectDamage = 4.0f
    fun fireWellBolts(world: World, user: LivingEntity, ticks: Int, stack: ItemStack) {
        val entity = CosmicBoltEntity(world, user)
        entity.setPosition(user.eyePos)
        setPropertiesTwo(entity, user.pitch, user.yaw, 0.0f, getLaunchVelocity(ticks, user, stack), 0.0f)
        entity.directDamage = WellDirectDamage
        entity.indirectDamage = WellIndirectDamage
        entity.pickupType = PickupPermission.DISALLOWED
        world.spawnEntity(entity)
        if (user.isOnGround) {
            val entity2 = CosmicBoltEntity(world, user)
            entity2.setPosition(user.eyePos)
            setPropertiesTwo(
                entity2,
                user.pitch,
                user.yaw + getAngleBetweenWellBolts(ticks),
                0.0f,
                getLaunchVelocity(ticks, user, stack),
                0.0f
            )
            entity2.directDamage = WellDirectDamage
            entity2.indirectDamage = WellIndirectDamage
            entity2.pickupType = PickupPermission.DISALLOWED
            world.spawnEntity(entity2)
            val entity3 = CosmicBoltEntity(world, user)
            entity3.setPosition(user.eyePos)
            setPropertiesTwo(
                entity3,
                user.pitch,
                user.yaw - getAngleBetweenWellBolts(ticks),
                0.0f,
                getLaunchVelocity(ticks, user, stack),
                0.0f
            )
            entity3.directDamage = WellDirectDamage
            entity3.indirectDamage = WellIndirectDamage
            entity3.pickupType = PickupPermission.DISALLOWED
            world.spawnEntity(entity3)
            val entity4 = CosmicBoltEntity(world, user)
            entity4.setPosition(user.eyePos)
            setPropertiesTwo(
                entity4,
                user.pitch,
                user.yaw + getAngleBetweenWellBolts(ticks).times(2),
                0.0f,
                getLaunchVelocity(ticks, user, stack),
                0.0f
            )
            entity4.directDamage = WellDirectDamage
            entity4.indirectDamage = WellIndirectDamage
            entity4.pickupType = PickupPermission.DISALLOWED
            world.spawnEntity(entity4)
            val entity5 = CosmicBoltEntity(world, user)
            entity5.setPosition(user.eyePos)
            setPropertiesTwo(
                entity5,
                user.pitch,
                user.yaw - getAngleBetweenWellBolts(ticks).times(2),
                0.0f,
                getLaunchVelocity(ticks, user, stack),
                0.0f
            )
            entity5.directDamage = WellDirectDamage
            entity5.indirectDamage = WellIndirectDamage
            entity5.pickupType = PickupPermission.DISALLOWED
            world.spawnEntity(entity5)
        } else {
            val entity2 = CosmicBoltEntity(world, user)
            entity2.setPosition(user.eyePos)
            setPropertiesTwo(
                entity2,
                user.pitch + getAngleBetweenWellBolts(ticks),
                user.yaw,
                0.0f,
                getLaunchVelocity(ticks, user, stack),
                0.0f
            )
            entity2.directDamage = WellDirectDamage
            entity2.indirectDamage = WellIndirectDamage
            entity2.pickupType = PickupPermission.DISALLOWED
            world.spawnEntity(entity2)
            val entity3 = CosmicBoltEntity(world, user)
            entity3.setPosition(user.eyePos)
            setPropertiesTwo(
                entity3,
                user.pitch - getAngleBetweenWellBolts(ticks),
                user.yaw,
                0.0f,
                getLaunchVelocity(ticks, user, stack),
                0.0f
            )
            entity3.directDamage = WellDirectDamage
            entity3.indirectDamage = WellIndirectDamage
            entity3.pickupType = PickupPermission.DISALLOWED
            world.spawnEntity(entity3)
            val entity4 = CosmicBoltEntity(world, user)
            entity4.setPosition(user.eyePos)
            setPropertiesTwo(
                entity4,
                user.pitch + getAngleBetweenWellBolts(ticks).times(2),
                user.yaw,
                0.0f,
                getLaunchVelocity(ticks, user, stack),
                0.0f
            )
            entity4.directDamage = WellDirectDamage
            entity4.indirectDamage = WellIndirectDamage
            entity4.pickupType = PickupPermission.DISALLOWED
            world.spawnEntity(entity4)
            val entity5 = CosmicBoltEntity(world, user)
            entity5.setPosition(user.eyePos)
            setPropertiesTwo(
                entity5,
                user.pitch - getAngleBetweenWellBolts(ticks).times(2),
                user.yaw,
                0.0f,
                getLaunchVelocity(ticks, user, stack),
                0.0f
            )
            entity5.directDamage = WellDirectDamage
            entity5.indirectDamage = WellIndirectDamage
            entity5.pickupType = PickupPermission.DISALLOWED
            world.spawnEntity(entity5)
        }
    }

    val GrizzDirectDamage = 0.2f
    val GrizzIndirectDamage = 5f
    fun fireSoManyFuckingBolts(world: World, user: LivingEntity, ticks: Int, stack: ItemStack) {
        repeat(18) {
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
            //entity.airResOnDrop = 0.5 + world.random.nextFloat().times(0.25)
            world.spawnEntity(entity)
        }
    }

    override fun getUseAction(stack: ItemStack): UseAction = UseAction.BOW

    override fun getUseTicks(stack: ItemStack, livingEntity: LivingEntity): Int = USE_TICKS

    fun isTestEnchantedTri(user: LivingEntity, stack: ItemStack): Boolean {
        return stack.hasEnchantment(StarbornSoundscapeEnchantments.TRI_THIS)
    }

    fun isTestEnchantedWell(user: LivingEntity, stack: ItemStack): Boolean {
        return stack.hasEnchantment(StarbornSoundscapeEnchantments.WELL_WELL_WELL)
    }

    fun isTestEnchantedGrizz(user: LivingEntity, stack: ItemStack): Boolean {
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

    override fun isItemBarVisible(stack: ItemStack): Boolean {
        return hasASongToSing(stack)
    }
}