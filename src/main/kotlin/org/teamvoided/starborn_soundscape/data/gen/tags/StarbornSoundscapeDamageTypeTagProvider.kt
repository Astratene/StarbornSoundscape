package org.teamvoided.starborn_soundscape.data.gen.tags

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.entity.damage.DamageType
import net.minecraft.registry.HolderLookup
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.DamageTypeTags
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeDamageTypes
import java.util.concurrent.CompletableFuture

class StarbornSoundscapeDamageTypeTagProvider(o: FabricDataOutput, r: CompletableFuture<HolderLookup.Provider>) :
    FabricTagProvider<DamageType>(o, RegistryKeys.DAMAGE_TYPE, r) {

    override fun configure(wrapperLookup: HolderLookup.Provider) {
        getOrCreateTagBuilder(DamageTypeTags.BYPASSES_COOLDOWN)
            .add(StarbornSoundscapeDamageTypes.BOLT_EXPLOSION)
            .add(StarbornSoundscapeDamageTypes.BOLT_DIRECT)
            .add(StarbornSoundscapeDamageTypes.SMALL_SOUNDWAVES)

        getOrCreateTagBuilder(DamageTypeTags.NO_KNOCKBACK)
            .add(StarbornSoundscapeDamageTypes.SMALL_SOUNDWAVES)
            .add(StarbornSoundscapeDamageTypes.BIG_SOUNDWAVES)
            .add(StarbornSoundscapeDamageTypes.CRUSHED)

        getOrCreateTagBuilder(DamageTypeTags.BYPASSES_SHIELD)
            .add(StarbornSoundscapeDamageTypes.SMALL_SOUNDWAVES)
            .add(StarbornSoundscapeDamageTypes.BIG_SOUNDWAVES)
            .add(StarbornSoundscapeDamageTypes.CRUSHED)
    }
}