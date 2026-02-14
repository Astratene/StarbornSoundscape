package org.teamvoided.starborn_soundscape.entity

import net.minecraft.block.Blocks
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.MovementType
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.entity.projectile.PersistentProjectileEntity
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.NbtCompound
import net.minecraft.particle.BlockStateParticleEffect
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import org.joml.Math.lerp
import org.joml.Vector3f
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeDamageTypes
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeDamageTypes.customDamage
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeEntities
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeEntities.BIG_SPEAKER
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeSounds
import java.util.UUID
import kotlin.math.roundToInt

class BigSpeakerEntity : Entity {

    var owner: LivingEntity? = null
    var ownerUuid: UUID? = null

    constructor(entityType: EntityType<out BigSpeakerEntity?>?, world: World?) :
            super(entityType as EntityType<out Entity?>?, world)

    constructor(world: World?, owner: LivingEntity?) :
            super(BIG_SPEAKER as EntityType<out Entity?>, world) {
        this.owner = owner
    }

    override fun hasNoGravity(): Boolean {
        return false
    }

    val damageRadius = 5.0
    val damageRange = 300.0
    val damage = 10f
    var preFireTicks = 50
    var firingTicks = 220
    var disipationTicks = 15
    var isStillOnGround = true
    val hitEntities = mutableListOf<LivingEntity>()

    override fun tick() {
        faceBeam()
        this.applyGravity()
        this.move(MovementType.SELF, this.velocity)
        if (owner == null && ownerUuid != null && world is ServerWorld) {
            owner = (world as ServerWorld).getEntity(ownerUuid!!) as? LivingEntity
        }
        if (!world.isClient && owner == null) {
            discard()
            return
        }

        super.tick()
        if (!isStillOnGround) {
            if (this.owner != null) {
                if (preFireTicks > 0) {
                    preFireTicks--
                    sendOutParticleBeam2(damageRadius, this, damageRange)
                    //this.yaw = owner!!.yaw
                    //this.pitch = owner!!.pitch
                    if (preFireTicks == 0) {
                        sendOutParticleBeam(damageRadius, this, 300.0, 11.0, 1)
                        sendOutParticleBeam3(damageRadius, this, 11.0, 0.0, 1)
                    }
                } else if (firingTicks > 0) {
                    dealDamageToEntitiesInBeam()
                    firingTicks--
                    val random = random.nextFloat().plus(-0.5f)
                    sendOutParticleBeam(damageRadius + random, this, 300.0, 11.0, 1)
                    sendOutParticleBeam3(damageRadius + random, this, 11.0, 0.0, 1)
                } else if (disipationTicks > 0) {
                    disipationTicks--
                } else {
                    if (!this.world.isClient) {
                        val serverWorld = this.world as ServerWorld
                        serverWorld.spawnParticles(
                            ParticleTypes.GLOW,
                            this.x,
                            this.y + 0.1,
                            this.z,
                            200,
                            1.0,
                            1.5,
                            1.0,
                            0.0
                        )
                    }
                    playDeactivationSound()
                    discard()
                }
            }
        }
        if (!this.isOnGround) {
            this.velocity = this.velocity.add(0.0, -0.1, 0.0)
            this.velocityDirty = true
            if (isStillOnGround) {
                dealDamageToEntitiesInSpeaker(10f, world)
            }
        } else {
            this.velocity = Vec3d.ZERO
            this.velocityDirty = true
            if (isStillOnGround) {
                HitGround(world)
                isStillOnGround = false
                dealDamageToEntitiesInSpeaker(25f, world)
            }
        }
    }
    fun playDeactivationSound(){
        this.world.playSound(
            null,
            this.x,
            this.y,
            this.z,
            SoundEvents.BLOCK_VAULT_CLOSE_SHUTTER,
            SoundCategory.PLAYERS,
            1.0f,
            0.5f
        )
    }

