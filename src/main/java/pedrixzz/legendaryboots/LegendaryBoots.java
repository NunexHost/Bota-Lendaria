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
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class LegendaryBoots implements ModInitializer {

    private int tick = 0;

    @Override
    public void onInitialize() {
        ServerTickEvents.END_SERVER_TICK.register(
                (listener) -> {
                    if (tick >= 20) {
                        for (ServerPlayerEntity player : listener.getPlayerManager().getPlayerList()) {
                            checkFeather(player);
                            checkBoots(player);
                            checkShield(player); // Check for legendary shield
                        }
                        tick = 0;
                    }
                    tick++;
                }
        );
    }

    private void checkFeather(ServerPlayerEntity player) {
        for (ItemStack itemStack : player.getHandItems()) {
            if (Registries.ITEM.getId(itemStack.getItem()).toString().equals("minecraft:feather") && itemStack.get(DataComponentTypes.CUSTOM_DATA) != null) {
                NbtCompound nbt = itemStack.get(DataComponentTypes.CUSTOM_DATA).copyNbt();
                if (nbt.contains("legendary_feather")) {
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.LEVITATION, 60, 0, false, true, false));
                }
            }
        }
    }

    private void checkBoots(ServerPlayerEntity player) {
        for (ItemStack itemStack : player.getAllArmorItems()) {
            if (Registries.ITEM.getId(itemStack.getItem()).toString().equals("minecraft:diamond_boots") && itemStack.get(DataComponentTypes.CUSTOM_DATA) != null) {
                NbtCompound nbt = itemStack.get(DataComponentTypes.CUSTOM_DATA).copyNbt();
                if (nbt.contains("legendary_boots")) {
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 60, 4, false, true, false));
                }
            }
        }
    }

    private void checkShield(ServerPlayerEntity player) {
        for (ItemStack itemStack : player.getHandItems()) {
            if (Registries.ITEM.getId(itemStack.getItem()).toString().equals("minecraft:shield") && itemStack.get(DataComponentTypes.CUSTOM_DATA) != null) {
                NbtCompound nbt = itemStack.get(DataComponentTypes.CUSTOM_DATA).copyNbt();
                if (nbt.contains("legendary_shield")) {
                    if (nbt.getBoolean("has_activated")) {
                        nbt.putBoolean("has_activated", false); // Reset after use
                        World world = player.getWorld();
                        world.playSound(null, player.getBlockPos(), SoundEvents.WARDEN_SONIC_BOOM, SoundCategory.PLAYERS, 1.0F, 1.0F);
                        // Simulate the Warden's Sonic Boom
                        Vec3d playerPos = player.getPos();
                        world.getEntitiesByClass(ServerPlayerEntity.class, player.getBoundingBox().expand(10), entity -> entity != player).forEach(targetPlayer -> {
                            targetPlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 200, 2, false, true, false));
                            targetPlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 100, 0, false, true, false));
                            targetPlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 100, 0, false, true, false));
                            targetPlayer.teleport(playerPos.add(0, 2, 0)); // Move target player slightly upwards
                        });
                    }
                    itemStack.set(DataComponentTypes.CUSTOM_DATA, nbt); // Update NBT
                }
            }
        }
    }
}
