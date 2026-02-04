package org.teamvoided.starborn_soundscape.item.song_selection

import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.BundleContentsComponent
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.registry.tag.TagKey

class SongSelectionHelper {
    fun getSongSelectionCount(containerStack: ItemStack, songSelection: Item): Int {
        val contents: BundleContentsComponent =
            containerStack.get(DataComponentTypes.BUNDLE_CONTENTS) ?: return 0

        var count = 0
        contents.stream().forEach { stack ->
            if(stack.isOf(songSelection)){
                count+=stack.count
            }
        }
        return count
    }

    fun hasSongSelection(containerStack: ItemStack, songSelection: Item): Boolean {
        return getSongSelectionCount(containerStack, songSelection) >0
    }

    fun getTaggedSongSelectionCount(containerStack: ItemStack, tag: TagKey<Item>): Int {
        val contents: BundleContentsComponent =
            containerStack.get(DataComponentTypes.BUNDLE_CONTENTS) ?: return 0

        var count = 0
        contents.stream().forEach { stack ->
            if(stack.isIn(tag)){
                count+=stack.count
            }
        }
        return count
    }

    fun hasTaggedSongSelection(containerStack: ItemStack, tag: TagKey<Item>): Boolean {
        return getTaggedSongSelectionCount(containerStack, tag) > 0
    }
}