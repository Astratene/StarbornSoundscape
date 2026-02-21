package org.teamvoided.starborn_soundscape.init

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemGroup.DisplayParameters
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.text.Text
import org.teamvoided.starborn_soundscape.StarbornSoundscape
import kotlin.collections.forEach

object StarbornSoundscapeTabs {
    fun init() = Unit

    val TAB = register("tab", StarbornSoundscapeItems.KEEP_UP_FT_STEP2IT, ::tabESB)

    fun tabESB(displayParameters: DisplayParameters): Set<ItemConvertible> {
        return StarbornSoundscapeItems.items()
    }

    fun register(name: String, icon: ItemConvertible, entrySetBuilder: EntrySetBuilder): RegistryKey<ItemGroup> {
        val id = StarbornSoundscape.id(name)
        val key = RegistryKey.of(RegistryKeys.ITEM_GROUP, id)

        Registry.register(
            Registries.ITEM_GROUP, key,
            FabricItemGroup.builder()
                .name(Text.translatable(id.toTranslationKey("itemgroup")))
                .icon { icon.asItem().defaultStack }
                .entries { displayParameters, collector ->
                    entrySetBuilder.createEntrySet(displayParameters).forEach(collector::addItem)
                }.build()
        )

        return key
    }

    fun interface EntrySetBuilder {
        fun createEntrySet(parameters: DisplayParameters): Set<ItemConvertible>
    }
}