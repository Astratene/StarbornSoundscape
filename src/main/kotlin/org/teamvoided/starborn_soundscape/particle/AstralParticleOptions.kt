package org.teamvoided.starborn_soundscape.particle

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.network.PacketByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.particle.ParticleEffect
import net.minecraft.particle.ParticleType
import net.minecraft.util.dynamic.Codecs
import org.joml.Vector3f
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeParticles

class AstralParticleOptions(
    val color: Vector3f = Vector3f(1F, 1F, 1F),
    val velocity: Vector3f = Vector3f(0F, 0F, 0F),
    val scale: Float = 1F,
    val twinkle: Boolean = false,
    val fade: Boolean = false,
    val spinning: Boolean = false,
    val shrinking: Boolean = false,
    val degrees: Float = 0F,
    val lifetime: Int = 27,
    val gravity: Float = 0F,
    val friction: Float = 0.98F
) : ParticleEffect {

    companion object {
        val CODEC: MapCodec<AstralParticleOptions> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Codecs.VECTOR3F.fieldOf("color").forGetter { it.color },
                Codecs.VECTOR3F.fieldOf("velocity").forGetter { it.velocity },
                Codec.FLOAT.fieldOf("scale").forGetter { it.scale },
                Codec.BOOL.fieldOf("twinkle").forGetter { it.twinkle },
                Codec.BOOL.fieldOf("fade").forGetter { it.fade },
                Codec.BOOL.fieldOf("spinning").forGetter { it.spinning },
                Codec.BOOL.fieldOf("shrinking").forGetter { it.shrinking },
                Codec.FLOAT.fieldOf("degrees").forGetter { it.degrees },
                Codec.INT.fieldOf("lifetime").forGetter { it.lifetime },
                Codec.FLOAT.fieldOf("gravity").forGetter { it.gravity },
                Codec.FLOAT.fieldOf("friction").forGetter { it.friction }
            ).apply(instance, ::AstralParticleOptions)
        }

        val STREAM_CODEC: PacketCodec<PacketByteBuf, AstralParticleOptions> = PacketCodec.create(
            { buf: PacketByteBuf, options: AstralParticleOptions ->
                buf.writeVector3f(options.color)
                buf.writeVector3f(options.velocity)
                buf.writeFloat(options.scale)
                buf.writeBoolean(options.twinkle)
                buf.writeBoolean(options.fade)
                buf.writeBoolean(options.spinning)
                buf.writeBoolean(options.shrinking)
                buf.writeFloat(options.degrees)
                buf.writeInt(options.lifetime)
                buf.writeFloat(options.gravity)
                buf.writeFloat(options.friction)
            },
            { buf: PacketByteBuf ->
                AstralParticleOptions(
                    buf.readVector3f(),
                    buf.readVector3f(),
                    buf.readFloat(),
                    buf.readBoolean(),
                    buf.readBoolean(),
                    buf.readBoolean(),
                    buf.readBoolean(),
                    buf.readFloat(),
                    buf.readInt(),
                    buf.readFloat(),
                    buf.readFloat()
                )
            })
    }

    override fun getType(): ParticleType<*> = StarbornSoundscapeParticles.STAR
}