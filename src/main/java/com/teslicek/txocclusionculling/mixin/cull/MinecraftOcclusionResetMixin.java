package com.teslicek.txocclusionculling.mixin.cull;

import com.teslicek.txocclusionculling.cull.OcclusionCuller;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MinecraftOcclusionResetMixin {

    @Inject(method = "setLevel", at = @At("TAIL"))
    private void txocclusionculling$resetOcclusion(CallbackInfo ci) {
        OcclusionCuller.reset();
    }
}
