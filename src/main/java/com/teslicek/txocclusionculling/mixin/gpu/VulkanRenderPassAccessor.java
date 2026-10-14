package com.teslicek.txocclusionculling.mixin.gpu;

import com.mojang.renderpearl.backend.vulkan.VulkanDevice;
import com.mojang.renderpearl.backend.vulkan.VulkanRenderPass;
import org.lwjgl.vulkan.VkCommandBuffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(VulkanRenderPass.class)
public interface VulkanRenderPassAccessor {

    @Accessor("commandBuffer")
    VkCommandBuffer txocclusionculling$commandBuffer();

    @Accessor("device")
    VulkanDevice txocclusionculling$device();
}
