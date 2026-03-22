package org.teamvoided.starborn_soundscape.item

import net.minecraft.client.item.TooltipConfig
import net.minecraft.component.type.AttributeModifiersComponent
import net.minecraft.entity.EquipmentSlotGroup
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.ToolMaterial
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.math.Box
import net.minecraft.world.World
import net.mokus.mokuslib.itemskin.CustomItemModel
import org.teamvoided.starborn_soundscape.components.MetronomeChargeData
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeDataComponents
import org.teamvoided.starborn_soundscape.item.song_selection.ToolSongHoldingItem
import org.teamvoided.starborn_soundscape.item.tracker.AxeBassTracker
import org.teamvoided.starborn_soundscape.util.PlayerAxeMeter
import software.bernie.geckolib.util.Color
import java.lang.Math.clamp
import kotlin.math.round

class AxeBassItem(settings: Settings) : ToolSongHoldingItem(settings), CustomItemModel {

    override fun hasInventoryModel(): Boolean {
        return true
    }

    companion object {
        private const val SHOCKWAVE_RADIUS = 6.0
        private const val SHOCKWAVE_DAMAGE = 8f
        private const val SHOCKWAVE_PULL_STRENGTH = 0
        private const val DEFAULT_CHARGE_PER_HIT = 8
        private const val CHARGE_TAKEN = 64
        const val MAX_CHARGE = 64
        const val BAR_LIMIT = 13f
        fun funnyMath(x: Int, y: Int) = clamp(round(BAR_LIMIT - x * BAR_LIMIT / y).toLong(), 0, BAR_LIMIT.toInt())

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
        stack: ItemStack,
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
        val stack = user.getStackInHand(hand)
        val charge = stack.getOrDefault(
            StarbornSoundscapeDataComponents.METRONOME_CHARGE_DATA,
            MetronomeChargeData.DEFAULT
        ).charge
        if (!world.isClient) {
            if (charge >= 64) {
                stack.set(StarbornSoundscapeDataComponents.METRONOME_CHARGE_DATA, MetronomeChargeData(0))
                emitShockwave(world, user)
                useMetronomeSong(stack, user, world)
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

        val rings = 4
        val pointsPerRing = 60

        for (rStep in 0..rings) {
            val r = SHOCKWAVE_RADIUS * (rStep.toDouble() / rings)
            val serverWorld = world as ServerWorld
            val center = user.pos
            for (i in 0 until pointsPerRing) {
                val angle = 2 * Math.PI * i / pointsPerRing

                val x = center.x + r * kotlin.math.cos(angle)
                val z = center.z + r * kotlin.math.sin(angle)
                val y = center.y + 0.1

                serverWorld.spawnParticles(ParticleTypes.SCULK_SOUL,
                    x, y, z,
                    1, 0.0, 0.0, 0.0, 0.0)
                serverWorld.spawnParticles(ParticleTypes.LAVA,
                    x, y, z,
                    1, 0.0, 0.0, 0.0, 0.1)
                serverWorld.spawnParticles(ParticleTypes.LARGE_SMOKE,
                    x, y, z,
                    1, 0.0, 0.0, 0.0, 0.1)
            }
        }

        for (entity in entities) {
            val dir = user.pos.subtract(entity.pos).normalize()
            entity.damage(world.damageSources.playerAttack(user), SHOCKWAVE_DAMAGE)
            entity.addVelocity(dir.x * SHOCKWAVE_PULL_STRENGTH, 0.75, dir.z * SHOCKWAVE_PULL_STRENGTH)
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

        if (tracker.isOnBeat(tick, 2)) {
            PlayerAxeMeter.get(player).add(8f)
        }
    }

    override fun postHit(stack: ItemStack, target: LivingEntity?, attacker: LivingEntity): Boolean {
        if (attacker is PlayerEntity) {
            val tracker = AxeBassTracker.get(attacker)
            val tick = attacker.world.time

            if (tracker.isOnBeat(tick, 2)) {
                var charge = stack.getOrDefault(
                    StarbornSoundscapeDataComponents.METRONOME_CHARGE_DATA,
                    MetronomeChargeData.DEFAULT
                ).charge
                if (stack.item is ToolSongHoldingItem && (stack.item as ToolSongHoldingItem).hasASongToSing(stack)) {
                    charge += (stack.item as ToolSongHoldingItem).getSongItem(stack)!!
                        .getMetronomeChargePerHit(attacker)
                } else {
                    charge += DEFAULT_CHARGE_PER_HIT
                }
                if (charge > 64) {
                    charge = 64
                }
                stack.set(StarbornSoundscapeDataComponents.METRONOME_CHARGE_DATA, MetronomeChargeData(charge))
            }
        }
        return super.postHit(stack, target, attacker)
    }

    // item bar stuff
    override fun getItemBarStep(stack: ItemStack): Int {
        val data =
            stack.getOrDefault(StarbornSoundscapeDataComponents.METRONOME_CHARGE_DATA, MetronomeChargeData.DEFAULT)
        return data?.let {
            funnyMath(
                MAX_CHARGE - it.charge,
                MAX_CHARGE
            )
        } ?: BAR_LIMIT.toInt()
    }

    override fun allowComponentsUpdateAnimation(
        player: PlayerEntity?,
        hand: Hand?,
        oldStack: ItemStack?,
        newStack: ItemStack?
    ): Boolean {
        return false
    }

    override fun getItemBarColor(stack: ItemStack): Int {
        if (hasASongToSing(stack)) {
            return getBarColor(stack)
        }
        return Color.BLUE.color
    }

    override fun isItemBarVisible(stack: ItemStack): Boolean {
        return true
    }
}
