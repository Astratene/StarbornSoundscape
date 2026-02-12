package org.teamvoided.starborn_soundscape.util

import net.minecraft.client.item.ModelPredicateProviderRegistry
import net.minecraft.util.Identifier
import org.teamvoided.starborn_soundscape.StarbornSoundscape
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeItems

class StarbornSoundscapeModelPredicates {
    fun init(){
        ModelPredicateProviderRegistry.register(
            StarbornSoundscapeItems.OVERARCHIEVER,
            Identifier.of(StarbornSoundscape.MODID,"pull")
        ) { stack, world, entity, seed ->
            if (entity == null) 0.0f
            else if (entity.activeItem != stack) 0.0f
            else (stack.getUseTicks(entity) - entity.itemUseTimeLeft)
                .toFloat() / 20.0f
        }

        ModelPredicateProviderRegistry.register(
            StarbornSoundscapeItems.OVERARCHIEVER,
            Identifier.of(StarbornSoundscape.MODID,"pulling")
        ) { stack, world, entity, seed ->
            if (entity != null && entity.isUsingItem && entity.activeItem == stack) 1.0f else 0.0f
        }

    }
}