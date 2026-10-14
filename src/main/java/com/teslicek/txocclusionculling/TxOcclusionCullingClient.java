package com.teslicek.txocclusionculling;

import com.teslicek.txocclusionculling.gpu.OcclusionProbe;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.minecraft.network.chat.Component;

public final class TxOcclusionCullingClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, context) -> dispatcher.register(ClientCommands.literal("txocclusion").executes(command -> {
            if (OcclusionProbe.isActive()) {
                command.getSource().sendError(Component.literal("Occlusion probe is already running"));

                return 0;
            }

            OcclusionProbe.arm();
            command.getSource().sendFeedback(Component.literal("Measuring hidden terrain in the next frame"));

            return 1;
        })));
    }
}
