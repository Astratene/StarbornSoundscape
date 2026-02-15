package org.teamvoided.starborn_soundscape.item.song_selection

import net.minecraft.client.item.TooltipConfig
import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.BundleContentsComponent
import net.minecraft.entity.Entity
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.StackReference
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsage
import net.minecraft.screen.slot.Slot
import net.minecraft.sound.SoundEvents
import net.minecraft.text.Text
import net.minecraft.util.ClickType
import net.minecraft.util.Formatting
import net.minecraft.world.World
import org.teamvoided.starborn_soundscape.StarbornSoundscape
import org.teamvoided.starborn_soundscape.data.tags.StarbornSoundscapeItemTags
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeItems
import software.bernie.geckolib.util.Color
import java.util.*

open class SongHoldingItem(settings: Settings) : Item(settings) {


    //all the bundle stuff
    override fun onClickedOnOther(stack: ItemStack, slot: Slot, clickType: ClickType?, player: PlayerEntity): Boolean {
        if (clickType != ClickType.RIGHT) {
            return false
        } else {
            val bundleContentsComponent =
                stack.getOrDefault(DataComponentTypes.BUNDLE_CONTENTS, BundleContentsComponent.DEFAULT)
            if (bundleContentsComponent == null) {
                return false
            } else {
                val itemStack = slot.getStack()

                val builder = BundleContentsComponent.Builder(bundleContentsComponent)
                if (itemStack.isEmpty()) {
                    this.playRemoveOneSound(player)
                    val itemStack2 = builder.removeFirst()
                    if (itemStack2 != null) {
                        val itemStack3 = slot.insertStack(itemStack2)
                        builder.tryAdd(itemStack3)

                    }
                } else if (itemStack.getItem().canBeNested()) {
                    val i: Int = builder.tryTransfer(slot, player)
                    if (i > 0) {
                        this.playInsertSound(player)
                    }
                }
                stack.set<BundleContentsComponent?>(DataComponentTypes.BUNDLE_CONTENTS, builder.build())
            }
            return true
        }
    }

    override fun onClicked(
        stack: ItemStack,
        otherStack: ItemStack,
        slot: Slot,
        clickType: ClickType?,
        player: PlayerEntity,
        cursorStackReference: StackReference
    ): Boolean {
        if (clickType == ClickType.RIGHT && slot.canTakePartial(player)) {
            val bundleContentsComponent =
                stack.getOrDefault(DataComponentTypes.BUNDLE_CONTENTS, BundleContentsComponent.DEFAULT)
            if (bundleContentsComponent == null) {
                return false
            } else {
                val builder = BundleContentsComponent.Builder(bundleContentsComponent)
                if (otherStack.isEmpty()) {
                    val itemStack = builder.removeFirst()
                    if (itemStack != null) {
                        this.playRemoveOneSound(player)
                        cursorStackReference.set(itemStack)
                    }
                } else if (otherStack.isIn(StarbornSoundscapeItemTags.SONG_ITEMS)) {
                    val i: Int = builder.tryAdd(otherStack)
                    if (i > 0) {
                        this.playInsertSound(player)
                    }
                }

                stack.set<BundleContentsComponent?>(DataComponentTypes.BUNDLE_CONTENTS, builder.build())
                return true
            }
        } else {
            return false
        }
    }

