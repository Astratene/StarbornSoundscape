package org.teamvoided.starborn_soundscape.item

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.Item.Settings
import net.minecraft.item.ItemStack
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import org.teamvoided.starborn_soundscape.entity.SmallSpeakerEntity

class TesterItem(settings: Settings) : Item(settings) {

    override fun use(world: World, user: PlayerEntity, hand: Hand?): TypedActionResult<ItemStack?>? {
        val positions =
            mutableListOf(Vec3d(1.0, 2.5, -0.5),
                Vec3d(-1.0, 2.5, -0.5),
                Vec3d(1.25, 1.5, -0.5),
                Vec3d(-1.25, 1.5, -0.5),
                Vec3d(1.0, 0.5, -0.5),
                Vec3d(-1.0, 0.5, -0.5))
        repeat(6) {
            positions.add(Vec3d(world.random.nextDouble().plus(-0.5).times(10), world.random.nextDouble().times(3), -1.0))
            val speaker = SmallSpeakerEntity(world, user)
            speaker.relativeVec = positions[it]
            speaker.ticksTillTrackTarget = ((it.floorDiv(2)) * 10) + 20
            speaker.setPosition(user.pos)
            world.spawnEntity(speaker)
        }
        return super.use(world, user, hand)
    }
}