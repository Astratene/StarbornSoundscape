package org.teamvoided.starborn_soundscape.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.hud.in_game.InGameHud;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Formatting;
import net.minecraft.util.Rarity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.teamvoided.starborn_soundscape.item.OverarchieverItem;
import org.teamvoided.starborn_soundscape.item.song_selection.SongItem;

@Mixin(InGameHud.class)
public class InGameHudMixin {

    @Shadow
    private ItemStack currentStack;

    @WrapOperation(method = "renderHeldItemTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Rarity;getFormatting()Lnet/minecraft/util/Formatting;", ordinal = 0))
    Formatting customRarityDisplayName(Rarity instance, Operation<Formatting> original) {
        if (currentStack.getItem() instanceof SongItem song) {
            return song.getNameColor();
        }
        if (currentStack.getItem() instanceof OverarchieverItem bow){
            return Formatting.DARK_PURPLE;
        }
        return original.call(instance);
    }
}
