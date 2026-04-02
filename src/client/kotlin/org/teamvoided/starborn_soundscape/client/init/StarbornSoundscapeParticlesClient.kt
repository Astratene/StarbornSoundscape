package org.teamvoided.starborn_soundscape.client.init

import einstein.astrasparticles.client.AstralParticle
import einstein.astrasparticles.client.AstralParticle.Provider
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry.PendingParticleFactory
import net.minecraft.particle.ParticleEffect
import net.minecraft.particle.ParticleType
import org.teamvoided.starborn_soundscape.client.particles.ToxicPoofParticle
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeParticles

object StarbornSoundscapeParticlesClient {

    fun init() {
        register(StarbornSoundscapeParticles.TOXIC_POOF, ToxicPoofParticle::Factory)
        ParticleFactoryRegistry.getInstance().register(StarbornSoundscapeParticles.STAR, AstralParticle::Provider)
    }

    fun <T : ParticleEffect> register(type: ParticleType<T>, constructor: PendingParticleFactory<T>) =
        ParticleFactoryRegistry.getInstance().register(type, constructor)
}