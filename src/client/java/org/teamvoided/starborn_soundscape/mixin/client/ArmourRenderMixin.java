package org.teamvoided.starborn_soundscape.mixin.client;


import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.hud.in_game.InGameHud;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.teamvoided.starborn_soundscape.StarbornSoundscape;
import org.teamvoided.starborn_soundscape.init.StarbornSoundscapeEffects;

@Mixin(InGameHud.class)
public class ArmourRenderMixin {

    @Unique
    private static final Identifier ARMOR_EMPTY = StarbornSoundscape.id("hud/corroded_armor_empty");
    @Unique
    private static final Identifier ARMOR_HALF = StarbornSoundscape.id("hud/corroded_armor_half");
    @Unique
    private static final Identifier ARMOR_FULL = StarbornSoundscape.id("hud/corroded_armor_full");

    @Inject(method = "renderArmorBar", at = @At("HEAD"), cancellable = true)
    private static void renderArmorBar(GuiGraphics graphics, PlayerEntity player, int y, int uncappedMaxHealth, int cappedMaxHealth, int x, CallbackInfo ci) {
        if (player != null && player.hasStatusEffect(StarbornSoundscapeEffects.INSTANCE.getCORROSION())) {
            int i = player.getArmor();
            if (i > 0) {
                RenderSystem.enableBlend();
                int j = y - (uncappedMaxHealth - 1) * cappedMaxHealth - 10;

                for(int k = 0; k < 10; ++k) {
                    int l = x + k * 8;
                    if (k * 2 + 1 < i) {
                        graphics.drawGuiTexture(ARMOR_FULL, l, j, 9, 9);
                    }

                    if (k * 2 + 1 == i) {
                        graphics.drawGuiTexture(ARMOR_HALF, l, j, 9, 9);
                    }

                    if (k * 2 + 1 > i) {
                        graphics.drawGuiTexture(ARMOR_EMPTY, l, j, 9, 9);
                    }
                }

                RenderSystem.disableBlend();
            }
            ci.cancel();
        }
    }

}
