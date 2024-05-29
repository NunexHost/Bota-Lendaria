package pedrixzz.legendaryboots;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;

public class LegendaryBoots implements ModInitializer {

    private int tick = 0;

    @Override
    public void onInitialize() {
        ServerTickEvents.END_SERVER_TICK.register(
                (listener) -> {
                    if(tick >= 20) {
                        for (ServerPlayerEntity player : listener.getPlayerManager().getPlayerList()) {
                            checkFeather(player);
                            checkBoots(player);
                        }
                        tick = 0;
                    }
                    tick++;
                }
        );
    }

    private void checkFeather(ServerPlayerEntity player){
        for(ItemStack itemStack: player.getHandItems()) {
            if (Registries.ITEM.getId(itemStack.getItem()).toString().equals("minecraft:feather") && itemStack.get(DataComponentTypes.CUSTOM_DATA) != null) {
                NbtCompound nbt = itemStack.get(DataComponentTypes.CUSTOM_DATA).copyNbt();
                if (nbt.contains("legendary_feather")) {
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.LEVITATION, 60, 0, false, true, false));
                }
            }
        }
    }

    private void checkBoots(ServerPlayerEntity player){
        for(ItemStack itemStack: player.getAllArmorItems()){
            if(Registries.ITEM.getId(itemStack.getItem()).toString().equals("minecraft:diamond_boots") && itemStack.get(DataComponentTypes.CUSTOM_DATA) != null){
                NbtCompound nbt = itemStack.get(DataComponentTypes.CUSTOM_DATA).copyNbt();
                if(nbt.contains("legendary_boots")){
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 60, 4, false, true, false));
                }
            }
        }
    }
}
