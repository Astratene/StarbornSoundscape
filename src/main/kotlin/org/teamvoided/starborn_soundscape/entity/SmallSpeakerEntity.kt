package org.teamvoided.starborn_soundscape.entity

import net.minecraft.command.argument.EntityAnchorArgumentType.EntityAnchor
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import org.joml.Math
import org.joml.Math.lerp
import org.joml.Vector3f
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeDamageTypes
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeDamageTypes.customDamage
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeEntities
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeSounds
import software.bernie.geckolib.animatable.GeoEntity
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache
import software.bernie.geckolib.animation.AnimatableManager
import software.bernie.geckolib.animation.AnimationController
import software.bernie.geckolib.animation.RawAnimation
import software.bernie.geckolib.util.GeckoLibUtil
import kotlin.math.roundToInt


class SmallSpeakerEntity : Entity, GeoEntity {

    var owner: LivingEntity? = null

    constructor(entityType: EntityType<out SmallSpeakerEntity?>?, world: World?) :
            super(entityType as EntityType<out Entity?>?, world)

    constructor(world: World?, owner: LivingEntity?) :
            super(StarbornSoundscapeEntities.SMALL_SPEAKER as EntityType<out Entity?>, world) {
        this.owner = owner
    }

    var ticksTillTrackTarget = 20
    var ticksTillShootLaser = 10
    var lifetimeTicks = 200
    var postStopTicks = 20
    var damage = 0.5f
    var damageRadius = 0.5
    var damageRange = 100.0
    var targetGrabRadius = 5.0 // the radius from the centre of its vision it can grab a target from
    var targetGrabLength = 100.0
    var targetEntity: Entity? = null
    var targetVelocity = Vec3d.ZERO
    val lastFivePlacesTheTargetWas = mutableListOf<Vec3d>()
    var relativeVec: Vec3d = rotationVector
    var followingPastVelocity = false
    var randomlySelectedFollowPoint = Vec3d.ZERO
    var randomlySelectedFollowDistance = 50.0
    var tempMultiplier = 0.0
    var playedSound = false
    var pitch2 = 1.25f

    override fun tick() {
        this.faceBeam()
        if (!world.isClient && owner == null) {
            discard()
            return
        }
        if (this.owner != null) {
            if (ticksTillTrackTarget > 0) {
                sendOutParticleBeam2(5.0, this, 10.0)
                ticksTillTrackTarget--
                val ownerPos = this.owner!!.pos
                val ownerYaw = this.owner!!.yaw
                val rotatedVec = this.relativeVec.rotateY(((ownerYaw) * (Math.PI.toFloat() / 180)) * -1)
                this.setPosition(ownerPos.add(rotatedVec.x, rotatedVec.y, rotatedVec.z))
                this.setRotation(this.owner!!.yaw, 0f)//this.owner!!.pitch)
                if (ticksTillTrackTarget <= 0) {
                    val temp = pickATarget()
                    if (temp != null) this.targetEntity = temp
                    else {
                        randomlySelectedFollowPoint =
                            this.eyePos.add(this.rotationVector.multiply(randomlySelectedFollowDistance))
                        followingPastVelocity = true
                        targetVelocity = Vec3d(
                            random.nextDouble().times(2).plus(-1),
                            random.nextDouble().times(0.2).plus(-0.1),
                            random.nextDouble().times(2).plus(-1)
                        )
                    }
                }
            } else if (ticksTillShootLaser > 0) {
                if(ticksTillShootLaser == 3){
                    this.world.playSound(
                        null,
                        this.x,
                        this.y,
                        this.z,
                        StarbornSoundscapeSounds.SMALL_SPEAKER_STARTUP,
                        SoundCategory.PLAYERS,
                        3.0f,
                        1.0f
                    )                }
                ticksTillShootLaser--
                if (targetEntity != null) {
                    lastFivePlacesTheTargetWas.add(targetEntity!!.eyePos)
                    if (targetEntity!!.isAlive) {
                        targetVelocity = targetEntity!!.velocity
                    }
                } else {
                    lastFivePlacesTheTargetWas.add(
                        randomlySelectedFollowPoint.add(
                            targetVelocity.multiply(
                                tempMultiplier
                            )
                        )
                    )
                    tempMultiplier += 1.0
                }
            } else if (lifetimeTicks > 0) {
                //triggerAnim(null) TODO just simply set the animation here, should work fine
                if (!playedSound) {
                    playedSound = true
                    playSound()
                }
                lifetimeTicks--
                if (targetEntity != null) {
                    this.lookAt(EntityAnchor.EYES, lastFivePlacesTheTargetWas.first())
                    if (this.age % 5 == 0) dealDamageToEntitiesInBeam()
                    sendOutParticleBeam(0.2f, this, damageRange)
                    lastFivePlacesTheTargetWas.removeFirst()
                    lastFivePlacesTheTargetWas.add(targetEntity!!.eyePos)
                    if (!targetEntity!!.isAlive) {
                        this.targetEntity = null
                        this.followingPastVelocity = true
                    } else if (targetEntity!!.isAlive) {
                        targetVelocity = targetEntity!!.velocity
                    }
                } else if (followingPastVelocity) {
                    this.lookAt(EntityAnchor.EYES, lastFivePlacesTheTargetWas.first())
                    if (this.age % 5 == 0) dealDamageToEntitiesInBeam()
                    val random = random.nextFloat().plus(-0.5f).times(0.025f)
                    sendOutParticleBeam(0.2f + random, this, damageRange)
                    lastFivePlacesTheTargetWas.removeFirst()
                    lastFivePlacesTheTargetWas.add(
                        lastFivePlacesTheTargetWas.last().add(Vec3d(targetVelocity.toVector3f()))
                    )
                }
            } else if (postStopTicks > 0) {
                stopTriggeredAnim(null, null)
                postStopTicks--
            } else {
                if (!this.world.isClient) {
                    val serverWorld = this.world as ServerWorld
                    serverWorld.spawnParticles(
                        ParticleTypes.GLOW,
                        this.x,
                        this.y + 0.1,
                        this.z,
                        20,
                        0.2,
                        0.2,
                        0.2,
                        0.0
                    )
                }
                playDeactivationSound()
                discard()
            }
        }
    }

