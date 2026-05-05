package org.teamvoided.starborn_soundscape.data.gen.provider

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
import net.minecraft.data.server.recipe.RecipeExporter
import net.minecraft.data.server.recipe.RecipeJsonFactory
import net.minecraft.data.server.recipe.ShapedRecipeJsonFactory
import net.minecraft.data.server.recipe.ShapelessRecipeJsonFactory
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.item.Items
import net.minecraft.recipe.RecipeCategory
import net.minecraft.registry.HolderLookup
import net.minecraft.registry.Registries
import net.minecraft.registry.tag.ItemTags
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeItems
import java.util.concurrent.CompletableFuture

class StarbornSoundscapeRecipeProvider(
    output: FabricDataOutput, registriesFuture: CompletableFuture<HolderLookup.Provider>
) : FabricRecipeProvider(output, registriesFuture) {
    override fun generateRecipes(exporter: RecipeExporter) = with(exporter) {
        generateCraftingRecipes()


    }

    private fun RecipeExporter.generateCraftingRecipes() {
        shaped(StarbornSoundscapeItems.UNPRINTED_RECORD)
            .pattern("WWW")
            .pattern("W W")
            .pattern("WWW")
            .ingredient('W', StarbornSoundscapeItems.DISC_WAX)
            .criterion(hasItem(StarbornSoundscapeItems.DISC_WAX), conditionsFromItem(StarbornSoundscapeItems.DISC_WAX))
            .offerTo(this)
        simpleShapeless(
            StarbornSoundscapeItems.DISC_WAX,
            RecipeCategory.MISC, 4,
            listOf(
                Items.CLAY_BALL, Items.HONEYCOMB, Items.SCULK_SENSOR
            )
        ).offerTo(this)
        shaped(StarbornSoundscapeItems.CONSPIRACY_AND_MURDER)
            .pattern("BRB")
            .pattern("RWR")
            .pattern("BRB")
            .ingredient('W', StarbornSoundscapeItems.UNPRINTED_RECORD)
            .ingredient('R', Items.RED_DYE)
            .ingredient('B', Items.BLACK_DYE)
            .criterion(hasItem(StarbornSoundscapeItems.UNPRINTED_RECORD), conditionsFromItem(StarbornSoundscapeItems.UNPRINTED_RECORD))
            .offerTo(this)
        shaped(StarbornSoundscapeItems.TOXIC_CITY)
            .pattern(" R ")
            .pattern("RWR")
            .pattern(" R ")
            .ingredient('W', StarbornSoundscapeItems.UNPRINTED_RECORD)
            .ingredient('R', Items.LIME_DYE)
            .criterion(hasItem(StarbornSoundscapeItems.UNPRINTED_RECORD), conditionsFromItem(StarbornSoundscapeItems.UNPRINTED_RECORD))
            .offerTo(this)
        shaped(StarbornSoundscapeItems.BREAK_IN)
            .pattern(" R ")
            .pattern("RWR")
            .pattern(" R ")
            .ingredient('W', StarbornSoundscapeItems.UNPRINTED_RECORD)
            .ingredient('R', Items.GREEN_DYE)
            .criterion(hasItem(StarbornSoundscapeItems.UNPRINTED_RECORD), conditionsFromItem(StarbornSoundscapeItems.UNPRINTED_RECORD))
            .offerTo(this)
        shaped(StarbornSoundscapeItems.SENT_OFF_TRACK)
            .pattern(" R ")
            .pattern("RWR")
            .pattern(" R ")
            .ingredient('W', StarbornSoundscapeItems.UNPRINTED_RECORD)
            .ingredient('R', Items.LIGHT_GRAY_DYE)
            .criterion(hasItem(StarbornSoundscapeItems.UNPRINTED_RECORD), conditionsFromItem(StarbornSoundscapeItems.UNPRINTED_RECORD))
            .offerTo(this)
        shaped(StarbornSoundscapeItems.BALCONY_SUICIDE)
            .pattern(" R ")
            .pattern("RWR")
            .pattern(" R ")
            .ingredient('W', StarbornSoundscapeItems.UNPRINTED_RECORD)
            .ingredient('R', Items.LIGHT_BLUE_DYE)
            .criterion(hasItem(StarbornSoundscapeItems.UNPRINTED_RECORD), conditionsFromItem(StarbornSoundscapeItems.UNPRINTED_RECORD))
            .offerTo(this)
        shaped(StarbornSoundscapeItems.CLEAN_ESCAPE)
            .pattern(" R ")
            .pattern("RWR")
            .pattern(" R ")
            .ingredient('W', StarbornSoundscapeItems.UNPRINTED_RECORD)
            .ingredient('R', Items.PURPLE_DYE)
            .criterion(hasItem(StarbornSoundscapeItems.UNPRINTED_RECORD), conditionsFromItem(StarbornSoundscapeItems.UNPRINTED_RECORD))
            .offerTo(this)
        shaped(StarbornSoundscapeItems.EYES_ON_THE_LIES)
            .pattern(" R ")
            .pattern("RWR")
            .pattern(" R ")
            .ingredient('W', StarbornSoundscapeItems.UNPRINTED_RECORD)
            .ingredient('R', Items.WHITE_DYE)
            .criterion(hasItem(StarbornSoundscapeItems.UNPRINTED_RECORD), conditionsFromItem(StarbornSoundscapeItems.UNPRINTED_RECORD))
            .offerTo(this)
        shaped(StarbornSoundscapeItems.FOUND_DEAD)
            .pattern("BRB")
            .pattern("RWR")
            .pattern("BRB")
            .ingredient('W', StarbornSoundscapeItems.UNPRINTED_RECORD)
            .ingredient('R', Items.LIGHT_GRAY_DYE)
            .ingredient('B', Items.BLUE_DYE)
            .criterion(hasItem(StarbornSoundscapeItems.UNPRINTED_RECORD), conditionsFromItem(StarbornSoundscapeItems.UNPRINTED_RECORD))
            .offerTo(this)
        shaped(StarbornSoundscapeItems.BURNING_TESTIMONY)
            .pattern("BRB")
            .pattern("RWR")
            .pattern("BRB")
            .ingredient('W', StarbornSoundscapeItems.UNPRINTED_RECORD)
            .ingredient('R', Items.ORANGE_DYE)
            .ingredient('B', Items.RED_DYE)
            .criterion(hasItem(StarbornSoundscapeItems.UNPRINTED_RECORD), conditionsFromItem(StarbornSoundscapeItems.UNPRINTED_RECORD))
            .offerTo(this)
        shaped(StarbornSoundscapeItems.IN_MY_ELEMENT)
            .pattern("PBW")
            .pattern("LDY")
            .pattern("ORO")
            .ingredient('D', StarbornSoundscapeItems.UNPRINTED_RECORD)
            .ingredient('R', Items.RED_DYE)
            .ingredient('B', Items.BLACK_DYE)
            .ingredient('P', Items.PURPLE_DYE)
            .ingredient('W', Items.WHITE_DYE)
            .ingredient('L', Items.BLUE_DYE)
            .ingredient('Y', Items.YELLOW_DYE)
            .ingredient('O', Items.ORANGE_DYE)
            .criterion(hasItem(StarbornSoundscapeItems.UNPRINTED_RECORD), conditionsFromItem(StarbornSoundscapeItems.UNPRINTED_RECORD))
            .offerTo(this)
        shaped(StarbornSoundscapeItems.OVERARCHIEVER)
            .pattern("INL")
            .pattern("NBI")
            .pattern("LID")
            .ingredient('B', Items.BOW)
            .ingredient('I', Items.IRON_INGOT)
            .ingredient('N', Items.NETHERITE_INGOT)
            .ingredient('L', Items.LAPIS_BLOCK)
            .ingredient('D', Items.DIAMOND)
            .criterion(hasItem(StarbornSoundscapeItems.UNPRINTED_RECORD), conditionsFromItem(StarbornSoundscapeItems.UNPRINTED_RECORD))
            .offerTo(this)
        shaped(StarbornSoundscapeItems.METRONOME)
            .pattern("ISI")
            .pattern("BMS")
            .pattern("NDT")
            .ingredient('M', Items.NETHERITE_AXE)
            .ingredient('I', Items.IRON_INGOT)
            .ingredient('N', Items.NETHERITE_INGOT)
            .ingredient('S', Items.STRING)
            .ingredient('D', Items.DIAMOND)
            .ingredient('B', Items.RED_DYE)
            .ingredient('T', Items.ORANGE_DYE)
            .criterion(hasItem(StarbornSoundscapeItems.UNPRINTED_RECORD), conditionsFromItem(StarbornSoundscapeItems.UNPRINTED_RECORD))
            .offerTo(this)
        shaped(StarbornSoundscapeItems.BANJOLECTRIC)
            .pattern("ISI")
            .pattern("BMS")
            .pattern("NDT")
            .ingredient('M', Items.NETHERITE_AXE)
            .ingredient('I', Items.IRON_INGOT)
            .ingredient('N', Items.NETHERITE_INGOT)
            .ingredient('S', Items.STRING)
            .ingredient('D', Items.DIAMOND)
            .ingredient('B', Items.BLUE_DYE)
            .ingredient('T', Items.BLACK_DYE)
            .criterion(hasItem(StarbornSoundscapeItems.UNPRINTED_RECORD), conditionsFromItem(StarbornSoundscapeItems.UNPRINTED_RECORD))
            .offerTo(this)
    }
    fun RecipeJsonFactory.itemCriterion(item: ItemConvertible): RecipeJsonFactory =
        this.criterion(hasItem(item), conditionsFromItem(item))

    private fun shaped(
        result: ItemConvertible,
        category: RecipeCategory = RecipeCategory.MISC,
        count: Int = 1
    ): ShapedRecipeJsonFactory {
        return ShapedRecipeJsonFactory(category, result, count)
    }
    private fun simpleShapeless(
        result: ItemConvertible,
        category: RecipeCategory = RecipeCategory.MISC,
        count: Int = 1,
        ingredients: List<ItemConvertible>
    ): ShapelessRecipeJsonFactory {
        val factory = ShapelessRecipeJsonFactory(category, result, count)
        ingredients.forEach(factory::ingredient)
        ingredients.toSet().forEach {
            factory.criterion(hasItem(it), conditionsFromItem(it))
        }

        return factory
    }

    val Item.id get() = Registries.ITEM.getId(this)
}