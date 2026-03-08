package org.teamvoided.starborn_soundscape.client.init

import com.mojang.blaze3d.systems.RenderSystem
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.client.render.DeltaTracker
import net.minecraft.util.Hand
import org.teamvoided.starborn_soundscape.StarbornSoundscape.id
import org.teamvoided.starborn_soundscape.components.CurrentUseTime
import org.teamvoided.starborn_soundscape.components.OverarchieverData
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeDataComponents
import org.teamvoided.starborn_soundscape.item.overarchieverItem
import kotlin.math.min
import kotlin.math.roundToInt

object StarbornHudRendering {

    fun init() = HudRenderCallback.EVENT.register(::hudRenderer)

    private var rightIconTicks = 0
    private var leftIconTicks = 0
    private var lowerIconTicks = 0

    @JvmField
    var RenderingOver = false

    fun hudRenderer(graphics: GuiGraphics, @Suppress("UNUSED_PARAMETER") deltaTracker: DeltaTracker) {
        val client = MinecraftClient.getInstance() ?: return
        val player = client.player ?: return
        if (client.options.hudHidden) return
        if (!client.options.perspective.isFirstPerson) return

        if (rightIconTicks > 0) rightIconTicks--
        if (leftIconTicks > 0) leftIconTicks--
        if (lowerIconTicks > 0) lowerIconTicks--

        graphics.matrices.push()
        RenderSystem.enableBlend()

        graphics.renderOverArchIeverCircle(player)

        RenderSystem.disableBlend()
        graphics.matrices.pop()
    }

    private fun GuiGraphics.renderOverArchIeverCircle(player: ClientPlayerEntity) {
        val hand = getOverArchIeverHand(player)
        if (hand != null) {
            this.drawGuiTexture(
                id("hud/overarchiever_uncharged"),
                (this.scaledWindowWidth / 2) - 7,
                (this.scaledWindowHeight / 2) - 7,
                15,
                15
            )
            val stack = player.getStackInHand(hand)
            if (stack.item is overarchieverItem) {
                val item = stack.item as overarchieverItem
                var charge = stack.getOrDefault(
                    StarbornSoundscapeDataComponents.CURRENT_USE_TIME,
                    CurrentUseTime.DEFAULT
                ).useTime
                val smallRing = item.getMinChargeTicks(player, stack)
                val bigRing = item.getChargeTicks(player, stack)

                if (charge >= smallRing) {
                    this.drawGuiTexture(
                        id("hud/overarchiever_half_charged"),
                        (this.scaledWindowWidth / 2) - 7,
                        (this.scaledWindowHeight / 2) - 7,
                        15,
                        15
                    )
                    charge -= smallRing
                    if (charge >= (bigRing - smallRing)) {
                        this.drawGuiTexture(
                            id("hud/overarchiever_full_charged"),
                            (this.scaledWindowWidth / 2) - 7,
                            (this.scaledWindowHeight / 2) - 7,
                            15,
                            15
                        )
                    } else {
                        var bigRingPixels = (((charge.toFloat()) / (bigRing.toFloat() - smallRing.toFloat())) * 40f).roundToInt()
                        repeat(min(bigRingPixels, 6)) {
                            this.drawGuiTexture(
                                id("hud/full_charge_full_dot"),
                                ((this.scaledWindowWidth / 2)) + it,
                                ((this.scaledWindowHeight / 2)) - 5,
                                1,
                                1
                            )
                        }
                        bigRingPixels -= 6
                        if (bigRingPixels > 0){
                            repeat(min(bigRingPixels, 10)) {
                                this.drawGuiTexture(
                                    id("hud/full_charge_full_dot"),
                                    ((this.scaledWindowWidth / 2)) + 5,
                                    ((this.scaledWindowHeight / 2) - 4) + it,
                                    1,
                                    1
                                )
                            }
                            bigRingPixels -= 10
                            if (bigRingPixels > 0){
                                repeat(min(bigRingPixels, 10)) {
                                    this.drawGuiTexture(
                                        id("hud/full_charge_full_dot"),
                                        ((this.scaledWindowWidth / 2) + 4) - it,
                                        ((this.scaledWindowHeight / 2) + 5),
                                        1,
                                        1
                                    )
                                }
                                bigRingPixels -= 10
                                if (bigRingPixels > 0){
                                    repeat(min(bigRingPixels, 10)) {
                                        this.drawGuiTexture(
                                            id("hud/full_charge_full_dot"),
                                            ((this.scaledWindowWidth / 2) - 5),
                                            ((this.scaledWindowHeight / 2) + 4) - it,
                                            1,
                                            1
                                        )
                                    }
                                    bigRingPixels -= 10
                                    if (bigRingPixels > 0){
                                        repeat(min(bigRingPixels, 4)) {
                                            this.drawGuiTexture(
                                                id("hud/full_charge_full_dot"),
                                                ((this.scaledWindowWidth / 2) - 4) + it,
                                                ((this.scaledWindowHeight / 2) - 5),
                                                1,
                                                1
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    var smallRingPixels = ((charge.toFloat() / smallRing.toFloat()) * 32f).roundToInt()
                    repeat(min(smallRingPixels, 5)) {
                        this.drawGuiTexture(
                            id("hud/full_charge_half_dot"),
                            ((this.scaledWindowWidth / 2)) + it,
                            ((this.scaledWindowHeight / 2)) - 4,
                            1,
                            1
                        )
                    }
                    smallRingPixels -= 5
                    if (smallRingPixels > 0){
                        repeat(min(smallRingPixels, 8)) {
                            this.drawGuiTexture(
                                id("hud/full_charge_half_dot"),
                                ((this.scaledWindowWidth / 2)) + 4,
                                (((this.scaledWindowHeight / 2)) - 3) + it,
                                1,
                                1
                            )
                        }
                        smallRingPixels -= 8
                        if (smallRingPixels > 0){
                            repeat(min(smallRingPixels, 8)) {
                                this.drawGuiTexture(
                                    id("hud/full_charge_half_dot"),
                                    (((this.scaledWindowWidth / 2)) + 3) - it,
                                    (((this.scaledWindowHeight / 2)) + 4),
                                    1,
                                    1
                                )
                            }
                            smallRingPixels -= 8
                            if (smallRingPixels > 0){
                                repeat(min(smallRingPixels, 8)) {
                                    this.drawGuiTexture(
                                        id("hud/full_charge_half_dot"),
                                        (((this.scaledWindowWidth / 2)) - 4),
                                        (((this.scaledWindowHeight / 2)) + 3) - it,
                                        1,
                                        1
                                    )
                                }
                                smallRingPixels -= 8
                                if (smallRingPixels > 0){
                                    repeat(min(smallRingPixels, 3)) {
                                        this.drawGuiTexture(
                                            id("hud/full_charge_half_dot"),
                                            (((this.scaledWindowWidth / 2)) - 3) + it,
                                            (((this.scaledWindowHeight / 2)) - 4),
                                            1,
                                            1
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public fun getOverArchIeverHand(player: ClientPlayerEntity): Hand? {
        val heldItem = player.getStackInHand(Hand.MAIN_HAND)
        val offhandItem = player.getStackInHand(Hand.OFF_HAND)
        if (heldItem.item is overarchieverItem) {
            return Hand.MAIN_HAND
        } else if (offhandItem.item is overarchieverItem) {
            return Hand.OFF_HAND
        } else return null
    }

}