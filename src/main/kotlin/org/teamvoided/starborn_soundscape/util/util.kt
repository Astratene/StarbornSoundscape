package org.teamvoided.starborn_soundscape.util

import net.minecraft.enchantment.Enchantment
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.item.ItemStack
import net.minecraft.registry.Holder
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.tag.TagKey
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvent
import net.minecraft.util.Identifier
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import org.joml.Vector3f


public fun setPropertiesTwo(
    entity: ProjectileEntity, pitch: Float, yaw: Float, roll: Float, speed: Float, modifierXYZ: Float
) {
    val f = -MathHelper.sin(yaw * (Math.PI.toFloat() / 180)) * MathHelper.cos(pitch * (Math.PI.toFloat() / 180))
    val g = -MathHelper.sin((pitch + roll) * (Math.PI.toFloat() / 180))
    val h = MathHelper.cos(yaw * (Math.PI.toFloat() / 180)) * MathHelper.cos(pitch * (Math.PI.toFloat() / 180))
    entity.setVelocity(f.toDouble(), g.toDouble(), h.toDouble(), speed, modifierXYZ)
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