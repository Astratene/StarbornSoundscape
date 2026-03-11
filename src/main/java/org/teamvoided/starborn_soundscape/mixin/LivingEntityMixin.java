package org.teamvoided.starborn_soundscape.mixin;

import net.minecraft.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static org.teamvoided.starborn_soundscape.util.MiscFuncsKt.disablesShields;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "canDisableShield", at = @At("HEAD"), cancellable = true)
    public void canDisableShield(CallbackInfoReturnable<Boolean> cir) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (entity != null) {
            boolean disables = disablesShields(entity);
            if (disables) cir.setReturnValue(true);
        }
    }

}
