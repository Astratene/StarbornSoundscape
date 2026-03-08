package org.teamvoided.starborn_soundscape.item.astra_only_no_snooping

import net.minecraft.advancement.criterion.Criteria
import net.minecraft.block.BlockState
import net.minecraft.client.item.TooltipConfig
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.projectile.PersistentProjectileEntity
import net.minecraft.item.*
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.Formatting
import net.minecraft.util.Hand
import net.minecraft.world.event.GameEvent
import java.util.*
import net.mokus.mokuslib.itemskin.CustomItemModel
import org.teamvoided.starborn_soundscape.entity.SmallSpeakerEntity
import org.teamvoided.starborn_soundscape.entity.astra_stuff_dont_peep.StarProjectileEntity
import kotlin.time.Duration.Companion.minutes

class UniverseEdgeItem(settings: Item.Settings) : SwordItem(ToolMaterials.NETHERITE, settings), CustomItemModel {

    override fun postHit(stack: ItemStack?, target: LivingEntity, attacker: LivingEntity): Boolean {
        repeat(5) {
            val star = StarProjectileEntity(attacker.world, attacker)
            star.setPosition(target.eyePos)
            star.setVelocity(
                (attacker.world.random.nextDouble().minus(0.5).times(1)),
                (attacker.world.random.nextDouble().div(5).minus(0.1)),
                (attacker.world.random.nextDouble().minus(0.5).times(1))
            )
            star.trackingEntity = target
            star.pickupType = PersistentProjectileEntity.PickupPermission.DISALLOWED
            star.lifetimeTicks = attacker.world.random.range(200, 210)
            attacker.world.spawnEntity(star)
        }
        return super.postHit(stack, target, attacker)
    }

    override fun appendTooltip(
        stack: ItemStack?,
        context: TooltipContext?,
        tooltip: MutableList<Text?>,
        config: TooltipConfig?
    ) {
        tooltip.add(
            Text.translatable("tooltip.soundscape.wip.tooltip").formatted(Formatting.LIGHT_PURPLE)
        )
        super.appendTooltip(stack, context, tooltip, config)
    }

    override fun hasInventoryModel(): Boolean {
        return true
    }

}