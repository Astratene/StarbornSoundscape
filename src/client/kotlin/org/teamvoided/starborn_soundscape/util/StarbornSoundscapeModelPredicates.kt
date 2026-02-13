package org.teamvoided.starborn_soundscape.util

import net.minecraft.client.item.ModelPredicateProviderRegistry
import net.minecraft.util.Identifier
import org.teamvoided.starborn_soundscape.StarbornSoundscape
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeItems
import org.teamvoided.starborn_soundscape.item.overarchieverItem

object StarbornSoundscapeModelPredicates {
    fun init(){
        ModelPredicateProviderRegistry.register(
            StarbornSoundscapeItems.OVERARCHIEVER,
            Identifier.of("minecraft","pull")
        ) { stack, world, entity, _ ->

            if (entity == null) return@register 0f
            if (entity.activeItem != stack) return@register 0f

            val item = stack.item as overarchieverItem
            val maxCharge = item.getChargeTicks(entity, stack).toFloat()

            val useTime = stack.getUseTicks(entity) - entity.itemUseTimeLeft
            (useTime / maxCharge).coerceIn(0f, 1f)
        }

        ModelPredicateProviderRegistry.register(
            StarbornSoundscapeItems.OVERARCHIEVER,
            Identifier.of("minecraft","pulling")
        ) { stack, _, entity, _ ->
            if (entity != null && entity.isUsingItem && entity.activeItem == stack) 1f else 0f
        }
    }

}