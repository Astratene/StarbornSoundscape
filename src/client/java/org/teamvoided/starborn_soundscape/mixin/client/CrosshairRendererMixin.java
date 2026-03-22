package org.teamvoided.starborn_soundscape.mixin.client;


import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.hud.in_game.InGameHud;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.DeltaTracker;
import net.minecraft.item.Item;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.teamvoided.starborn_soundscape.StarbornSoundscape;
import org.teamvoided.starborn_soundscape.item.AxeBassItem;
import org.teamvoided.starborn_soundscape.item.OverarchieverItem;
import org.teamvoided.starborn_soundscape.item.tracker.AxeBassTracker;

@Mixin(InGameHud.class)
public class CrosshairRendererMixin {

    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(method = "renderCrosshair", at = @At("HEAD"), cancellable = true)
    private void overArchIeve(GuiGraphics graphics, DeltaTracker tracker, CallbackInfo ci){
        ClientPlayerEntity player = this.client.player;
        if (player != null && getOverArchIeverHand(player)){
            ci.cancel();
        }
        else {
            return;
        }
    }

    @Unique
    private boolean getOverArchIeverHand(ClientPlayerEntity player) {
        Item heldItem = player.getStackInHand(Hand.MAIN_HAND).getItem();
        Item offhandItem = player.getStackInHand(Hand.OFF_HAND).getItem();
        if (heldItem instanceof OverarchieverItem){
            return true;
        }
        else return offhandItem instanceof OverarchieverItem;
    }

    /*
    @Inject(method = "renderCrosshair", at = @At("HEAD"), cancellable = true)
    private void metroNome(GuiGraphics graphics, DeltaTracker tracker, CallbackInfo ci){
        ClientPlayerEntity player = this.client.player;
        if (player != null && getMetronomeHand(player)){
            ci.cancel();
        }
        else {
            return;
        }
    }

    @Unique
    private boolean getMetronomeHand(ClientPlayerEntity player) {
        Item heldItem = player.getStackInHand(Hand.MAIN_HAND).getItem();
        Item offhandItem = player.getStackInHand(Hand.OFF_HAND).getItem();
        if (heldItem instanceof AxeBassItem){
            return true;
        }
        else return offhandItem instanceof AxeBassItem;
    }

     */
}
