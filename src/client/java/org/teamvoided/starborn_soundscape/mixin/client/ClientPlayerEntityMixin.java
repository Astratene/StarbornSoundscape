package org.teamvoided.starborn_soundscape.mixin.client;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import static org.teamvoided.starborn_soundscape.util.MixinFunsKt.modifyItemUseSlowdown;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {
    public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    @ModifyConstant(method = "tickMovement", constant = @Constant(floatValue = 0.2f))
    private float setShieldUseDelay(float constant) {
        return modifyItemUseSlowdown(this.getActiveItem());
    }
}
