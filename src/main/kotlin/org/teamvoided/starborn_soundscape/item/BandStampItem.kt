package org.teamvoided.starborn_soundscape.item

import net.minecraft.client.item.TooltipConfig
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.Formatting
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeEffects

class BandStampItem(settings: Settings) : Item(settings) {

//    override fun useOnEntity(
//        stack: ItemStack?,
//        user: PlayerEntity?,
//        entity: LivingEntity?,
//        hand: Hand?
//    ): ActionResult? {
//        if (entity is PlayerEntity){
//            entity.addStatusEffect(StatusEffectInstance(
//                StarbornSoundscapeEffects.BAND_APPROVED,
//                -1, 0,
//                false, true, true
//            ))
//        }
//        return super.useOnEntity(stack, user, entity, hand)
//    }

    override fun postHit(stack: ItemStack?, target: LivingEntity, attacker: LivingEntity?): Boolean {
        if (target.hasStatusEffect(StarbornSoundscapeEffects.BAND_APPROVED)){
            target.removeStatusEffect(StarbornSoundscapeEffects.BAND_APPROVED)
        }
        else {
            target.addStatusEffect(StatusEffectInstance(
                StarbornSoundscapeEffects.BAND_APPROVED,
                -1, 0,
                false, false, true
            ))
        }
        return super.postHit(stack, target, attacker)
    }

    override fun use(world: World?, user: PlayerEntity, hand: Hand?): TypedActionResult<ItemStack?>? {
        if (user.hasStatusEffect(StarbornSoundscapeEffects.BAND_APPROVED)){
            user.removeStatusEffect(StarbornSoundscapeEffects.BAND_APPROVED)
        }
        else {
            user.addStatusEffect(StatusEffectInstance(
                StarbornSoundscapeEffects.BAND_APPROVED,
                -1, 0,
                false, false, true
            ))
        }
        return super.use(world, user, hand)
    }

    override fun appendTooltip(
        stack: ItemStack?,
        context: TooltipContext?,
        tooltip: MutableList<Text?>,
        config: TooltipConfig?
    ) {
        tooltip.add(
            Text.translatable("tooltip.soundscape.stamp1.tooltip").formatted(Formatting.DARK_PURPLE)
        )
        super.appendTooltip(stack, context, tooltip, config)
    }

}