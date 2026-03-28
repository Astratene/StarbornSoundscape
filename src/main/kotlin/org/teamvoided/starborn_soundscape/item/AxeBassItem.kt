package org.teamvoided.starborn_soundscape.item

import net.minecraft.block.Blocks
import net.minecraft.client.item.TooltipConfig
import net.minecraft.component.type.AttributeModifiersComponent
import net.minecraft.entity.EquipmentSlotGroup
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
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
import org.teamvoided.starborn_soundscape.entity.ToxicCloudEntity
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeDataComponents
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeEffects
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeItems
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeParticles
import org.teamvoided.starborn_soundscape.item.song_selection.ToolSongHoldingItem
import org.teamvoided.starborn_soundscape.item.songs.AllEyesSongItem
import org.teamvoided.starborn_soundscape.item.songs.BurningAndBlazeSongItem
import org.teamvoided.starborn_soundscape.item.songs.InMyElementSongItem
import org.teamvoided.starborn_soundscape.item.songs.KeepUpSongItem
import org.teamvoided.starborn_soundscape.item.songs.ToxicitySongItem
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
            Text.translatable("tooltip.soundscape.toggle.tooltip").formatted(Formatting.GRAY)
        )
        super.appendTooltip(stack, context, tooltip, config)
    }

    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        val stack = user.getStackInHand(hand)
        val tracker = AxeBassTracker.get(user)

        if (user.isSneaking) {
            if (!world.isClient) {
                tracker.metronomeEnabled = !tracker.metronomeEnabled

                user.sendMessage(
                    Text.literal(
                        if (tracker.metronomeEnabled) "Metronome ON"
                        else "Metronome OFF"
                    ).formatted(Formatting.GRAY),
                    true
                )
            }
            return TypedActionResult.success(stack)
        }
        val charge = stack.getOrDefault(
            StarbornSoundscapeDataComponents.METRONOME_CHARGE_DATA,
            MetronomeChargeData.DEFAULT
        ).charge
        if (!world.isClient) {
            if (charge >= 64) {
                stack.set(StarbornSoundscapeDataComponents.METRONOME_CHARGE_DATA, MetronomeChargeData(0))

                if (getSongItem(stack) is KeepUpSongItem) {
                    keepUpLaunch(world, user, stack)
                } else {
                    emitShockwave(world, user, stack)
                }

                useMetronomeSong(stack, user, world)
            }

        } else {
            return TypedActionResult.fail(user.getStackInHand(hand))
        }
        return TypedActionResult.success(user.getStackInHand(hand))
    }

    private fun keepUpLaunch(world: World, user: PlayerEntity, stack: ItemStack) {
        val look = user.rotationVector.normalize()

        val horizontalBoost = 1.5
        val verticalBoost = 1.0

        user.addVelocity(
            look.x * horizontalBoost,
            verticalBoost,
            look.z * horizontalBoost
        )

        user.velocityDirty = true
        user.velocityModified = true

        val tracker = AxeBassTracker.get(user)
        tracker.pendingSlam = true

        world.playSound(
            null,
            user.blockPos,
            SoundEvents.ENTITY_ENDER_DRAGON_FLAP,
            SoundCategory.PLAYERS,
            1.0f,
            1.2f
        )
    }

    private fun emitShockwave(world: World, user: PlayerEntity, stack: ItemStack) {

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

                if (getSongItem(stack) is BurningAndBlazeSongItem) {
                    serverWorld.spawnParticles(
                        ParticleTypes.LAVA,
                        x, y, z,
                        1, 0.0, 0.0, 0.0, 0.1
                    )
                    serverWorld.spawnParticles(
                        ParticleTypes.EXPLOSION,
                        x, y, z,
                        1, 0.0, 0.0, 0.0, 0.1
                    )
                } else if (getSongItem(stack) is AllEyesSongItem) {
                    serverWorld.spawnParticles(
                        ParticleTypes.FIREWORK,
                        x, y, z,
                        1, 0.0, 0.0, 0.0, 0.1
                    )
                    serverWorld.spawnParticles(
                        ParticleTypes.FLASH,
                        x, y, z,
                        1, 0.0, 0.0, 0.0, 0.1
                    )
                } else if (getSongItem(stack) is ToxicitySongItem) {
                    serverWorld.spawnParticles(
                        ParticleTypes.SCULK_SOUL,
                        x, y, z,
                        1, 0.0, 0.0, 0.0, 0.1
                    )
                    serverWorld.spawnParticles(
                        StarbornSoundscapeParticles.TOXIC_POOF,
                        x, y, z,
                        1, 0.0, 0.0, 0.0, 0.1
                    )
                    serverWorld.spawnParticles(
                        ParticleTypes.FLAME,
                        x, y, z,
                        1, 0.0, 0.0, 0.0, 0.1
                    )
                    serverWorld.spawnParticles(
                        ParticleTypes.LARGE_SMOKE,
                        x, y, z,
                        1, 0.0, 0.0, 0.0, 0.1
                    )
                } else if (getSongItem(stack) is InMyElementSongItem) {
                    serverWorld.spawnParticles(
                        ParticleTypes.FLAME,
                        x, y, z,
                        1, 0.0, 0.0, 0.0, 0.1
                    )
                    serverWorld.spawnParticles(
                        ParticleTypes.LARGE_SMOKE,
                        x, y, z,
                        1, 0.0, 0.0, 0.0, 0.1
                    )
                    serverWorld.spawnParticles(
                        ParticleTypes.LAVA,
                        x, y, z,
                        1, 0.0, 0.0, 0.0, 0.1
                    )
                    serverWorld.spawnParticles(
                        ParticleTypes.SMALL_FLAME,
                        x, y, z,
                        1, 0.0, 0.0, 0.0, 0.1
                    )
                } else {
                    serverWorld.spawnParticles(
                        ParticleTypes.SCULK_SOUL,
                        x, y, z,
                        1, 0.0, 0.0, 0.0, 0.0
                    )
                    serverWorld.spawnParticles(
                        ParticleTypes.LAVA,
                        x, y, z,
                        1, 0.0, 0.0, 0.0, 0.1
                    )
                    serverWorld.spawnParticles(
                        ParticleTypes.LARGE_SMOKE,
                        x, y, z,
                        1, 0.0, 0.0, 0.0, 0.1
                    )
                }
            }
        }

        val hasBurningSong = getSongItem(stack) is BurningAndBlazeSongItem
        val hasInMyElementSong = getSongItem(stack) is InMyElementSongItem
        val hasToxicitySong = getSongItem(stack) is ToxicitySongItem

        if (hasToxicitySong && world is ServerWorld) {
            val cloud = ToxicCloudEntity(world, user)
            cloud.setPosition(user.x, user.y, user.z)

            cloud.owner = user
            cloud.isSmall = false

            world.spawnEntity(cloud)
        }


        for (entity in entities) {
            val dir = user.pos.subtract(entity.pos).normalize()

            entity.damage(world.damageSources.playerAttack(user), SHOCKWAVE_DAMAGE)
            entity.addVelocity(dir.x * SHOCKWAVE_PULL_STRENGTH, 0.75, dir.z * SHOCKWAVE_PULL_STRENGTH)
            entity.velocityDirty = true

            if (hasBurningSong && entity is LivingEntity && entity.isOnFire) {

                if (entity == user) continue

                if (entity.hasStatusEffect(StarbornSoundscapeEffects.BAND_APPROVED)) continue

                world.createExplosion(
                    null,
                    entity.x,
                    entity.y,
                    entity.z,
                    2.0f,
                    World.ExplosionSourceType.NONE
                )

                entity.setOnFireFor(150)
            }
            if (hasInMyElementSong && entity is LivingEntity) {
                val center = user.blockPos
                val radius = SHOCKWAVE_RADIUS.toInt()
                val fireState = Blocks.FIRE.defaultState

                for (x in -radius..radius) {
                    for (z in -radius..radius) {
                        if (x * x + z * z <= radius * radius) {

                            for (yOffset in -1..1) {
                                val pos = center.add(x, yOffset, z)

                                if (world.getBlockState(pos).isAir &&
                                    fireState.canPlaceAt(world, pos)
                                ) {
                                    world.setBlockState(pos, fireState)
                                }
                            }
                        }
                    }
                }
                user.addStatusEffect(
                    StatusEffectInstance(
                        StatusEffects.FIRE_RESISTANCE,
                        600,
                        0
                    )
                )
                if (entity.hasStatusEffect(StarbornSoundscapeEffects.BAND_APPROVED)){
                    entity.addStatusEffect(
                        StatusEffectInstance(
                            StatusEffects.FIRE_RESISTANCE,
                            600,
                            0
                        )
                    )
                }
            }
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
                if (getSongItem(stack) is BurningAndBlazeSongItem && target is LivingEntity){
                    target.setOnFireFor(100)
                }
            }
        }
        return super.postHit(stack, target, attacker)
    }

    fun tick(player: PlayerEntity) {
        val tracker = AxeBassTracker.get(player)

        if (tracker.pendingSlam && player.isOnGround) {
            tracker.pendingSlam = false

            if (!player.world.isClient) {
                val stack = player.mainHandStack

                if (stack.item is AxeBassItem) {
                    (stack.item as AxeBassItem).emitShockwave(player.world, player, stack)
                }
            }
        }
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
