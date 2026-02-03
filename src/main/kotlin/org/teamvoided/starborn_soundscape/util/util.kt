package org.teamvoided.starborn_soundscape.util

import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.util.math.MathHelper


public fun setPropertiesTwo(
    entity: ProjectileEntity, pitch: Float, yaw: Float, roll: Float, speed: Float, modifierXYZ: Float
) {
    val f = -MathHelper.sin(yaw * (Math.PI.toFloat() / 180)) * MathHelper.cos(pitch * (Math.PI.toFloat() / 180))
    val g = -MathHelper.sin((pitch + roll) * (Math.PI.toFloat() / 180))
    val h = MathHelper.cos(yaw * (Math.PI.toFloat() / 180)) * MathHelper.cos(pitch * (Math.PI.toFloat() / 180))
    entity.setVelocity(f.toDouble(), g.toDouble(), h.toDouble(), speed, modifierXYZ)
}