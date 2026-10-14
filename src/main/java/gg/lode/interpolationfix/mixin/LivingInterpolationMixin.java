package gg.lode.interpolationfix.mixin;

import net.minecraft.world.entity.InterpolationHandler;
import net.minecraft.world.entity.LinearInterpolationHandler;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Puts living entities back on the 26.2 interpolation: a fixed three ticks toward the newest
 * position.
 *
 * <p>26.3's stepped interpolation lasts {@code updateInterval} ticks, so for players (2) and most
 * mobs it runs out before the next move packet lands and the entity stands still for a tick, which
 * reads as stutter (MC-311974, MC-311976). Three ticks over a two-tick packet rate leaves the
 * spare tick vanilla used to have. Only the client side is swapped: the integrated server still
 * needs the stepped tracker its {@code ServerEntity} reads paths from.
 */
@Mixin(LivingEntity.class)
public class LivingInterpolationMixin {
    @Inject(method = "createInterpolationHandler", at = @At("HEAD"), cancellable = true)
    private void interpolationfix$linearInterpolation(CallbackInfoReturnable<InterpolationHandler> cir) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (self.level().isClientSide()) {
            cir.setReturnValue(LinearInterpolationHandler.create(self));
        }
    }
}
