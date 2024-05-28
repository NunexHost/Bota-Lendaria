package pedrixzz.legendaryboots;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.component.DataComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TextCodecs;

import java.util.function.UnaryOperator;

public class LegendaryBoots implements ModInitializer {

    @Override
    public void onInitialize() {
        ServerTickEvents.END_SERVER_TICK.register(
                (listener) -> {
                    for(ServerPlayerEntity player: listener.getPlayerManager().getPlayerList()){
                        //player.getMainHandStack();
                    }
                }
        );
    }
}
