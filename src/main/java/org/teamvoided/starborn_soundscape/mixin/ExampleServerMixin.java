package org.teamvoided.starborn_soundscape.mixin;

import com.mojang.datafixers.DataFixer;
import net.minecraft.resource.pack.PackManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.Services;
import net.minecraft.server.WorldGenerationProgressListenerFactory;
import net.minecraft.server.WorldStem;

import net.minecraft.world.storage.WorldSaveStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.net.Proxy;

import static org.teamvoided.starborn_soundscape.StarbornSoundscape.log;

@Mixin(MinecraftServer.class)
public class ExampleServerMixin {

    @Inject(method = "<init>", at = @At("TAIL"))
    private static void run(Thread serverThread, WorldSaveStorage.Session session, PackManager dataPackManager, WorldStem worldStem, Proxy proxy, DataFixer dataFixer, Services services, WorldGenerationProgressListenerFactory worldGenerationProgressListenerFactory, CallbackInfo ci) {
        log.info("Hello from server Mixin");
    }
}
