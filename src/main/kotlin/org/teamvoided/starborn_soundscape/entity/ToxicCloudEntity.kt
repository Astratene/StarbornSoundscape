package org.teamvoided.starborn_soundscape.entity

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.data.DataTracker
import net.minecraft.nbt.NbtCompound
import net.minecraft.world.World
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeEntities.TOXIC_CLOUD
import java.util.UUID

class ToxicCloudEntity : Entity {

    var owner: LivingEntity? = null
    var ownerUuid: UUID? = null

    constructor(entityType: EntityType<out ToxicCloudEntity?>?, world: World?) :
            super(entityType as EntityType<out Entity?>?, world)

    constructor(world: World?, owner: LivingEntity?) :
            super(TOXIC_CLOUD as EntityType<out Entity?>, world) {
        this.owner = owner
    }

    val isSmall = false
    val smallWidth = 1
    val smallHeight = 0.5
    val bigWidth = 4
    val bigHeight = 1
    val cloudLifespan = 200



    override fun initDataTracker(builder: DataTracker.Builder?) {
        TODO("Not yet implemented")
    }

    override fun hasNoGravity(): Boolean {
        return true
    }

    override fun readCustomDataFromNbt(nbt: NbtCompound?) {
        TODO("Not yet implemented")
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound?) {
        TODO("Not yet implemented")
    }
}