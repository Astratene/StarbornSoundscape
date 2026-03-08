package org.teamvoided.starborn_soundscape.mixin.client;


import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.hud.in_game.InGameHud;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.DeltaTracker;
import net.minecraft.item.Item;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.teamvoided.starborn_soundscape.item.overarchieverItem;

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
        if (heldItem instanceof overarchieverItem){
            return true;
        }
        else return offhandItem instanceof overarchieverItem;
    }

}