    fun HitGround(world: World) {
        if (world is ServerWorld) {
            world.spawnParticles(
                ParticleTypes.ELECTRIC_SPARK,
                this.x,
                this.y,
                this.z,
                100,
                3.0,
                0.0,
                3.0,
                0.5
            )
        }
        world.playSoundFromEntity(this, SoundEvents.ITEM_MACE_SMASH_GROUND_HEAVY, SoundCategory.PLAYERS, 1.0f, 1.0f)
        world.playSoundFromEntity(this, StarbornSoundscapeSounds.SOUND_SO_LOUD_IT_KILLS_YA, SoundCategory.PLAYERS, 10.0f, 1.0f)
    }

    fun hitAir(world: World) {
        if (world is ServerWorld) {
            world.spawnParticles(
                ParticleTypes.ELECTRIC_SPARK,
                this.x,
                this.y,
                this.z,
                100,
                1.0,
                0.0,
                1.0,
                0.5
            )
        }
        world.playSoundFromEntity(this, SoundEvents.ITEM_MACE_SMASH_AIR, SoundCategory.PLAYERS, 1.0f, 1.0f)
    }

    fun dealDamageToEntitiesInBeam() {
        val entities = collectEntitiesInBeamWithMinPos(damageRadius, this, damageRange, 6.0)
        for (entity in entities) {
            entity.customDamage(
                StarbornSoundscapeDamageTypes.BIG_SOUNDWAVES,
                damage,
                owner,
                owner
            )
        }
    }
    fun dealDamageToEntitiesInSpeaker(damage: Float, world: World) {
        val entities = mutableListOf<Entity>()
        entities.addAll(
            world.getOtherEntities(
                this, Box(
                    this.pos.x + 2.0,
                    this.pos.y + 3.0,
                    this.pos.z + 2.0,
                    this.pos.x - 2.0,
                    this.pos.y - 1.0,
                    this.pos.z - 2.0
                )
            ).filter { it is LivingEntity && it != this.owner && (!hitEntities.contains(it) || damage > 15f) }
        )
        for (entity in entities) {
            entity.customDamage(
                StarbornSoundscapeDamageTypes.CRUSHED,
                damage,
                owner,
                owner
            )
            hitEntities.add(entity as LivingEntity)
        }
        if (entities.isNotEmpty()) {
            this.velocity = Vec3d.ZERO
            hitAir(world)
        }
    }

    fun collectEntitiesInBeamWithMinPos(size: Double, caster: BigSpeakerEntity, length: Double, minPos: Double): MutableList<LivingEntity> {
        val entities = mutableListOf<Entity>()
        val endPos = caster.eyePos.add(caster.rotationVector.multiply(length)).add(0.0, 1.0, 0.0)
        val startPos = caster.eyePos.add(caster.rotationVector.multiply(minPos)).add(0.0, 1.0, 0.0)
        val interval = length / size
        for (i in 0..interval.roundToInt()) {
            entities.addAll(
                world.getOtherEntities(
                    caster, Box(
                        (lerp(startPos.x, endPos.x, i / interval)) + size,
                        (lerp(startPos.y + 2 - size, endPos.y, i / interval)) + size,
                        (lerp(startPos.z, endPos.z, i / interval)) + size,
                        (lerp(startPos.x, endPos.x, i / interval)) - size,
                        (lerp(startPos.y + 2 - size, endPos.y, i / interval)) - size,
                        (lerp(startPos.z, endPos.z, i / interval)) - size
                    )
                ).filter { it is LivingEntity && it != this.owner }
            )
        }
        return entities as MutableList<LivingEntity>
    }

    fun sendOutParticleBeam(size: Double, caster: BigSpeakerEntity, length: Double, startLength: Double, ticks: Int) {
        val endPos = caster.pos.add(caster.rotationVector.multiply(length)).add(0.0, 1.0, 0.0)
        val startPos = caster.pos.add(caster.rotationVector.multiply(startLength)).add(0.0, 1.0, 0.0)

        val beamRenderer = BeamRendererEntity(world, startPos.x, startPos.y, startPos.z)
        beamRenderer.dataTracker.set(BeamRendererEntity.OuterColour, 0x005d3e96)
        beamRenderer.dataTracker.set(BeamRendererEntity.InterColour, 0x002b99ca)
        beamRenderer.dataTracker.set(BeamRendererEntity.LiveTime, ticks)
        beamRenderer.dataTracker.set(BeamRendererEntity.ShrinkTime, 0)
        beamRenderer.dataTracker.set(BeamRendererEntity.TargetPos, endPos.toVector3f())
        beamRenderer.dataTracker.set(
            BeamRendererEntity.OriginPos,
            Vector3f(startPos.x.toFloat(), (startPos.y).toFloat(), startPos.z.toFloat())
        )
        beamRenderer.dataTracker.set(BeamRendererEntity.OuterThickness, size.toFloat())
        beamRenderer.dataTracker.set(BeamRendererEntity.MaxOuterThickness, size.toFloat())
        beamRenderer.dataTracker.set(BeamRendererEntity.InnerCubes, 3)
        beamRenderer.setPosition(startPos)
        world.spawnEntity(beamRenderer)
    }

