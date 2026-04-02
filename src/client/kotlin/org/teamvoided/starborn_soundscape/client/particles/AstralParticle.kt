package einstein.astrasparticles.client

import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleFactory
import net.minecraft.client.particle.ParticleTextureSheet
import net.minecraft.client.particle.SpriteBillboardParticle
import net.minecraft.client.particle.SpriteProvider
import net.minecraft.client.render.Camera
import net.minecraft.client.world.ClientWorld
import org.teamvoided.starborn_soundscape.particle.AstralParticleOptions

class AstralParticle(
    level: ClientWorld,
    x: Double,
    y: Double,
    z: Double,
    xSpeed: Double,
    ySpeed: Double,
    zSpeed: Double,
    sprites: SpriteProvider,
    options: AstralParticleOptions
) : SpriteBillboardParticle(level, x, y, z, xSpeed, ySpeed, zSpeed) {

    val lifetimeAlpha = DynamicAlpha(1F, 0F, 0.5F, 1F)
    val fade = options.fade
    val spinning = options.spinning
    val degrees = options.degrees
    val twinkle = options.twinkle
    val velocity = options.velocity
    val shrinking = options.shrinking
    val scalee = options.scale

    init {
        colorAlpha = lifetimeAlpha.startAlpha()
        val color = options.color
        setColor(color.x, color.y, color.z)
        setSprite(sprites)
        setVelocity(
            world.random.nextDouble().minus(0.5).times(velocity.x),
            world.random.nextDouble().minus(0.5).times(velocity.y),
            world.random.nextDouble().minus(0.5).times(velocity.z)
        )
        scale = 0.2F
        scale(0.5F * scalee)
        maxAge = options.lifetime
        gravityStrength = options.gravity
        velocityMultiplier = options.friction
    }

    override fun buildGeometry(vertexConsumer: VertexConsumer, camera: Camera, partialTick: Float) {
        if (!twinkle || age < maxAge / 3F || (age + maxAge) / 3F % 0.5F == 0F) {
            super.buildGeometry(vertexConsumer, camera, partialTick)
        }

        if (fade) {
            colorAlpha = lifetimeAlpha.getAlpha(age, maxAge, partialTick)
        }
    }

    override fun tick() {
        super.tick()
        if (spinning) {
            prevAngle = angle
            angle += degrees
        }
        if (shrinking) {
            scale += -0.005f
            if (scale < 0) {
                scale += -0.005f
            }
        }
    }

    override fun getType(): ParticleTextureSheet? {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT
    }

    override fun getBrightness(tint: Float): Int {
        return 15728880
    }

    class Provider(val sprites: SpriteProvider) : ParticleFactory<AstralParticleOptions> {
        override fun createParticle(
            options: AstralParticleOptions,
            level: ClientWorld,
            x: Double,
            y: Double,
            z: Double,
            xSpeed: Double,
            ySpeed: Double,
            zSpeed: Double
        ): Particle = AstralParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, sprites, options)
    }
}