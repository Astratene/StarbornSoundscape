package org.teamvoided.starborn_soundscape.data.gen.provider

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.minecraft.block.Block
import net.minecraft.data.client.ItemModelGenerator
import net.minecraft.data.client.model.BlockStateModelGenerator
import net.minecraft.data.client.model.Models
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.util.Identifier
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeItems

class StarbornSoundscapeModelProvider(o: FabricDataOutput) : FabricModelProvider(o) {

    // Put items that already have models in here :3
    val hasModel = listOf<Item>(
        StarbornSoundscapeItems.OVERARCHIEVER,
        StarbornSoundscapeItems.BAND_STAMP,
        StarbornSoundscapeItems.EDGE_OF_THE_UNIVERSE,
        StarbornSoundscapeItems.BANJOLECTRIC
    )

    override fun generateBlockStateModels(p0: BlockStateModelGenerator?) {

    }

    override fun generateItemModels(gen: ItemModelGenerator) {
        StarbornSoundscapeItems.items().filterNot(hasModel::contains).forEach { gen.register(it, Models.HANDHELD) }
    }

    private fun Block.blockModel(): Identifier = Registries.BLOCK.getId(this).withPrefix("block/")
}