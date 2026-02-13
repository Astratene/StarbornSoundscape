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
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import org.joml.Math.lerp
import org.joml.Vector3f
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeDamageTypes
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeDamageTypes.customDamage
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeEntities
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeEntities.BIG_SPEAKER
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
    val damageRange = 1000.0
    val damage = 5f
    var preFireTicks = 20
    var firingTicks = 200
    var disipationTicks = 20
    var isStillOnGround = true

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
                        sendOutParticleBeam(damageRadius, this, 100.0, 11.0)
                        sendOutParticleBeam3(damageRadius, this, 10.0, 1.0)
                    }
                }
                if (preFireTicks < 1 && firingTicks > 0) {
                    dealDamageToEntitiesInBeam()
                    firingTicks--
                } else if (disipationTicks > 0) {
                    disipationTicks--
                } else discard()
            }
        }
        if (!this.isOnGround) {
            this.velocity = this.velocity.add(0.0, -0.1, 0.0)
            this.velocityDirty = true
        } else {
            this.velocity = Vec3d.ZERO
            this.velocityDirty = true
            if (isStillOnGround) {
                HitGround(world)
                isStillOnGround = false
            }
        }
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
    }

    fun dealDamageToEntitiesInBeam() {
        val entities = collectEntitiesInBeam(damageRadius, this, damageRange)
        for (entity in entities) {
            entity.customDamage(
                StarbornSoundscapeDamageTypes.BIG_SOUNDWAVES,
                damage,
                owner,
                owner
            )
        }
    }

    fun collectEntitiesInBeam(size: Double, caster: BigSpeakerEntity, length: Double): MutableList<LivingEntity> {
        val entities = mutableListOf<Entity>()
        val endPos = caster.eyePos.add(caster.rotationVector.multiply(length))
        val interval = length / size
        for (i in 0..interval.roundToInt()) {
            entities.addAll(
                world.getOtherEntities(
                    caster, Box(
                        (lerp(caster.eyePos.x, endPos.x, i / interval)) + size,
                        (lerp(caster.eyePos.y - size, endPos.y, i / interval)) + size,
                        (lerp(caster.eyePos.z, endPos.z, i / interval)) + size,
                        (lerp(caster.eyePos.x, endPos.x, i / interval)) - size,
                        (lerp(caster.eyePos.y - size, endPos.y, i / interval)) - size,
                        (lerp(caster.eyePos.z, endPos.z, i / interval)) - size
                    )
                ).filter { it is LivingEntity && it != this.owner }
            )
        }
        return entities as MutableList<LivingEntity>
    }

    fun sendOutParticleBeam(size: Double, caster: BigSpeakerEntity, length: Double, startLength: Double) {
        val endPos = caster.pos.add(caster.rotationVector.multiply(length)).add(0.0,1.0,0.0)
        val startPos = caster.pos.add(caster.rotationVector.multiply(startLength)).add(0.0,1.0,0.0)

        val beamRenderer = BeamRendererEntity(world, startPos.x, startPos.y, startPos.z)
        beamRenderer.dataTracker.set(BeamRendererEntity.OuterColour, 0x005d3e96)
        beamRenderer.dataTracker.set(BeamRendererEntity.InterColour, 0x002b99ca)
        beamRenderer.dataTracker.set(BeamRendererEntity.LiveTime, 200)
        beamRenderer.dataTracker.set(BeamRendererEntity.ShrinkTime, 20)
        beamRenderer.dataTracker.set(BeamRendererEntity.TargetPos, endPos.toVector3f())
        beamRenderer.dataTracker.set(
            BeamRendererEntity.OriginPos,
            Vector3f(startPos.x.toFloat(), (startPos.y).toFloat(), startPos.z.toFloat())
        )
        beamRenderer.dataTracker.set(BeamRendererEntity.OuterThickness, 5.0f)
        beamRenderer.dataTracker.set(BeamRendererEntity.MaxOuterThickness, 5.0f)
        beamRenderer.dataTracker.set(BeamRendererEntity.InnerCubes, 3)
        beamRenderer.setPosition(startPos)
        world.spawnEntity(beamRenderer)
    }

    fun sendOutParticleBeam3(size: Double, caster: BigSpeakerEntity, length: Double, startLength: Double) {
        val endPos = caster.pos.add(caster.rotationVector.multiply(length)).add(0.0,1.0,0.0)
        val startPos = caster.pos.add(caster.rotationVector.multiply(startLength)).add(0.0,1.0,0.0)

        val coneRenderer = ConeRendererEntity(world, caster.x, caster.y, caster.z)
        coneRenderer.dataTracker.set(ConeRendererEntity.OuterColour, 0x005d3e96)
        coneRenderer.dataTracker.set(ConeRendererEntity.InterColour, 0x002b99ca)
        coneRenderer.dataTracker.set(ConeRendererEntity.LiveTime, 200)
        coneRenderer.dataTracker.set(ConeRendererEntity.ShrinkTime, 20)
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
                    (lerp(this.pos.y - 0.5, endPos.y, i / interval)),
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
}