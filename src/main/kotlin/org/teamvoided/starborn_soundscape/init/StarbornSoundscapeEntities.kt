package org.teamvoided.starborn_soundscape.init

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import org.teamvoided.starborn_soundscape.StarbornSoundscape.id
import org.teamvoided.starborn_soundscape.entity.CosmicBoltEntity

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


    private fun <T : Entity> register(path: String, entry: EntityType<T>): EntityType<T> {
        return Registry.register(Registries.ENTITY_TYPE, id(path), entry)
    }
}