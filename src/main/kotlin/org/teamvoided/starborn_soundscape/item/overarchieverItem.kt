package org.teamvoided.starborn_soundscape.item

import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.PersistentProjectileEntity.PickupPermission
import net.minecraft.item.Item
import net.minecraft.item.Item.Settings
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
import org.joml.Math.lerp
import org.teamvoided.starborn_soundscape.entity.CosmicBoltEntity
import org.teamvoided.starborn_soundscape.util.setPropertiesTwo
import kotlin.math.max
import kotlin.math.min

class overarchieverItem(settings: Settings) : Item(settings) {

    override fun use(world: World, player: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        player.setCurrentHand(hand)
        return TypedActionResult(ActionResult.CONSUME_PARTIAL, player.getStackInHand(hand))
    }

    fun getChargeTicks(): Int {
        return if (isTestEnchantedTri) 40 else if (isTestEnchantedWell) 60 else if (isTestEnchantedGrizz) 80 else 20 // will change based on enchantments
    }
    fun getExtraFlareTicks(): Int {
        return if (isTestEnchantedGrizz) 40 else -1
    }

    fun getAngleBetweenTriBolts(ticks: Int): Float {
        return (20 - (ticks - 20)).plus(2).toFloat()
    }

    fun getAngleBetweenWellBolts(ticks: Int): Float {
        return (ticks - 20).times(0.2f)
    }

    fun getMaxSpread(ticks: Int): Float {
        return 20 - (0.375f * max(ticks - 40, 0))
    }

    fun getLaunchVelocity(ticks: Int): Float {
        return (ticks / getChargeTicks().toFloat()).times(5f)
    }

    override fun usageTick(world: World, user: LivingEntity, stack: ItemStack?, remainingUseTicks: Int) {
        val usedTicks = min(USE_TICKS - remainingUseTicks, getChargeTicks())
        if (usedTicks == 20 || usedTicks == (getChargeTicks() -1 )|| usedTicks == getExtraFlareTicks()){
            val vec3d: Vec3d = user.getLerpedEyePos(1f)
            val vec3d2: Vec3d = user.getRotationVec(1f)
            val vec3d3 = vec3d.add(vec3d2.x * 1, vec3d2.y * 1, vec3d2.z * 1)
            if (world is ServerWorld){
                world.spawnParticles(
                    ParticleTypes.GLOW,
                    vec3d3.x,
                    vec3d3.y - 0.5,
                    vec3d3.z,
                    5,
                    0.0,
                    0.0,
                    0.0,
                    0.2
                )
                world.playSound(null,
                    user.x,
                    user.y,
                    user.z,
                    SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME,
                    SoundCategory.PLAYERS,
                    6.0F,
                    1.0f)
            }
        }
        super.usageTick(world, user, stack, remainingUseTicks)
    }

    override fun onStoppedUsing(stack: ItemStack?, world: World, user: LivingEntity, remainingUseTicks: Int) {
        val usedTickes = min(USE_TICKS - remainingUseTicks, getChargeTicks())
        if (usedTickes >= MIN_TICKS_TO_FIRE) {
            fire(world, user, usedTickes)
        }
        super.onStoppedUsing(stack, world, user, remainingUseTicks)
    }

    fun fire(world: World, user: LivingEntity, ticks: Int) {
        if (isTestEnchantedTri) {
            fireTriBolts(world, user, ticks)
        } else if (isTestEnchantedWell) {
            fireWellBolts(world, user, ticks)
        } else if (isTestEnchantedGrizz) {
            fireSoManyFuckingBolts(world, user, ticks)
        } else {
            val entity = CosmicBoltEntity(world, user)
            entity.setPosition(user.eyePos)
            setPropertiesTwo(entity, user.pitch, user.yaw, 0.0f, getLaunchVelocity(ticks), 0.0f)
            entity.pickupType = PickupPermission.DISALLOWED
            world.spawnEntity(entity)
        }
    }

