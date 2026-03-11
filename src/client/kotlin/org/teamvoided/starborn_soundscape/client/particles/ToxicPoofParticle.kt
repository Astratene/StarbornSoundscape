package org.teamvoided.starborn_soundscape.client.particles

import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleFactory
import net.minecraft.client.particle.ParticleTextureSheet
import net.minecraft.client.particle.SpriteBillboardParticle
import net.minecraft.client.particle.SpriteProvider
import net.minecraft.client.world.ClientWorld
import net.minecraft.particle.DefaultParticleType

class ToxicPoofParticle (
    world: ClientWorld,
    x: Double,
    y: Double,
    z: Double,
    velocityX: Double,
    velocityY: Double,
    velocityZ: Double,
    val provider: SpriteProvider
) :
    SpriteBillboardParticle(world, x, y, z) {

    init {
        this.velocityX = velocityX * 0.8
        this.velocityY = velocityY * 0.8
        this.velocityZ = velocityZ * 0.8
        setSpriteForAge(provider)
        maxAge = 22
    }

    override fun tick() {
        super.tick()
        this.colorAlpha = 0.5f
        setSpriteForAge(provider)
    }

//    override fun getBrightness(tint: Float): Int {
//        return 120
//    }

    override fun getSize(tickDelta: Float): Float {
        return 0.5f
    }



    override fun getType(): ParticleTextureSheet? {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT
    }

    class Factory(val provider: SpriteProvider) : ParticleFactory<DefaultParticleType> {

        override fun createParticle(
            effect: DefaultParticleType?,
            world: ClientWorld,
            x: Double,
            y: Double,
            z: Double,
            velocityX: Double,
            velocityY: Double,
            velocityZ: Double
        ): Particle {
            return ToxicPoofParticle(world, x, y, z, velocityX, velocityY, velocityZ, provider)
        }
    }
}