    override fun appendTooltip(
        stack: ItemStack,
        context: TooltipContext?,
        tooltip: MutableList<Text?>,
        type: TooltipConfig?
    ) {
        val contents =
            stack.getOrDefault(DataComponentTypes.BUNDLE_CONTENTS, BundleContentsComponent.DEFAULT)

        if (contents == null || contents.isEmpty) {
            tooltip.add(
                Text.translatable("tooltip.soundscape.requires_song.tooltip").formatted(Formatting.DARK_PURPLE)
            )
//            tooltip.add(
//                Text.translatable("tooltip.soundscape.requires_song.tooltip_1").formatted(Formatting.RED)
//            )
            return
        }

        tooltip.add(
            Text.translatable("tooltip.soundscape.song.tooltip")
                .formatted(Formatting.YELLOW).formatted(Formatting.ITALIC)
        )

        val planStack: ItemStack = contents.copyContents().iterator().next()
        val planItem = planStack.getItem()

        if (planStack.item is SongItem) {
            val songStack = planStack.item as SongItem
            tooltip.add(
                Text.literal("  ")
                    .append(planStack.getName())
                    .formatted(songStack.getNameColor()).formatted(Formatting.BOLD)
            )
            songStack.addDescription(tooltip)
        }

        val planTooltip: MutableList<Text> = ArrayList<Text>()
//        planItem.appendTooltip(planStack, context, planTooltip, type)

        for (line in planTooltip) {
            val key = line.asComponent().toString()

            if (key.contains("tooltip.soundscape.song.tooltip") ||
                key.contains("tooltip.soundscape.blank_spot.tooltip")
            ) {
                continue
            }

            tooltip.add(
                Text.literal("  ")
                    .append(line)
                    .formatted(Formatting.GRAY)
            )
        }
    }

    override fun onItemEntityDestroyed(entity: ItemEntity) {
        val bundleContentsComponent =
            entity.getStack().get<BundleContentsComponent?>(DataComponentTypes.BUNDLE_CONTENTS)
        if (bundleContentsComponent != null) {
            entity.getStack()
                .set<BundleContentsComponent?>(DataComponentTypes.BUNDLE_CONTENTS, BundleContentsComponent.DEFAULT)
            ItemUsage.spawnItemContents(entity, bundleContentsComponent.copyContents())
        }
    }

    private fun playRemoveOneSound(entity: Entity) {
        entity.playSound(
            SoundEvents.ITEM_BUNDLE_REMOVE_ONE,
            0.8f,
            0.8f + entity.getWorld().getRandom().nextFloat() * 0.4f
        )
    }

    private fun playInsertSound(entity: Entity) {
        entity.playSound(SoundEvents.ITEM_BUNDLE_INSERT, 0.8f, 0.8f + entity.getWorld().getRandom().nextFloat() * 0.4f)
    }

    fun useSong(stack: ItemStack, user: LivingEntity, world: World) {
        val bundleContents = stack.getOrDefault(DataComponentTypes.BUNDLE_CONTENTS, BundleContentsComponent.DEFAULT)
        if (bundleContents != null && !bundleContents.isEmpty) {
            val songInWeapon = bundleContents.copyContents()
            for (item in songInWeapon) {
                if (item.item is SongItem) {
                    val actualItem = item.item as SongItem
                    actualItem.useSong(user, world)
                    break
                }
            }
        }
    }

    fun getOverarchieverUseCharge(stack: ItemStack, user: LivingEntity) : Int {
        val bundleContents = stack.getOrDefault(DataComponentTypes.BUNDLE_CONTENTS, BundleContentsComponent.DEFAULT)
        if (bundleContents != null && !bundleContents.isEmpty) {
            val songInWeapon = bundleContents.copyContents()
            for (item in songInWeapon) {
                if (item.item is SongItem) {
                    val actualItem = item.item as SongItem
                    return actualItem.getOverArchIeverChargeReduction(user)
                }
            }
        }
        return 100000
    }

    fun getBarColor(stack: ItemStack) : Int {
        val bundleContents = stack.getOrDefault(DataComponentTypes.BUNDLE_CONTENTS, BundleContentsComponent.DEFAULT)
        if (bundleContents != null && !bundleContents.isEmpty) {
            val songInWeapon = bundleContents.copyContents()
            for (item in songInWeapon) {
                if (item.item is SongItem) {
                    val actualItem = item.item as SongItem
                    return actualItem.getBarColor().rgb
                }
            }
        }
        return Color.RED.color
    }

    fun hasASongToSing(stack: ItemStack) : Boolean {
        val bundleContents = stack.getOrDefault(DataComponentTypes.BUNDLE_CONTENTS, BundleContentsComponent.DEFAULT)
        if (bundleContents != null && !bundleContents.isEmpty) {
            val songInWeapon = bundleContents.copyContents()
            for (item in songInWeapon) {
                if (item.item is SongItem) {
                    return true
                }
            }
        }
        return false
    }
}