package org.teamvoided.starborn_soundscape.init

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes.simple
import net.minecraft.particle.DefaultParticleType
import net.minecraft.particle.ParticleType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import org.teamvoided.starborn_soundscape.StarbornSoundscape.id

object StarbornSoundscapeParticles {
    val TOXIC_POOF: DefaultParticleType = simple()

    fun init() {
        register("toxic_poof", TOXIC_POOF)
    }

    fun register(id: String, particleType: ParticleType<*>): ParticleType<*> =
        Registry.register(Registries.PARTICLE_TYPE, id(id), particleType)
}