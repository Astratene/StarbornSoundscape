package org.teamvoided.starborn_soundscape.item

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.Item.Settings
import net.minecraft.item.ItemStack
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import org.joml.Vector3d
import org.joml.Vector3f
import org.teamvoided.starborn_soundscape.entity.BeamRendererEntity
import org.teamvoided.starborn_soundscape.entity.ConeRendererEntity
import org.teamvoided.starborn_soundscape.entity.SmallSpeakerEntity
import org.teamvoided.starborn_soundscape.entity.SpotLightEntity

class TesterItem(settings: Settings) : Item(settings) {

    override fun use(world: World, user: PlayerEntity, hand: Hand?): TypedActionResult<ItemStack?>? {
        val beamRenderer = SpotLightEntity(world, user.x, user.eyeY, user.z)
        beamRenderer.dataTracker.set(SpotLightEntity.OuterColour, 0x005d3e96)
        beamRenderer.dataTracker.set(SpotLightEntity.InterColour, 0x002b99ca)
        //beamRenderer.dataTracker.set(BeamRendererEntity.LiveTime, ticks)
        //beamRenderer.dataTracker.set(SpotLightEntity.ShrinkTime, 0)
        beamRenderer.dataTracker.set(SpotLightEntity.TargetPos, Vector3f(user.x.toFloat() + 1, user.eyeY.toFloat() + 10, user.z.toFloat()))
        beamRenderer.dataTracker.set(SpotLightEntity.OuterThickness, 0.1f)
        beamRenderer.dataTracker.set(SpotLightEntity.MaxOuterThickness, 0.1f)
        beamRenderer.dataTracker.set(SpotLightEntity.InnerCubes, 1)
        beamRenderer.dataTracker.set(SpotLightEntity.EndSize, 2f)
        beamRenderer.dataTracker.set(SpotLightEntity.MaxEndSize, 2f)
        beamRenderer.dataTracker.set(SpotLightEntity.Length, 10f)
        beamRenderer.setPosition(user.x, user.eyeY, user.z)
        world.spawnEntity(beamRenderer)
        println("waow")
        return super.use(world, user, hand)
    }
}