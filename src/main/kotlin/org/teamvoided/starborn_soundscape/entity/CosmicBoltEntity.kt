package org.teamvoided.starborn_soundscape.entity

import com.ibm.icu.text.MessagePattern
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.entity.mob.EndermanEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.PersistentProjectileEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.network.packet.s2c.play.SoundPlayS2CPacket
import net.minecraft.particle.ParticleEffect
import net.minecraft.particle.ParticleTypes
import net.minecraft.registry.Holder
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import org.joml.Vector3f
import org.teamvoided.starborn_soundscape.entity.astra_stuff_dont_peep.StarProjectileEntity
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeDamageTypes
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeDamageTypes.customDamage
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeEffects
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeEntities
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeParticles
import org.teamvoided.starborn_soundscape.mixin.PersistentProjectileEntityAccessor
import org.teamvoided.starborn_soundscape.particle.AstralParticleOptions
import org.teamvoided.starborn_soundscape.util.sillyLightningTime

class CosmicBoltEntity : PersistentProjectileEntity {

    constructor(entityType: EntityType<out CosmicBoltEntity>, world: World) : super(entityType, world)

    constructor(world: World, owner: LivingEntity) : super(
        StarbornSoundscapeEntities.COSMIC_BOLT, owner, world, Items.ARROW.defaultStack, Items.STONE.defaultStack
    )

    constructor(x: Double, y: Double, z: Double, world: World) : super(
        StarbornSoundscapeEntities.COSMIC_BOLT, x, y, z, world, Items.ARROW.defaultStack, Items.STONE.defaultStack
    )

    var directDamage = 10f
    var indirectDamage = 10f
    var timeTillBoom = 20
    var explosionRadius = 1.75
    var tracerRound = false
    var fireRound = false
    var breakRound = false
    var sparkRound = false
    var cloudRound = false
    val sparkMult = 0.2f

    override fun onEntityHit(entityHitResult: EntityHitResult) {
        var colour = Vector3f(128f / 255f, 128f / 255f, 128f / 255f)
        if (entityHitResult.entity is LivingEntity) {
            val hit = entityHitResult.entity as LivingEntity

            if (hit is PlayerEntity && hit.blockedByShield(
                    this.damageSources.create(
                        StarbornSoundscapeDamageTypes.BOLT_DIRECT,
                        owner,
                        owner
                    )
                )
            ) if (breakRound) {
                hit.itemCooldownManager.set(Items.SHIELD, 40); hit.stopUsingItem()
                colour = Vector3f(0f / 255f, 120f / 255f, 5f / 255f)
            } else return
            if (hit.hasStatusEffect(StarbornSoundscapeEffects.BAND_APPROVED)) return
            hit.customDamage(
                StarbornSoundscapeDamageTypes.BOLT_DIRECT,
                directDamage,
                owner,
                owner
            )
            if (hit is EndermanEntity) return
            if (tracerRound) {
                hit.addStatusEffect(
                    StatusEffectInstance(
                        StatusEffects.GLOWING,
                        200, 0,
                        false, false, true
                    )
                )
                colour = Vector3f(255f / 255f, 255f / 255f, 255f / 255f)
            }
            if (fireRound) {
                hit.setOnFireFor(100)
                colour = Vector3f(255f / 255f, 134f / 255f, 0f / 255f)
            }
            if (sparkRound) {
                shockANearbyGuy(world, directDamage, sparkMult, hit)
                colour = Vector3f(255f / 255f, 255f / 255f, 255f / 255f)
            }
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
            if (this.owner != null) {
                if (this.owner is ServerPlayerEntity) {
                    (this.owner as ServerPlayerEntity).networkHandler.send(
                        SoundPlayS2CPacket(
                            Holder.createDirect(SoundEvents.BLOCK_END_PORTAL_FRAME_FILL),
                            SoundCategory.PLAYERS,
                            this.owner!!.x,
                            this.owner!!.y,
                            this.owner!!.z,
                            0.1F,
                            2.0f,
                            world.getRandom().nextLong()
                        )
                    )
                }
                if (world is ServerWorld) {
                    (world as ServerWorld).spawnParticles(
                        AstralParticleOptions(
                            colour,
                            Vector3f(0f, 0f, 0f),
                            5f,
                            false,
                            true,
                            true,
                            false,
                            0.2f,
                            20,
                            -0.0001f,
                            0.9f
                        ), this.x, this.y, this.z, 1, 0.0, 0.0, 0.0, 0.0
                    )
                }
            }
            if (this.owner != null && !hit.isAlive) {
                if (this.owner is ServerPlayerEntity) {
                    (this.owner as ServerPlayerEntity).networkHandler.send(
                        SoundPlayS2CPacket(
                            Holder.createDirect(SoundEvents.ENTITY_ARROW_HIT_PLAYER),
                            SoundCategory.PLAYERS,
                            this.owner!!.x,
                            this.owner!!.y,
                            this.owner!!.z,
                            0.1F,
                            2.0f,
                            world.getRandom().nextLong()
                        )
                    )
                }
                if (world is ServerWorld) {
                    (world as ServerWorld).spawnParticles(
                        AstralParticleOptions(
                            colour,
                            Vector3f(0.5f, 0.5f, 0.5f),
                            1.5f,
                            false,
                            true,
                            true,
                            false,
                            0.0f,
                            20,
                            0.5f,
                            0.3f
                        ), this.x, this.y, this.z, 5, 0.0, 0.0, 0.0, 0.0
                    )
                }
            }
        }
    }

