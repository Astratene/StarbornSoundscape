package org.teamvoided.starborn_soundscape.item

import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.AttributeModifiersComponent
import net.minecraft.component.type.NbtComponent
import net.minecraft.entity.Entity
import net.minecraft.entity.EquipmentSlotGroup
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.AxeItem
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.ToolMaterial
import net.minecraft.item.ToolMaterials
import net.minecraft.nbt.NbtCompound
import net.minecraft.sound.SoundCategory
import net.minecraft.world.World
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeSounds

class AxeBassItem(settings: Item.Settings) : AxeItem(ToolMaterials.NETHERITE, settings) {
    val MAX_CHARGE = 100f
    val CHARGE_PER_HIT = 12.5f
    val BEAT_INTERVAL = 15
    val BEAT_WINDOW = 1

    companion object {
        fun createAttributes(
            material: ToolMaterial,
            baseAttackDamageModifier: Int,
            attackSpeedModifier: Float,
        ): AttributeModifiersComponent {
            return AttributeModifiersComponent.builder().add(
                EntityAttributes.GENERIC_ATTACK_DAMAGE, EntityAttributeModifier(
                    BASE_ATTACK_DAMAGE,
                    (baseAttackDamageModifier.toFloat() + material.attackDamage).toDouble(),
                    EntityAttributeModifier.Operation.ADD_VALUE
                ), EquipmentSlotGroup.MAINHAND
            ).add(
                EntityAttributes.GENERIC_ATTACK_SPEED, EntityAttributeModifier(
                    BASE_ATTACK_SPEED, attackSpeedModifier.toDouble(), EntityAttributeModifier.Operation.ADD_VALUE
                ), EquipmentSlotGroup.MAINHAND
            ).build()
        }
    }

    private fun ItemStack.getAxeData(): NbtCompound {
        return this.get(DataComponentTypes.CUSTOM_DATA)?.nbt ?: NbtCompound()
    }

    private fun ItemStack.setAxeData(nbt: NbtCompound) {
        this.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(nbt))
    }

    var ItemStack.beatCounter: Int
        get() = getAxeData().getInt("BeatCounter")
        set(value) {
            val nbt = getAxeData()
            nbt.putInt("BeatCounter", value)
            setAxeData(nbt)
        }

    var ItemStack.lastBeatTick: Long
        get() = getAxeData().getLong("LastBeatTick")
        set(value) {
            val nbt = getAxeData()
            nbt.putLong("LastBeatTick", value)
            setAxeData(nbt)
        }

    override fun inventoryTick(
        stack: ItemStack,
        world: World,
        entity: Entity,
        slot: Int,
        selected: Boolean
    ) {
        if (!selected || world.isClient || entity !is PlayerEntity) return

        val tick = world.time
        if (tick - stack.lastBeatTick >= BEAT_INTERVAL) {
            stack.lastBeatTick = tick
            stack.beatCounter++

            val sound = if (stack.beatCounter % 4 == 0) {
                StarbornSoundscapeSounds.METRONOME_1
            } else {
                StarbornSoundscapeSounds.METRONOME_2
            }

            world.playSound(
                null,
                entity.blockPos,
                sound,
                SoundCategory.PLAYERS,
                0.9f,
                1.0f
            )
        }
    }
}