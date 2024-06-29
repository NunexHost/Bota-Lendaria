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
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
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
                            checkSonicShield(player);
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

    private void checkSonicShield(ServerPlayerEntity player) {
        ItemStack shield = player.getStackInHand(Hand.OFF_HAND);
        if (Registries.ITEM.getId(shield.getItem()).toString().equals("minecraft:shield") && shield.get(DataComponentTypes.CUSTOM_DATA) != null) {
            NbtCompound nbt = shield.get(DataComponentTypes.CUSTOM_DATA).copyNbt();
            if (nbt.contains("legendary_shield")) {
                shield.useOn(player, player.world, Hand.OFF_HAND);
            }
        }
    }

    public static TypedActionResult<ItemStack> useShield(World world, ServerPlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        HitResult hitResult = user.raycast(4.5, 0, false);
        if (hitResult.getType() == HitResult.Type.BLOCK) {
            BlockHitResult blockHitResult = (BlockHitResult) hitResult;
            Vec3d vec3d = blockHitResult.getPos();
            world.playSound(null, vec3d.x, vec3d.y, vec3d.z, SoundEvents.WARDEN_SONIC_BOOM, SoundCategory.HOSTILE, 1.0f, 1.0f);
            return TypedActionResult.success(stack);
        }
        return TypedActionResult.pass(stack);
    }
}
