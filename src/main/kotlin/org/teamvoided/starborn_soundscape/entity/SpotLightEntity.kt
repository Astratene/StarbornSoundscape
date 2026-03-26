package org.teamvoided.starborn_soundscape.entity

import net.minecraft.command.argument.EntityAnchorArgumentType
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.nbt.NbtCompound
import net.minecraft.world.World
import org.joml.Vector3f
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeEntities.SPOTLIGHT
import org.teamvoided.starborn_soundscape.util.toVec3d

class SpotLightEntity : Entity {

    constructor(entityType: EntityType<out SpotLightEntity?>?, world: World?) :
            super(entityType as EntityType<out Entity?>?, world)

    constructor(world: World?, owner: LivingEntity?) :
            super(SPOTLIGHT as EntityType<out Entity?>, world)

    constructor(world: World?, x: Double, y: Double, z: Double) :
            super(SPOTLIGHT as EntityType<out Entity?>, world)

    override fun tick() {
        this.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, dataTracker.get(TargetPos).toVec3d())
        //super.tick()
    }

    override fun shouldRender(distance: Double): Boolean {
        return true
    }

    override fun initDataTracker(builder: DataTracker.Builder) {
        builder.add(InterColour, 0xffffffff.toInt())
        builder.add(OuterColour, 0xffffffff.toInt())
        builder.add(Length, 0f)
        builder.add(MaxOuterThickness, 0f)
        builder.add(OuterThickness, 0f)
        builder.add(ShrinkTime, 0)
        builder.add(LiveTime, 0)
        builder.add(InnerCubes, 0)
        builder.add(Opacity, 0.3f)
        builder.add(EndSize, 0f)
        builder.add(MaxEndSize, 0f)
        builder.add(TargetPos, Vector3f(0f, 0f, 0f))
    }

    companion object {
        val InterColour: TrackedData<Int> =
            DataTracker.registerData(SpotLightEntity::class.java, TrackedDataHandlerRegistry.INTEGER)
        val OuterColour: TrackedData<Int> =
            DataTracker.registerData(SpotLightEntity::class.java, TrackedDataHandlerRegistry.INTEGER)
        val Length: TrackedData<Float> =
            DataTracker.registerData(SpotLightEntity::class.java, TrackedDataHandlerRegistry.FLOAT)
        val MaxOuterThickness: TrackedData<Float> =
            DataTracker.registerData(SpotLightEntity::class.java, TrackedDataHandlerRegistry.FLOAT)
        val OuterThickness: TrackedData<Float> =
            DataTracker.registerData(SpotLightEntity::class.java, TrackedDataHandlerRegistry.FLOAT)
        val ShrinkTime: TrackedData<Int> =
            DataTracker.registerData(SpotLightEntity::class.java, TrackedDataHandlerRegistry.INTEGER)
        val LiveTime: TrackedData<Int> =
            DataTracker.registerData(SpotLightEntity::class.java, TrackedDataHandlerRegistry.INTEGER)
        val InnerCubes: TrackedData<Int> =
            DataTracker.registerData(SpotLightEntity::class.java, TrackedDataHandlerRegistry.INTEGER)
        val Opacity: TrackedData<Float> =
            DataTracker.registerData(SpotLightEntity::class.java, TrackedDataHandlerRegistry.FLOAT)
        val EndSize: TrackedData<Float> =
            DataTracker.registerData(SpotLightEntity::class.java, TrackedDataHandlerRegistry.FLOAT)
        val MaxEndSize: TrackedData<Float> =
            DataTracker.registerData(SpotLightEntity::class.java, TrackedDataHandlerRegistry.FLOAT)
        val TargetPos: TrackedData<Vector3f> =
            DataTracker.registerData(SpotLightEntity::class.java, TrackedDataHandlerRegistry.VECTOR3F)
    }


    override fun readCustomDataFromNbt(nbt: NbtCompound?) {
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound?) {
    }
}