    fun fireTriBolts(world: World, user: LivingEntity, ticks: Int) {
        val entity = CosmicBoltEntity(world, user)
        entity.setPosition(user.eyePos)
        setPropertiesTwo(entity, user.pitch, user.yaw, 0.0f, getLaunchVelocity(ticks), 0.0f)
        entity.directDamage = 5f
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
                getLaunchVelocity(ticks),
                0.0f
            )
            entity2.directDamage = 5f
            entity2.pickupType = PickupPermission.DISALLOWED
            world.spawnEntity(entity2)
            val entity3 = CosmicBoltEntity(world, user)
            entity3.setPosition(user.eyePos)
            setPropertiesTwo(
                entity3,
                user.pitch,
                user.yaw - getAngleBetweenTriBolts(ticks),
                0.0f,
                getLaunchVelocity(ticks),
                0.0f
            )
            entity3.directDamage = 5f
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
                getLaunchVelocity(ticks),
                0.0f
            )
            entity2.directDamage = 5f
            entity2.pickupType = PickupPermission.DISALLOWED
            world.spawnEntity(entity2)
            val entity3 = CosmicBoltEntity(world, user)
            entity3.setPosition(user.eyePos)
            setPropertiesTwo(
                entity3,
                user.pitch - getAngleBetweenTriBolts(ticks),
                user.yaw,
                0.0f,
                getLaunchVelocity(ticks),
                0.0f
            )
            entity3.directDamage = 5f
            entity3.pickupType = PickupPermission.DISALLOWED
            world.spawnEntity(entity3)
        }
    }

    fun fireWellBolts(world: World, user: LivingEntity, ticks: Int) {
        val entity = CosmicBoltEntity(world, user)
        entity.setPosition(user.eyePos)
        setPropertiesTwo(entity, user.pitch, user.yaw, 0.0f, getLaunchVelocity(ticks), 0.0f)
        entity.directDamage = 2.5f
        entity.indirectDamage = 2.5f
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
                getLaunchVelocity(ticks),
                0.0f
            )
            entity2.directDamage = 2.5f
            entity2.indirectDamage = 2.5f
            entity2.pickupType = PickupPermission.DISALLOWED
            world.spawnEntity(entity2)
            val entity3 = CosmicBoltEntity(world, user)
            entity3.setPosition(user.eyePos)
            setPropertiesTwo(
                entity3,
                user.pitch,
                user.yaw - getAngleBetweenWellBolts(ticks),
                0.0f,
                getLaunchVelocity(ticks),
                0.0f
            )
            entity3.directDamage = 2.5f
            entity3.indirectDamage = 2.5f
            entity3.pickupType = PickupPermission.DISALLOWED
            world.spawnEntity(entity3)
            val entity4 = CosmicBoltEntity(world, user)
            entity4.setPosition(user.eyePos)
            setPropertiesTwo(
                entity4,
                user.pitch,
                user.yaw + getAngleBetweenWellBolts(ticks).times(2),
                0.0f,
                getLaunchVelocity(ticks),
                0.0f
            )
            entity4.directDamage = 2.5f
            entity4.indirectDamage = 2.5f
            entity4.pickupType = PickupPermission.DISALLOWED
            world.spawnEntity(entity4)
            val entity5 = CosmicBoltEntity(world, user)
            entity5.setPosition(user.eyePos)
            setPropertiesTwo(
                entity5,
                user.pitch,
                user.yaw - getAngleBetweenWellBolts(ticks).times(2),
                0.0f,
                getLaunchVelocity(ticks),
                0.0f
            )
            entity5.directDamage = 2.5f
            entity5.indirectDamage = 2.5f
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
                getLaunchVelocity(ticks),
                0.0f
            )
            entity2.directDamage = 2.5f
            entity2.indirectDamage = 2.5f
            entity2.pickupType = PickupPermission.DISALLOWED
            world.spawnEntity(entity2)
            val entity3 = CosmicBoltEntity(world, user)
            entity3.setPosition(user.eyePos)
            setPropertiesTwo(
                entity3,
                user.pitch - getAngleBetweenWellBolts(ticks),
                user.yaw,
                0.0f,
                getLaunchVelocity(ticks),
                0.0f
            )
            entity3.directDamage = 2.5f
            entity3.indirectDamage = 2.5f
            entity3.pickupType = PickupPermission.DISALLOWED
            world.spawnEntity(entity3)
            val entity4 = CosmicBoltEntity(world, user)
            entity4.setPosition(user.eyePos)
            setPropertiesTwo(
                entity4,
                user.pitch + getAngleBetweenWellBolts(ticks).times(2),
                user.yaw,
                0.0f,
                getLaunchVelocity(ticks),
                0.0f
            )
            entity4.directDamage = 2.5f
            entity4.indirectDamage = 2.5f
            entity4.pickupType = PickupPermission.DISALLOWED
            world.spawnEntity(entity4)
            val entity5 = CosmicBoltEntity(world, user)
            entity5.setPosition(user.eyePos)
            setPropertiesTwo(
                entity5,
                user.pitch - getAngleBetweenWellBolts(ticks).times(2),
                user.yaw,
                0.0f,
                getLaunchVelocity(ticks),
                0.0f
            )
            entity5.directDamage = 2.5f
            entity5.indirectDamage = 2.5f
            entity5.pickupType = PickupPermission.DISALLOWED
            world.spawnEntity(entity5)
        }
    }

    fun fireSoManyFuckingBolts(world: World, user: LivingEntity, ticks: Int) {
        repeat(9) {
            val entity = CosmicBoltEntity(world, user)
            entity.setPosition(user.eyePos)
            setPropertiesTwo(entity, user.pitch, user.yaw, 0.0f, getLaunchVelocity(ticks), getMaxSpread(ticks))
            entity.directDamage = 2f
            entity.indirectDamage = 2f
            entity.timeTillBoom = 20 + world.random.range(-5, 5)
            entity.pickupType = PickupPermission.DISALLOWED
            //entity.airResOnDrop = 0.5 + world.random.nextFloat().times(0.25)
            world.spawnEntity(entity)
        }
    }

    override fun getUseAction(stack: ItemStack): UseAction = UseAction.BLOCK

    override fun getUseTicks(stack: ItemStack, livingEntity: LivingEntity): Int = USE_TICKS

    val isTestEnchantedTri = false
    val isTestEnchantedWell = false
    val isTestEnchantedGrizz = true


    companion object {
        const val USE_TICKS = 72000
        const val MIN_TICKS_TO_FIRE = 20
    }
}