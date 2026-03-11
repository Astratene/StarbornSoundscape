package org.teamvoided.starborn_soundscape.util

import net.minecraft.enchantment.Enchantment
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.item.ItemStack
import net.minecraft.registry.Holder
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.tag.TagKey
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvent
import net.minecraft.util.Identifier
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import org.joml.Math.lerp
import org.joml.Vector3f
import org.teamvoided.starborn_soundscape.entity.BeamRendererEntity


public fun setPropertiesTwo(
    entity: ProjectileEntity, pitch: Float, yaw: Float, roll: Float, speed: Float, modifierXYZ: Float
) {
    val f = -MathHelper.sin(yaw * (Math.PI.toFloat() / 180)) * MathHelper.cos(pitch * (Math.PI.toFloat() / 180))
    val g = -MathHelper.sin((pitch + roll) * (Math.PI.toFloat() / 180))
    val h = MathHelper.cos(yaw * (Math.PI.toFloat() / 180)) * MathHelper.cos(pitch * (Math.PI.toFloat() / 180))
    entity.setVelocity(f.toDouble(), g.toDouble(), h.toDouble(), speed, modifierXYZ)
}

fun setPropertiesBasedOnPlayerLookingDirection(entity: ProjectileEntity, pos: Vec3d, secondaryPos: Vec3d, speed: Float, modifierXYZ: Float){
    val f = pos.x - secondaryPos.x
    val g = pos.y - secondaryPos.y
    val h = pos.z - secondaryPos.z
    entity.setVelocity(-f, -g, -h, speed, modifierXYZ)
}

fun getPlayerLookingDirectionPos(entity: LivingEntity) : Vec3d{
    return entity.eyePos.add(entity.rotationVector)
}

fun Vector3f.toVec3d(): Vec3d = Vec3d(x.toDouble(), y.toDouble(), z.toDouble())

fun World.playSound(pos: Vec3d, soundEvent: SoundEvent, category: SoundCategory, volume: Float, pitch: Float) {
    this.playSound(null, pos.x, pos.y, pos.z, soundEvent, category, volume, pitch)
}

fun World.playSound(pos: Vec3d, soundEvent: Holder<SoundEvent>, category: SoundCategory, volume: Float, pitch: Float) {
    this.method_60511(null, pos.x, pos.y, pos.z, soundEvent, category, volume, pitch)
}

fun <T, R : Registry<T>> RegistryKey<R>.tag(id: Identifier) = TagKey.of(this, id)

fun ItemStack.hasEnchantment(enchantment: RegistryKey<Enchantment>): Boolean =
    this.enchantments.enchantments.any { it.isRegistryKey(enchantment) }

fun <T> Registry<T>.registerHolder(id: Identifier, entry: T): Holder.Reference<T> =
    Registry.registerHolder(this, id, entry)

fun sillyLightningTime(
    pos1: Vec3d,
    pos2: Vec3d,
    world: ServerWorld,
    minBends: Int,
    maxBends: Int,
    boltTicks: Int,
    thickness: Float,
    randMult: Double,
) {
    val bends = world.random.rangeInclusive(minBends, maxBends)
    val bendPos = mutableListOf<Vec3d>()
    bendPos.add(pos1)
    for (i in 0..<bends) {
        val maxlerp: Double = (1.0 / bends) * i
        val xrand = Math.pow(-1.0, (i.toDouble().plus(world.random.rangeInclusive(0, 1)))).times(randMult)
        val yrand = Math.pow(-1.0, i.toDouble()).times(randMult)
        val zrand = Math.pow(-1.0, (i.toDouble().plus(world.random.rangeInclusive(0, 1)))).times(randMult)
        val ymin = 0 //((pos1.y - pos2.y) / (bends)) * i
        bendPos.add(
            Vec3d(
                (lerp(pos1.x, pos2.x, maxlerp) + world.random.nextDouble().minus(0.5).times(xrand)),
                lerp(pos1.y, pos2.y, maxlerp) + ymin + world.random.nextDouble()
                    .minus(0.5).times(yrand),
                lerp(pos1.z, pos2.z, maxlerp) + world.random.nextDouble().minus(0.5).times(zrand)
            )
        )
    }
    bendPos.add(pos2)
    var count = 0
    for (i in 0..<(bendPos.size - 1)) {
        if (count > bendPos.size) {
            break
        }
        count++
        val a = bendPos[i]
        val b = bendPos[i + 1]
        val distance = a.distanceTo(b)
        val beamRenderer = BeamRendererEntity(world, a.x, a.y, a.z)
        beamRenderer.dataTracker.set(BeamRendererEntity.OuterColour, 0x00300d5a.toInt())
        beamRenderer.dataTracker.set(BeamRendererEntity.InterColour, 0x001b0832.toInt())
        beamRenderer.dataTracker.set(BeamRendererEntity.LiveTime, boltTicks)
        beamRenderer.dataTracker.set(BeamRendererEntity.ShrinkTime, boltTicks - 1)
        beamRenderer.dataTracker.set(BeamRendererEntity.TargetPos, Vec3d(b.x, b.y, b.z).toVector3f())
        beamRenderer.dataTracker.set(
            BeamRendererEntity.OriginPos,
            Vector3f(a.x.toFloat(), (a.y).toFloat(), a.z.toFloat())
        )
        beamRenderer.dataTracker.set(BeamRendererEntity.OuterThickness, thickness)
        beamRenderer.dataTracker.set(BeamRendererEntity.MaxOuterThickness, thickness)
        beamRenderer.dataTracker.set(BeamRendererEntity.InnerCubes, 2)
        beamRenderer.setPosition(a.x, a.y, a.z)
        world.spawnEntity(beamRenderer)
    }
}
