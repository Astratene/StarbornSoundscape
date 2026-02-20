package org.teamvoided.starborn_soundscape.mixin.client;

import net.minecraft.client.render.BackgroundRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(BackgroundRenderer.class)
public interface FogEffectAccessor {

    @Accessor("FOG_EFFECTS")
    static List<BackgroundRenderer.FogEffect> starbornFogEffects() {
        throw new IllegalStateException("fuck");
    };
}
