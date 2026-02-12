package org.teamvoided.starborn_soundscape.data.tags

import net.minecraft.item.Item
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import org.teamvoided.starborn_soundscape.StarbornSoundscape.id
import org.teamvoided.starborn_soundscape.util.tag

object StarbornSoundscapeItemTags {
    val ALL_TAGS = mutableSetOf<TagKey<Item>>()

    val ORVERARCHIEVER_ENCHANTABLE = create("overarchiever_enchantable")


    private fun create(id: String): TagKey<Item> {
        val key = RegistryKeys.ITEM.tag(id(id))
        ALL_TAGS.add(key)
        return key
    }
}