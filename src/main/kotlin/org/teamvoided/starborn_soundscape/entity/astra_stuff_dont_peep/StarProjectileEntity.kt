package org.teamvoided.starborn_soundscape.entity.astra_stuff_dont_peep

import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.projectile.PersistentProjectileEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.world.World
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeDamageTypes
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeDamageTypes.customDamage
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeEntities
import kotlin.math.sign

class StarProjectileEntity : PersistentProjectileEntity {

    constructor(entityType: EntityType<out StarProjectileEntity>, world: World) : super(entityType, world)

    constructor(world: World, owner: LivingEntity) : super(
        StarbornSoundscapeEntities.STAR_PROJECTILE, owner, world, Items.ARROW.defaultStack, Items.STONE.defaultStack
    )

    constructor(x: Double, y: Double, z: Double, world: World) : super(
        StarbornSoundscapeEntities.STAR_PROJECTILE, x, y, z, world, Items.ARROW.defaultStack, Items.STONE.defaultStack
    )

    override fun hasNoGravity(): Boolean {
        return true
    }

    val ticksTillTrack = 20
    var trackingEntity: LivingEntity? = null
    val dmg = 3
    var isInTrackingState = false
    var lifetimeTicks = 200
    var accelerationMult = 0.05

    override fun tick() {
        if (this.age == ticksTillTrack) {
            isInTrackingState = true
        }
        if (this.age > ticksTillTrack) {
            if (trackingEntity == null) {
                isInTrackingState = false
            }
            else if (!trackingEntity!!.isAlive) {
                lifetimeTicks -= 1
            }
            if (isInTrackingState) {
                val pos = this.pos
                val targetPos = trackingEntity!!.eyePos
                val x = (targetPos.x - pos.x).sign
                val y = (targetPos.y - pos.y).sign
                val z = (targetPos.z - pos.z).sign
                this.addVelocity(x * accelerationMult, y * accelerationMult, z * accelerationMult)
                this.velocityModified = true
            }
        }
        accelerationMult += 0.001
        if (this.world is ServerWorld) {
            (this.world as ServerWorld).spawnParticles(
                ParticleTypes.GLOW,
                this.x,
                this.y,
                this.z,
                1,
                0.0,
                0.0,
                0.0,
                0.0
            )
        }
        if (this.age > lifetimeTicks) {
            if (this.world is ServerWorld) {
                (this.world as ServerWorld).spawnParticles(
                    ParticleTypes.GLOW,
                    this.x,
                    this.y,
                    this.z,
                    5,
                    0.0,
                    0.0,
                    0.0,
                    0.2
                )
            }
            world.playSound(
                null,
                this.x,
                this.y,
                this.z,
                SoundEvents.BLOCK_GLASS_BREAK,
                SoundCategory.PLAYERS,
                1.0F,
                1.5f + world.random.nextFloat().plus(-0.5f).times(0.1f),
            )
            this.discard()
        }
        super.tick()
    }

    override fun onBlockHit(blockHitResult: BlockHitResult?) {
        if (this.world is ServerWorld) {
            (this.world as ServerWorld).spawnParticles(
                ParticleTypes.GLOW,
                this.x,
                this.y,
                this.z,
                5,
                0.0,
                0.0,
                0.0,
                0.2
            )
        }
        world.playSound(
            null,
            this.x,
            this.y,
            this.z,
            SoundEvents.BLOCK_GLASS_BREAK,
            SoundCategory.PLAYERS,
            1.0F,
            0.25f + world.random.nextFloat().plus(-0.1f).times(0.1f),
        )
        this.discard()
        super.onBlockHit(blockHitResult)
    }

    override fun onEntityHit(entityHitResult: EntityHitResult) {
        val hit = entityHitResult.entity
        if (hit is LivingEntity && hit != this.owner){
            hit.customDamage(
                StarbornSoundscapeDamageTypes.STARSTRUCK,
                3f,
                owner,
                owner
            )
            world.playSound(
                null,
                this.x,
                this.y,
                this.z,
                SoundEvents.BLOCK_AMETHYST_BLOCK_BREAK,
                SoundCategory.PLAYERS,
                1.0F,
                2f,
            )
            isInTrackingState = false
        }
    }

    override fun getDefaultItemStack(): ItemStack = Items.AIR.defaultStack
}