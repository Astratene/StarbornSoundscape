package org.teamvoided.starborn_soundscape.entity

import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.mob.EndermanEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.PersistentProjectileEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.util.math.Box
import net.minecraft.world.World
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeDamageTypes
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeDamageTypes.customDamage
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeEntities
import org.teamvoided.starborn_soundscape.mixin.PersistentProjectileEntityAccessor

class CosmicBoltEntity : PersistentProjectileEntity {

    constructor(entityType: EntityType<out CosmicBoltEntity>, world: World) : super(entityType, world)

    constructor(world: World, owner: LivingEntity) : super(
        StarbornSoundscapeEntities.COSMIC_BOLT, owner, world, Items.ARROW.defaultStack, Items.STONE.defaultStack
    )

    constructor(x: Double, y: Double, z: Double, world: World) : super(
        StarbornSoundscapeEntities.COSMIC_BOLT, x, y, z, world, Items.ARROW.defaultStack, Items.STONE.defaultStack
    )

    var directDamage = 10f
    var indirectDamage = 5f
    var timeTillBoom = 20
    var explosionRadius = 1.75

    override fun onEntityHit(entityHitResult: EntityHitResult) {
        if (entityHitResult.entity is LivingEntity) {
            val hit = entityHitResult.entity as LivingEntity
            val mult = if (hit is PlayerEntity) 1f else 1f
            hit.customDamage(
                StarbornSoundscapeDamageTypes.BOLT_DIRECT,
                directDamage,
                owner,
                owner
            )
            if (hit is EndermanEntity) return
            if (world is ServerWorld) {
                this.world.playSound(
                    null,
                    this.pos.x,
                    this.pos.y,
                    this.pos.z,
                    SoundEvents.ITEM_TRIDENT_HIT,
                    SoundCategory.PLAYERS,
                    1.0F,
                    1.0f
                )
            }
            //this.discard()
        }
    }

    override fun age() {
        super.age()
        if ((this as PersistentProjectileEntityAccessor).life() >= timeTillBoom) {
            val entities = world.getOtherEntities(
                null, Box(
                    pos.x + explosionRadius,
                    pos.y + explosionRadius,
                    pos.z + explosionRadius,
                    pos.x - explosionRadius,
                    pos.y - explosionRadius,
                    pos.z - explosionRadius
                )
            ).filter { it != this.owner && it is LivingEntity && this.distanceTo(it) <= explosionRadius }
            for (entity in entities) {
                entity.customDamage(
                    StarbornSoundscapeDamageTypes.BOLT_EXPLOSION,
                    indirectDamage,
                    owner,
                    owner
                )
            }
            if (this.world is ServerWorld) {
                val world = this.world as ServerWorld
                world.spawnParticles(
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
                world.playSound(
                    null,
                    this.x,
                    this.y,
                    this.z,
                    SoundEvents.BLOCK_ENDER_CHEST_OPEN,
                    SoundCategory.PLAYERS,
                    1.0F,
                    1.5f
                )
                this.discard()
            }
        }
    }

    override fun initDataTracker(builder: DataTracker.Builder) {
        super.initDataTracker(builder)
    }

    // now its physics time!

    override fun hasNoGravity(): Boolean {
        return true
    }

    var ticksTillDrop = 5
    var airResOnDrop = 0.7
    var gravityOnDrop = -0.5

    override fun tick() {
        if (ticksTillDrop > 0) ticksTillDrop--
        else if (!this.inGround) {
            var velocity = this.velocity
            velocity = velocity.multiply(airResOnDrop, 1.0, airResOnDrop)
            velocity = velocity.add(0.0, gravityOnDrop, 0.0)
            println(velocity)
            this.velocity = velocity
            this.velocityDirty = true
        }
        super.tick()
    }

    override fun onBlockHit(blockHitResult: BlockHitResult) {
        super.onBlockHit(blockHitResult)
        this.sound = getHitSound()
        this.shake = 0
    }

    override fun getDefaultItemStack(): ItemStack = Items.AIR.defaultStack
    override fun getHitSound(): SoundEvent = SoundEvents.ITEM_TRIDENT_HIT_GROUND

}