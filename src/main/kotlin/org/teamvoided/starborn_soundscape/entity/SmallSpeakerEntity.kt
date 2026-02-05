package org.teamvoided.starborn_soundscape.entity

import com.ibm.icu.util.CodePointTrie
import net.minecraft.command.argument.EntityAnchorArgumentType
import net.minecraft.command.argument.EntityAnchorArgumentType.EntityAnchor
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.data.DataTracker
import net.minecraft.nbt.NbtCompound
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d
import net.minecraft.world.RaycastContext
import net.minecraft.world.World
import org.joml.Math
import org.joml.Math.lerp
import org.joml.Vector3f
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeDamageTypes
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeDamageTypes.customDamage
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeEntities
import software.bernie.geckolib.animatable.GeoAnimatable
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache
import software.bernie.geckolib.animation.AnimatableManager
import software.bernie.geckolib.util.GeckoLibUtil
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt


class SmallSpeakerEntity : Entity, GeoAnimatable {

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
    var damage = 0.1f
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
    val followingPostDeath = false

    override fun tick() {
        if (this.owner == null) {
            discard()
        }
        if (this.owner != null) {
            if (ticksTillTrackTarget > 0) {
                sendOutParticleBeam2(5.0, this, 10.0)
                ticksTillTrackTarget--
                val ownerPos = this.owner!!.pos
                val ownerYaw = this.owner!!.yaw
                val rotatedVec = this.relativeVec.rotateY(((ownerYaw) * (Math.PI.toFloat() / 180)) * -1)
                this.setPosition(ownerPos.add(rotatedVec.x, rotatedVec.y, rotatedVec.z))
                this.setRotation(this.owner!!.yaw, 0f)
                if (ticksTillTrackTarget <= 0) {
                    val temp = pickATarget()
                    if (temp != null) this.targetEntity = temp
                    else {
                        randomlySelectedFollowPoint =
                            this.eyePos.add(this.rotationVector.multiply(randomlySelectedFollowDistance))
                        followingPastVelocity = true
                        targetVelocity = Vec3d(
                            random.nextDouble().times(2).plus(-1),
                            0.0,
                            random.nextDouble().times(2).plus(-1)
                        )
                    }
                }
            } else if (ticksTillShootLaser > 0) {
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
                lifetimeTicks--
                if (targetEntity != null) {
                    this.lookAt(EntityAnchor.EYES, lastFivePlacesTheTargetWas.first())
                    dealDamageToEntitiesInBeam()
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
                    dealDamageToEntitiesInBeam()
                    lastFivePlacesTheTargetWas.removeFirst()
                    lastFivePlacesTheTargetWas.add(
                        lastFivePlacesTheTargetWas.last().add(Vec3d(targetVelocity.toVector3f()))
                    )
                }
            }
        }
    }

    fun pickATarget(): LivingEntity? {
        val entities = collectEntitiesInBeam(targetGrabRadius, this, targetGrabLength)
        if (entities.isNotEmpty()) {
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
        sendOutParticleBeam(damageRadius, this, damageRange)
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


    fun sendOutParticleBeam(size: Double, caster: SmallSpeakerEntity, length: Double) {
        val endPos = caster.eyePos.add(caster.rotationVector.multiply(length))
        val interval = length / size
//        for (i in 0..interval.roundToInt()) {
//            if (!this.world.isClient) {
//                val serverWorld = this.world as ServerWorld
//                serverWorld.spawnParticles(
//                    ParticleTypes.END_ROD,
//                    (lerp(this.eyePos.x, endPos.x, i / interval)),
//                    (lerp(this.eyePos.y - 0.5, endPos.y, i / interval)),
//                    (lerp(this.eyePos.z, endPos.z, i / interval)),
//                    1,
//                    0.0,
//                    0.0,
//                    0.0,
//                    0.0
//                )
//            }
//        }
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
            beamRenderer.dataTracker.set(BeamRendererEntity.OuterThickness, 0.25f)
            beamRenderer.dataTracker.set(BeamRendererEntity.MaxOuterThickness, 0.25f)
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


    override fun initDataTracker(builder: DataTracker.Builder?) {
    }

    override fun readCustomDataFromNbt(nbt: NbtCompound?) {
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound?) {
    }

    private val animationCache: AnimatableInstanceCache =
        GeckoLibUtil.createInstanceCache(this)

    override fun registerControllers(p0: AnimatableManager.ControllerRegistrar?) {
        TODO("Not yet implemented")
    }

    override fun getAnimatableInstanceCache(): AnimatableInstanceCache? {
        return animationCache
    }

    override fun getTick(p0: Any?): Double {
        TODO("Not yet implemented")
    }
}