    fun playSound() {
        this.world.playSound(
            null,
            this.x,
            this.y,
            this.z,
            StarbornSoundscapeSounds.RAW_DEADLY_SOUND,
            SoundCategory.PLAYERS,
            5.0f,
            pitch2
        )
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
            (2 - pitch2)
        )
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

        dataTracker.set(TRACKED_PITCH, pitch)
        dataTracker.set(TRACKED_YAW, yaw)

    }

    fun pickATarget(): LivingEntity? {
        val entities = collectEntitiesInBeam(targetGrabRadius, this, targetGrabLength)
        if (entities.isNotEmpty()) {
            for (entity in entities) {
                if (entity is PlayerEntity) {
                    for (entiity in entities) {
                        if (entiity !is PlayerEntity) {
                            entities.remove(entiity)
                        }
                    }
                }
            }
            entities.shuffle()
            return entities.first()
        }
        return null
    }

    fun dealDamageToEntitiesInBeam() {
        val entities = collectEntitiesInBeam(damageRadius, this, damageRange)
        for (entity in entities) {
            entity.customDamage(
                StarbornSoundscapeDamageTypes.SMALL_SOUNDWAVES,
                damage,
                owner,
                owner
            )
        }
    }

    fun collectEntitiesInBeam(size: Double, caster: SmallSpeakerEntity, length: Double): MutableList<LivingEntity> {
        val entities = mutableListOf<Entity>()
        val endPos = caster.eyePos.add(caster.rotationVector.multiply(length))
        val interval = length / size
        for (i in 0..interval.roundToInt()) {
            entities.addAll(
                world.getOtherEntities(
                    caster, Box(
                        (Math.lerp(caster.eyePos.x, endPos.x, i / interval)) + size,
                        (Math.lerp(caster.eyePos.y - size, endPos.y, i / interval)) + size,
                        (Math.lerp(caster.eyePos.z, endPos.z, i / interval)) + size,
                        (Math.lerp(caster.eyePos.x, endPos.x, i / interval)) - size,
                        (Math.lerp(caster.eyePos.y - size, endPos.y, i / interval)) - size,
                        (Math.lerp(caster.eyePos.z, endPos.z, i / interval)) - size
                    )
                ).filter { it is LivingEntity && it != this.owner }
            )
        }
        return entities as MutableList<LivingEntity>
    }


    fun sendOutParticleBeam(size: Float, caster: SmallSpeakerEntity, length: Double) {
        val endPos = caster.eyePos.add(caster.rotationVector.multiply(length))
        if (this.age % 1 == 0) {
            val beamRenderer = BeamRendererEntity(world, caster.x, caster.y, caster.z)
            beamRenderer.dataTracker.set(BeamRendererEntity.OuterColour, 0x005d3e96)
            beamRenderer.dataTracker.set(BeamRendererEntity.InterColour, 0x002b99ca)
            beamRenderer.dataTracker.set(BeamRendererEntity.LiveTime, 1)
            beamRenderer.dataTracker.set(BeamRendererEntity.ShrinkTime, 0)
            beamRenderer.dataTracker.set(BeamRendererEntity.TargetPos, endPos.toVector3f())
            beamRenderer.dataTracker.set(
                BeamRendererEntity.OriginPos,
                Vector3f(caster.x.toFloat(), (caster.y).toFloat(), caster.z.toFloat())
            )
            beamRenderer.dataTracker.set(BeamRendererEntity.OuterThickness, size)
            beamRenderer.dataTracker.set(BeamRendererEntity.MaxOuterThickness, size)
            beamRenderer.dataTracker.set(BeamRendererEntity.InnerCubes, 2)
            beamRenderer.setPosition(caster.x, caster.y, caster.z)
            world.spawnEntity(beamRenderer)
        }
    }

    fun sendOutParticleBeam2(size: Double, caster: SmallSpeakerEntity, length: Double) {
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
                    1,
                    0.0,
                    0.0,
                    0.0,
                    0.0
                )
            }
        }
    }


    override fun initDataTracker(builder: DataTracker.Builder) {
        builder.add(TRACKED_PITCH, 0f)
        builder.add(TRACKED_YAW, 0f)
    }

    override fun readCustomDataFromNbt(nbt: NbtCompound?) {
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound?) {
    }

    private val animationCache: AnimatableInstanceCache =
        GeckoLibUtil.createInstanceCache(this)

    override fun registerControllers(registrar: AnimatableManager.ControllerRegistrar) {
    }

    override fun getTick(p0: Any?): Double {
        return this.age.toDouble()
    }

    override fun getAnimatableInstanceCache(): AnimatableInstanceCache? {
        return animationCache
    }

    companion object {
        val TRACKED_PITCH: TrackedData<Float> =
            DataTracker.registerData(SmallSpeakerEntity::class.java, TrackedDataHandlerRegistry.FLOAT)
        val TRACKED_YAW: TrackedData<Float> =
            DataTracker.registerData(SmallSpeakerEntity::class.java, TrackedDataHandlerRegistry.FLOAT)
    }

}