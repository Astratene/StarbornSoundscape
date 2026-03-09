package org.teamvoided.starborn_soundscape.init

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import org.teamvoided.starborn_soundscape.StarbornSoundscape.id
import org.teamvoided.starborn_soundscape.entity.*
import org.teamvoided.starborn_soundscape.entity.astra_stuff_dont_peep.*

object StarbornSoundscapeEntities {
    fun init() = Unit

    // example
//    val ENTITY = register(
//        "entity",
//        EntityType.Builder.create(EntityType.EntityFactory(::Entity), SpawnGroup.MISC)
//            .setDimensions(0.5f, 0.5f).maxTrackingRange(4).build()
//    )

    val COSMIC_BOLT = register(
        "cosmic_bolt",
        EntityType.Builder.create(EntityType.EntityFactory(::CosmicBoltEntity), SpawnGroup.MISC)
            .setDimensions(0.5f, 0.5f).maxTrackingRange(4).build()
    )
    val STAR_PROJECTILE = register(
        "star_projectile",
        EntityType.Builder.create(EntityType.EntityFactory(::StarProjectileEntity), SpawnGroup.MISC)
            .setDimensions(0.1f, 0.1f).maxTrackingRange(4).build()
    )

    val SMALL_SPEAKER = register(
        "small_speaker",
        EntityType.Builder.create(EntityType.EntityFactory(::SmallSpeakerEntity), SpawnGroup.MISC)
            .setDimensions(0.5f, 0.5f).maxTrackingRange(4).build()
    )

    val BIG_SPEAKER = register(
        "big_speaker",
        EntityType.Builder.create(EntityType.EntityFactory(::BigSpeakerEntity), SpawnGroup.MISC)
            .setDimensions(2.0f, 3.0f).maxTrackingRange(4).build()
    )

    val BEAM_RENDERER = register(
        "beam_renderer",
        EntityType.Builder.create(EntityType.EntityFactory(::BeamRendererEntity), SpawnGroup.MISC)
            .setDimensions(0.5f, 0.5f).maxTrackingRange(4).build()
    )

    val CONE_RENDERER = register(
        "cone_renderer",
        EntityType.Builder.create(EntityType.EntityFactory(::ConeRendererEntity), SpawnGroup.MISC)
            .setDimensions(0.5f, 0.5f).maxTrackingRange(4).build()
    )

    val TOXIC_CLOUD = register(
        "toxic_cloud",
        EntityType.Builder.create(EntityType.EntityFactory(::ToxicCloudEntity), SpawnGroup.MISC)
            .setDimensions(0.5f, 0.5f).maxTrackingRange(4).build()
    )


    private fun <T : Entity> register(path: String, entry: EntityType<T>): EntityType<T> {
        return Registry.register(Registries.ENTITY_TYPE, id(path), entry)
    }
}