    fun shockANearbyGuy(world: World, damage: Float, multiplier: Float, except: LivingEntity?) {
        val entities = mutableListOf<Entity>()
        entities.addAll(
            this.world.getOtherEntities(
                this.owner, Box(
                    this.x + 3,
                    this.y + 3,
                    this.z + 3,
                    this.x - 3,
                    this.y - 3,
                    this.z - 3
                )
            ).filter {
                it is LivingEntity && it.isAlive && it != this.owner && it != this && this.distanceTo(it) <= 10 && !it.hasStatusEffect(
                    StarbornSoundscapeEffects.BAND_APPROVED
                ) && it != except
            }
        )
        val entities2 = mutableListOf<LivingEntity>()
        if (entities.isNotEmpty()) {
            for (entity in entities) {
                if (entity is PlayerEntity) {
                    for (entiity in entities) {
                        if (entiity is PlayerEntity) {
                            entities2.add(entity)
                        }
                    }
                    break
                }
            }
            if (entities2.isNotEmpty()) {
                entities2.shuffle()
                val guy = entities2.first()
                if (world is ServerWorld) {
                    sillyLightningTime(this.pos, guy.eyePos, world, 1, 9, 5, 0.05f, 0.75)
                }
                guy.customDamage(
                    StarbornSoundscapeDamageTypes.SHOCKED,
                    damage * multiplier,
                    owner,
                    owner
                )
            } else {
                entities.shuffle()
                val guy = entities.first()
                if (world is ServerWorld) {
                    sillyLightningTime(this.pos, guy.eyePos, world, 1, 9, 5, 0.05f, 0.75)
                }
                guy.customDamage(
                    StarbornSoundscapeDamageTypes.SHOCKED,
                    damage * multiplier,
                    owner,
                    owner
                )
            }
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
            ).filter {
                it != this.owner && it is LivingEntity && this.distanceTo(it) <= explosionRadius && !it.hasStatusEffect(
                    StarbornSoundscapeEffects.BAND_APPROVED
                )
            }
            if (sparkRound) {
                shockANearbyGuy(world, indirectDamage, sparkMult, null)
                world.playSound(
                    null,
                    this.x,
                    this.y,
                    this.z,
                    SoundEvents.BLOCK_ENDER_CHEST_OPEN,
                    SoundCategory.PLAYERS,
                    1.0F,
                    1.5f + world.random.nextFloat().plus(-0.5f).times(0.5f),
                )
                this.discard()
                return
            } else if (cloudRound && this.owner is LivingEntity) {
                val cloud = ToxicCloudEntity(this.world, owner as LivingEntity)
                cloud.setPosition(this.eyePos)
                cloud.isSmall = true
                cloud.cloudLifespan = 100
                this.world.spawnEntity(cloud)
            }
            var hasPlayedSound = false
            for (entity in entities) {
                if (entity is PlayerEntity && entity.blockedByShield(
                        this.damageSources.create(
                            StarbornSoundscapeDamageTypes.BOLT_EXPLOSION,
                            owner,
                            owner
                        )
                    )
                ) if (breakRound) {
                    entity.itemCooldownManager.set(Items.SHIELD, 40); entity.stopUsingItem()
                } else return
                entity.customDamage(
                    StarbornSoundscapeDamageTypes.BOLT_EXPLOSION,
                    indirectDamage,
                    owner,
                    owner
                )
                if (tracerRound && entity is LivingEntity) {
                    entity.addStatusEffect(
                        StatusEffectInstance(
                            StatusEffects.GLOWING,
                            100, 0,
                            false, false, true
                        )
                    )
                }
                if (fireRound && entity is LivingEntity) {
                    entity.setOnFireFor(50)
                }
                if (this.owner != null && !hasPlayedSound) {
                    hasPlayedSound = true
                    if (this.owner is ServerPlayerEntity) {
                        (this.owner as ServerPlayerEntity).networkHandler.send(
                            SoundPlayS2CPacket(
                                Holder.createDirect(SoundEvents.ENTITY_ARROW_HIT_PLAYER),
                                SoundCategory.PLAYERS,
                                this.owner!!.x,
                                this.owner!!.y,
                                this.owner!!.z,
                                0.1F,
                                2.0f,
                                world.getRandom().nextLong()
                            )
                        )
                    }
                }
                if (this.owner != null && !entity.isAlive) {
                    hasPlayedSound = true
                    if (this.owner is ServerPlayerEntity) {
                        (this.owner as ServerPlayerEntity).networkHandler.send(
                            SoundPlayS2CPacket(
                                Holder.createDirect(SoundEvents.ENTITY_ARROW_HIT_PLAYER),
                                SoundCategory.PLAYERS,
                                this.owner!!.x,
                                this.owner!!.y,
                                this.owner!!.z,
                                0.1F,
                                0.5f,
                                world.getRandom().nextLong()
                            )
                        )
                    }
                }
            }
            if (this.world is ServerWorld) {
                val world = this.world as ServerWorld
                val particle = if (tracerRound) ParticleTypes.END_ROD else ParticleTypes.GLOW
                (world as ServerWorld).spawnParticles(
                    AstralParticleOptions(
                        Vector3f(1f, 1f, 1f),
                        Vector3f(01f, 01f, 1f),
                        1.5f,
                        false,
                        true,
                        true,
                        false,
                        0.0f,
                        20,
                        0.2f,
                        0.9f
                    ), this.x, this.y, this.z, 5, 0.0, 0.0, 0.0, 0.0
                )
                if (fireRound) {
                    world.spawnParticles(
                        ParticleTypes.FLAME,
                        this.x,
                        this.y,
                        this.z,
                        3,
                        0.0,
                        0.0,
                        0.0,
                        0.2
                    )
                }
                if (fireRound) {
                    world.spawnParticles(
                        ParticleTypes.CRIT,
                        this.x,
                        this.y,
                        this.z,
                        3,
                        0.0,
                        0.0,
                        0.0,
                        0.2
                    )
                }
                if (cloudRound) {
                    world.spawnParticles(
                        StarbornSoundscapeParticles.TOXIC_POOF,
                        this.x,
                        this.y,
                        this.z,
                        3,
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
                    SoundEvents.BLOCK_ENDER_CHEST_OPEN,
                    SoundCategory.PLAYERS,
                    1.0F,
                    1.5f + world.random.nextFloat().plus(-0.5f).times(0.5f),
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
            this.velocity = velocity
            this.velocityDirty = true
        }
        if (!this.inGround) {
            if (world is ServerWorld && tracerRound) (world as ServerWorld).spawnParticles(
                ParticleTypes.END_ROD, this.x, this.y, this.z,
                1,
                0.0, 0.0, 0.0,
                0.0
            )
            if (world is ServerWorld && fireRound) (world as ServerWorld).spawnParticles(
                ParticleTypes.FLAME, this.x, this.y, this.z,
                1,
                0.0, 0.0, 0.0,
                0.1
            )
            if (world is ServerWorld && breakRound) (world as ServerWorld).spawnParticles(
                ParticleTypes.CRIT, this.x, this.y, this.z,
                1,
                0.0, 0.0, 0.0,
                0.0
            )
            if (world is ServerWorld && cloudRound) (world as ServerWorld).spawnParticles(
                StarbornSoundscapeParticles.TOXIC_POOF, this.x, this.y, this.z,
                1,
                0.0, 0.0, 0.0,
                0.0
            )
        }
        if (world is ServerWorld && sparkRound) {
            sillyLightningTime(
                Vec3d(this.pos.x, this.pos.y, this.pos.z),
                Vec3d(
                    this.x + (world.random.nextDouble().minus(0.5) * 3),
                    this.y + (world.random.nextDouble().minus(0.5) * 3),
                    this.z + (world.random.nextDouble().minus(0.5) * 3)
                ), world as ServerWorld, 1, 9, 2, 0.01f, 0.75
            )
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