    fun sendOutParticleBeam3(size: Double, caster: BigSpeakerEntity, length: Double, startLength: Double, ticks: Int) {
        val endPos = caster.pos.add(caster.rotationVector.multiply(length)).add(0.0, 1.0, 0.0)
        val startPos = caster.pos.add(caster.rotationVector.multiply(startLength)).add(0.0, 1.0, 0.0)

        val coneRenderer = ConeRendererEntity(world, caster.x, caster.y, caster.z)
        coneRenderer.dataTracker.set(ConeRendererEntity.OuterColour, 0x005d3e96)
        coneRenderer.dataTracker.set(ConeRendererEntity.InterColour, 0x002b99ca)
        coneRenderer.dataTracker.set(ConeRendererEntity.LiveTime, ticks)
        coneRenderer.dataTracker.set(ConeRendererEntity.ShrinkTime, 0)
        coneRenderer.dataTracker.set(ConeRendererEntity.TargetPos, endPos.toVector3f())
        coneRenderer.dataTracker.set(
            ConeRendererEntity.OriginPos,
            Vector3f(caster.x.toFloat(), (caster.y + 1).toFloat(), caster.z.toFloat())
        )
        coneRenderer.dataTracker.set(ConeRendererEntity.OuterThickness, 0.75f)
        coneRenderer.dataTracker.set(ConeRendererEntity.MaxOuterThickness, 0.75f)
        coneRenderer.dataTracker.set(ConeRendererEntity.InnerCubes, 3)
        coneRenderer.setPosition(startPos)
        coneRenderer.dataTracker.set(ConeRendererEntity.EndSize, size.toFloat() - 0.75f)
        coneRenderer.dataTracker.set(ConeRendererEntity.MaxEndSize, size.toFloat() - 0.75f)
        world.spawnEntity(coneRenderer)
    }

    fun sendOutParticleBeam2(size: Double, caster: BigSpeakerEntity, length: Double) {
        val endPos = caster.eyePos.add(caster.rotationVector.multiply(length))
        val interval = length / size
        for (i in 0..interval.roundToInt()) {
            if (!this.world.isClient) {
                val serverWorld = this.world as ServerWorld
                serverWorld.spawnParticles(
                    ParticleTypes.ELECTRIC_SPARK,
                    (lerp(this.eyePos.x, endPos.x, i / interval)),
                    (lerp(this.pos.y + 1, endPos.y + 1, i / interval)),
                    (lerp(this.eyePos.z, endPos.z, i / interval)),
                    10,
                    0.3,
                    0.3,
                    0.3,
                    0.0
                )
            }
        }
    }

    fun faceBeam() {

        dataTracker.set(TRACKED_YAW, yaw)
        dataTracker.set(TRACKED_PITCH, pitch)

    }

    override fun initDataTracker(builder: DataTracker.Builder) {
        builder.add(TRACKED_PITCH, 0f)
        builder.add(TRACKED_YAW, 0f)
    }

    companion object {
        val TRACKED_PITCH: TrackedData<Float> =
            DataTracker.registerData(BigSpeakerEntity::class.java, TrackedDataHandlerRegistry.FLOAT)
        val TRACKED_YAW: TrackedData<Float> =
            DataTracker.registerData(BigSpeakerEntity::class.java, TrackedDataHandlerRegistry.FLOAT)
    }

    override fun readCustomDataFromNbt(nbt: NbtCompound?) {
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound?) {
    }

    override fun isCollidable(): Boolean {
        return true
    }
}