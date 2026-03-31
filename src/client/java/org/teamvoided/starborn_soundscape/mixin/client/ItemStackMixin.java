package org.teamvoided.starborn_soundscape.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Formatting;
import net.minecraft.util.Rarity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.teamvoided.starborn_soundscape.item.OverarchieverItem;
import org.teamvoided.starborn_soundscape.item.song_selection.SongItem;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Shadow
    public abstract Item getItem();

    @WrapOperation(method = "getTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Rarity;getFormatting()Lnet/minecraft/util/Formatting;", ordinal = 0))
    Formatting customRarityTooltipLines(Rarity instance, Operation<Formatting> original) {
        if (this.getItem() instanceof SongItem song) {
            return song.getNameColor();
        }
        if (this.getItem() instanceof OverarchieverItem bow){
            return Formatting.DARK_PURPLE;
        }
        return original.call(instance);
    }

    @WrapOperation(method = "toHoverableText", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Rarity;getFormatting()Lnet/minecraft/util/Formatting;", ordinal = 0))
    Formatting customRarityDisplayName(Rarity instance, Operation<Formatting> original) {
        if (this.getItem() instanceof SongItem song) {
            return song.getNameColor();
        }
        if (this.getItem() instanceof OverarchieverItem bow){
            return Formatting.DARK_PURPLE;
        }
        return original.call(instance);
    }


}
