package org.teamvoided.starborn_soundscape.item

import net.minecraft.client.item.TooltipConfig
import net.minecraft.component.type.AttributeModifiersComponent
import net.minecraft.entity.EquipmentSlotGroup
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.AxeItem
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.ToolMaterial
import net.minecraft.item.ToolMaterials
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.math.Box
import net.minecraft.world.World
import net.mokus.mokuslib.itemskin.CustomItemModel
import org.teamvoided.starborn_soundscape.item.song_selection.SongHoldingItem
import org.teamvoided.starborn_soundscape.item.tracker.AxeBassTracker
import org.teamvoided.starborn_soundscape.util.PlayerAxeMeter

class AxeBassItem(settings: Item.Settings) : AxeItem(ToolMaterials.NETHERITE, settings), CustomItemModel {

    override fun hasInventoryModel(): Boolean {
        return true
    }

    companion object {
        private const val SHOCKWAVE_RADIUS = 6.0
        private const val SHOCKWAVE_DAMAGE = 8f
        private const val SHOCKWAVE_PULL_STRENGTH = 1.2

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

    override fun appendTooltip(
        stack: ItemStack?,
        context: TooltipContext?,
        tooltip: MutableList<Text?>,
        config: TooltipConfig?
    ) {
        tooltip.add(
            Text.translatable("tooltip.soundscape.wip.tooltip").formatted(Formatting.LIGHT_PURPLE)
        )
        super.appendTooltip(stack, context, tooltip, config)
    }

    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        if (!world.isClient) {
            val meter = PlayerAxeMeter.get(user)
            if (meter.isFull()) {
                meter.consume()
                emitShockwave(world, user)
            }
        }
        return TypedActionResult.success(user.getStackInHand(hand))
    }

    private fun emitShockwave(world: World, user: PlayerEntity) {
        val box = Box(
            user.x, user.y, user.z,
            user.x, user.y, user.z
        ).expand(SHOCKWAVE_RADIUS)

        val entities = world.getOtherEntities(
            user,
            box
        ) { it is LivingEntity }

        for (entity in entities) {
            val dir = user.pos.subtract(entity.pos).normalize()
            entity.damage(world.damageSources.playerAttack(user), SHOCKWAVE_DAMAGE)
            entity.addVelocity(dir.x * SHOCKWAVE_PULL_STRENGTH, 0.3, dir.z * SHOCKWAVE_PULL_STRENGTH)
            entity.velocityDirty = true
        }

        world.playSound(
            null,
            user.blockPos,
            SoundEvents.ENTITY_WARDEN_SONIC_BOOM,
            SoundCategory.PLAYERS,
            1.0f,
            1.0f
        )
    }

    fun onHit(player: PlayerEntity, damage: Float) {
        val tracker = AxeBassTracker.get(player)
        val tick = player.world.time

        if (tracker.isOnBeat(tick, 1)) {
            PlayerAxeMeter.get(player).add(damage * 0.5f)
        }
    }
}
