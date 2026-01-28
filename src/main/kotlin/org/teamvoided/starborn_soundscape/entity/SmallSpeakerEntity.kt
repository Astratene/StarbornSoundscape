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
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeDamageTypes
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeDamageTypes.customDamage
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeEntities
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt


class SmallSpeakerEntity : Entity {

    var owner: LivingEntity? = null

    constructor(entityType: EntityType<out SmallSpeakerEntity?>?, world: World?) :
            super(entityType as EntityType<out Entity?>?, world)

    constructor(world: World?, owner: LivingEntity?) :
            super(StarbornSoundscapeEntities.SMALL_SPEAKER as EntityType<out Entity?>, world) {
        this.owner = owner
    }

    var ticksTillTrackTarget = 20
    var ticksTillShootLaser = 5
    var lifetimeTicks = 200
    var damage = 1f
    var damageRadius = 1.0
    var damageRange = 100.0
    var targetGrabRadius = 10.0 // the radius from the centre of its vision it can grab a target from
    var targetGrabLength = 5.0
    var currentLookingPoint: Vec3d? = null
    var targetEntity: Entity? = null
    var currentlyTrackingTarget = false
    var targetVelocity = Vec3d.ZERO
    val lastFivePlacesTheTargetWas = mutableListOf<Vec3d>()
    val relativeVec: Vec3d = rotationVector
    var followingPastVelocity = false
    var randomlySelectedFollowPoint = Vec3d.ZERO
    var randomlySelectedFollowDistance = 50.0
    var tempMultiplier = 0.0

    override fun tick() {
        if (this.owner == null) {
            discard()
        }
        if (ticksTillTrackTarget > 0) {
            ticksTillTrackTarget--
            this.setPosition(this.owner!!.pos.add(relativeVec)) // TODO actually test if this part works and fix it because im sure it doesn't
            this.setRotation(this.owner!!.yaw, 0f)
            if (ticksTillTrackTarget <= 0) {
                val temp = pickATarget()
                if (temp != null) this.targetEntity = temp
                else {
                    randomlySelectedFollowPoint = this.eyePos.add(this.rotationVector.multiply(randomlySelectedFollowDistance))
                    followingPastVelocity = true
                    targetVelocity = Vec3d(random.range(-5, 5).toDouble(),
                        random.range(-5, 5).toDouble(), random.range(-5, 5).toDouble()
                    )
                }
            }
        } else if (ticksTillShootLaser > 0) {
            ticksTillShootLaser--
            if (targetEntity != null) {
                lastFivePlacesTheTargetWas.add(targetEntity!!.pos)
                targetVelocity = targetEntity!!.velocity
            }
            else {
                lastFivePlacesTheTargetWas.add(randomlySelectedFollowPoint.add(targetVelocity.multiply(tempMultiplier)))
                tempMultiplier += 1.0
            }
        } else if (lifetimeTicks > 0) {
            lifetimeTicks--
            if (targetEntity != null) {
                this.lookAt(EntityAnchor.EYES, lastFivePlacesTheTargetWas.first())
                dealDamageToEntitiesInBeam()
                lastFivePlacesTheTargetWas.removeFirst()
                lastFivePlacesTheTargetWas.add(targetEntity!!.pos)
                targetVelocity = targetEntity!!.velocity
                if (!targetEntity!!.isAlive) {
                    this.targetEntity = null
                    this.followingPastVelocity = true
                }
            }
            else if (followingPastVelocity) {
                this.lookAt(EntityAnchor.EYES, lastFivePlacesTheTargetWas.first())
                dealDamageToEntitiesInBeam()
                lastFivePlacesTheTargetWas.removeFirst()
                lastFivePlacesTheTargetWas.add(lastFivePlacesTheTargetWas.last().add(Vec3d(targetVelocity.toVector3f())))
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
                ).filter { it is LivingEntity && it != this && it != this.owner }
            )
        }

        return entities as MutableList<LivingEntity>
    }


    override fun initDataTracker(builder: DataTracker.Builder?) {
    }

    override fun readCustomDataFromNbt(nbt: NbtCompound?) {
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound?) {
    }
}