package org.teamvoided.starborn_soundscape.entity

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.MovementType
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.nbt.NbtCompound
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.Box
import net.minecraft.world.World
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeEffects
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeEntities.TOXIC_CLOUD
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeParticles
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeSounds
import java.util.UUID
import kotlin.math.min

class ToxicCloudEntity : Entity {

    var owner: LivingEntity? = null
    var ownerUuid: UUID? = null

    constructor(entityType: EntityType<out ToxicCloudEntity?>?, world: World?) :
            super(entityType as EntityType<out Entity?>?, world)

    constructor(world: World?, owner: LivingEntity?) :
            super(TOXIC_CLOUD as EntityType<out Entity?>, world) {
        this.owner = owner
    }
    // set this to true for ur one brand
    var isSmall = false
    val smallWidth = 3.5
    val smallHeight = 2.5
    // sets the radius for your one brand
    val bigWidth = 4.0
    val bigHeight = 1.0
    var cloudLifespan = 200

    override fun tick() {
        if (this.age == 1) {
            this.world.playSound(
                null,
                this.x,
                this.y,
                this.z,
                StarbornSoundscapeSounds.FIZZ,
                SoundCategory.PLAYERS,
                2.0F,
                0.8f
            )
           }
        val height = if (isSmall) smallHeight else bigHeight
        val width = if (isSmall) smallWidth else bigWidth
        if (world is ServerWorld) (world as ServerWorld).spawnParticles(
            StarbornSoundscapeParticles.TOXIC_POOF, this.x, this.y, this.z,
            10,
            width * 0.4, height * 0.2, width * 0.4,
            0.01
        )
        if (this.age >= cloudLifespan || this.owner == null) {
            this.discard()
        } else {
            val entities = mutableListOf<Entity>()
            entities.addAll(
                world.getOtherEntities(
                    this, Box(
                        this.x + width,
                        this.y + height,
                        this.z + width,
                        this.x - width,
                        this.y,
                        this.z - width
                    )
                )
                    .filter { it is LivingEntity && it != this.owner && !it.hasStatusEffect(StarbornSoundscapeEffects.BAND_APPROVED) }
            )
            for (entity in entities) {
                if (entity is LivingEntity) {
                    if (isSmall) {
                        val shred = entity.getStatusEffect(StarbornSoundscapeEffects.SHRED_OF_TOXICITY)
                        val lvl = shred?.amplifier ?: -1
                        entity.addStatusEffect(
                            StatusEffectInstance(
                                StarbornSoundscapeEffects.SHRED_OF_TOXICITY,
                                40, min(lvl + 1, 100),
                                false, true, true
                            )
                        )
                        if (lvl >= 100) {
                            val deep = entity.getStatusEffect(StarbornSoundscapeEffects.DEEP_TOXICITY)
                            val lvl = deep?.amplifier ?: -1
                            entity.addStatusEffect(
                                StatusEffectInstance(
                                    StarbornSoundscapeEffects.DEEP_TOXICITY,
                                    40, min(lvl + 1, 100),
                                    false, true, true
                                )
                            )
                            if (lvl >= 100) {
                                entity.addStatusEffect(
                                    StatusEffectInstance(
                                        StarbornSoundscapeEffects.CORROSION,
                                        80, 0,
                                        false, true, true
                                    )
                                )
                            }
                        }
                    }
                    // this is the effects stuff for your one brand :3
                    else {
                        val shred = entity.getStatusEffect(StarbornSoundscapeEffects.IRRADIATED)
                        val lvl = shred?.amplifier ?: -1
                        entity.addStatusEffect(
                            StatusEffectInstance(
                                StarbornSoundscapeEffects.IRRADIATED,
                                20, min(lvl + 1, 100),
                                false, true, true
                            )
                        )
                        if (lvl >= 60) {
                            entity.addStatusEffect(
                                StatusEffectInstance(
                                    StarbornSoundscapeEffects.CORROSION,
                                    80, 0,
                                    false, true, true
                                )
                            )
                        }
                    }
                }
            }
        }
        this.move(MovementType.SELF, this.velocity)
        super.tick()
    }


    override fun initDataTracker(builder: DataTracker.Builder?) {
    }

    override fun hasNoGravity(): Boolean {
        return true
    }

    override fun readCustomDataFromNbt(nbt: NbtCompound?) {
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound?) {
    }
}