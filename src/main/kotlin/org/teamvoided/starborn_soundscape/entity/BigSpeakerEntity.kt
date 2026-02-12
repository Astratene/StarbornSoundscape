package org.teamvoided.starborn_soundscape.entity

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.nbt.NbtCompound
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.Box
import net.minecraft.world.World
import org.joml.Math.lerp
import org.joml.Vector3f
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeDamageTypes
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeDamageTypes.customDamage
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
        this.ownerUuid = owner?.uuid
    }

    val damageRadius = 5.0
    val damageRange = 1000.0
    val damage = 5f
    var preFireTicks = 20
    var firingTicks = 200
    var disipationTicks = 20

    override fun tick() {
        if (owner == null && ownerUuid != null && world is ServerWorld) {
            owner = (world as ServerWorld).getEntity(ownerUuid!!) as? LivingEntity
        }
        if (!world.isClient && owner == null) {
            discard()
            return
        }

        super.tick()
        if (this.owner != null) {
            if (preFireTicks > 0) {
                preFireTicks--
                sendOutParticleBeam2(damageRadius, this, damageRange)
                this.yaw = owner!!.yaw
                this.pitch = owner!!.pitch
                if (preFireTicks == 0) {
                    sendOutParticleBeam(damageRadius, this, damageRange)
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

    fun sendOutParticleBeam(size: Double, caster: BigSpeakerEntity, length: Double) {
        val endPos = caster.eyePos.add(caster.rotationVector.multiply(length))

        val beamRenderer = BeamRendererEntity(world, caster.x, caster.y, caster.z)
        beamRenderer.dataTracker.set(BeamRendererEntity.OuterColour, 0x005d3e96)
        beamRenderer.dataTracker.set(BeamRendererEntity.InterColour, 0x002b99ca)
        beamRenderer.dataTracker.set(BeamRendererEntity.LiveTime, 200)
        beamRenderer.dataTracker.set(BeamRendererEntity.ShrinkTime, 20)
        beamRenderer.dataTracker.set(BeamRendererEntity.TargetPos, endPos.toVector3f())
        beamRenderer.dataTracker.set(
            BeamRendererEntity.OriginPos,
            Vector3f(caster.x.toFloat(), (caster.y).toFloat(), caster.z.toFloat())
        )
        beamRenderer.dataTracker.set(BeamRendererEntity.OuterThickness, 5.0f)
        beamRenderer.dataTracker.set(BeamRendererEntity.MaxOuterThickness, 5.0f)
        beamRenderer.dataTracker.set(BeamRendererEntity.InnerCubes, 3)
        beamRenderer.setPosition(caster.x, caster.y, caster.z)
        world.spawnEntity(beamRenderer)
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
                    (lerp(this.eyePos.y - 0.5, endPos.y, i / interval)),
                    (lerp(this.eyePos.z, endPos.z, i / interval)),
                    10,
                    0.5,
                    0.5,
                    0.5,
                    0.0
                )
            }
        }
    }

    fun faceBeam() {
//        val dir = target.subtract(eyePos).normalize()
//
//        val yawDeg =
//            Math.toDegrees(kotlin.math.atan2(-dir.x, dir.z)).toFloat()
//
//        val pitchDeg =
//            Math.toDegrees(kotlin.math.asin(-dir.y)).toFloat()
//
//        yaw = yawDeg
//        prevYaw = yaw

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