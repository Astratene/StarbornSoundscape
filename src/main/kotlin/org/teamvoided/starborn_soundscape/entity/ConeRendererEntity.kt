package org.teamvoided.starborn_soundscape.entity

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.nbt.NbtCompound
import net.minecraft.world.World
import org.joml.Vector3f
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeEntities.CONE_RENDERER

class ConeRendererEntity : Entity {

    constructor(entityType: EntityType<out ConeRendererEntity?>?, world: World?) :
            super(entityType as EntityType<out Entity?>?, world)

    constructor(world: World?, owner: LivingEntity?) :
            super(CONE_RENDERER as EntityType<out Entity?>, world)

    constructor(world: World?, x: Double, y: Double, z: Double) :
            super(CONE_RENDERER as EntityType<out Entity?>, world)

    override fun tick() {
        if (this.dataTracker.get(LiveTime) > this.dataTracker.get(ShrinkTime)) {
            this.dataTracker.set(LiveTime, this.dataTracker.get(LiveTime) - 1)
            this.dataTracker.set(OuterThickness, this.dataTracker.get(MaxOuterThickness))
        } else if (this.dataTracker.get(LiveTime) > 0) {
            this.dataTracker.set(LiveTime, this.dataTracker.get(LiveTime) - 1)
            this.dataTracker.set(
                OuterThickness, getThickness(
                    this.dataTracker.get(ShrinkTime), this.dataTracker.get(
                        LiveTime
                    ), this.dataTracker.get(MaxOuterThickness)
                )
            )
            this.dataTracker.set(
                EndSize, getThickness(
                    this.dataTracker.get(ShrinkTime), this.dataTracker.get(
                        LiveTime
                    ), this.dataTracker.get(MaxEndSize)
                )
            )
        } else if (this.dataTracker.get(LiveTime) <= 0) {
            this.discard()
        }
        super.tick()
    }

    fun getThickness(ShrinkTime: Int, currentTime: Int, MaxSize: Float): Float {
        if (currentTime > ShrinkTime) {
            return MaxSize
        } else {
            return ((currentTime.toFloat() / ShrinkTime.toFloat()) * MaxSize)
        }
    }

    override fun shouldRender(distance: Double): Boolean {
        return true
    }

    override fun initDataTracker(builder: DataTracker.Builder) {
        builder.add(InterColour, 0xffffffff.toInt())
        builder.add(OuterColour, 0xffffffff.toInt())
        builder.add(TargetPos, Vector3f(0f, 0f, 0f))
        builder.add(MaxOuterThickness, 0f)
        builder.add(OuterThickness, 0f)
        builder.add(ShrinkTime, 0)
        builder.add(LiveTime, 0)
        builder.add(InnerCubes, 0)
        builder.add(Opacity, 0.3f)
        builder.add(OriginPos, Vector3f(0f, 0f, 0f))
        builder.add(EndSize, 0f)
        builder.add(MaxEndSize, 0f)
    }

    companion object {
        val InterColour: TrackedData<Int> =
            DataTracker.registerData(ConeRendererEntity::class.java, TrackedDataHandlerRegistry.INTEGER)
        val OuterColour: TrackedData<Int> =
            DataTracker.registerData(ConeRendererEntity::class.java, TrackedDataHandlerRegistry.INTEGER)
        val TargetPos: TrackedData<Vector3f> =
            DataTracker.registerData(ConeRendererEntity::class.java, TrackedDataHandlerRegistry.VECTOR3F)
        val MaxOuterThickness: TrackedData<Float> =
            DataTracker.registerData(ConeRendererEntity::class.java, TrackedDataHandlerRegistry.FLOAT)
        val OuterThickness: TrackedData<Float> =
            DataTracker.registerData(ConeRendererEntity::class.java, TrackedDataHandlerRegistry.FLOAT)
        val ShrinkTime: TrackedData<Int> =
            DataTracker.registerData(ConeRendererEntity::class.java, TrackedDataHandlerRegistry.INTEGER)
        val LiveTime: TrackedData<Int> =
            DataTracker.registerData(ConeRendererEntity::class.java, TrackedDataHandlerRegistry.INTEGER)
        val InnerCubes: TrackedData<Int> =
            DataTracker.registerData(ConeRendererEntity::class.java, TrackedDataHandlerRegistry.INTEGER)
        val Opacity: TrackedData<Float> =
            DataTracker.registerData(ConeRendererEntity::class.java, TrackedDataHandlerRegistry.FLOAT)
        val OriginPos: TrackedData<Vector3f> =
            DataTracker.registerData(ConeRendererEntity::class.java, TrackedDataHandlerRegistry.VECTOR3F)
        val EndSize: TrackedData<Float> =
            DataTracker.registerData(ConeRendererEntity::class.java, TrackedDataHandlerRegistry.FLOAT)
        val MaxEndSize: TrackedData<Float> =
            DataTracker.registerData(ConeRendererEntity::class.java, TrackedDataHandlerRegistry.FLOAT)
    }


    override fun readCustomDataFromNbt(nbt: NbtCompound?) {
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound?) {
    }
}