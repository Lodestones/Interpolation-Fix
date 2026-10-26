package gg.lode.interpolationfix.mixin;

import gg.lode.interpolationfix.InterpolationFix;
import java.util.Arrays;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Options;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Adds the toggle to the Controls screen without Fabric API. It goes in before {@code load()} so
 * the player's binding is read back from options.txt.
 */
@Mixin(Options.class)
public class OptionsMixin {
    @Shadow
    @Final
    @Mutable
    public KeyMapping[] keyMappings;

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Options;load()V"))
    private void interpolationfix$addToggleKey(CallbackInfo ci) {
        KeyMapping[] mappings = Arrays.copyOf(this.keyMappings, this.keyMappings.length + 1);
        mappings[mappings.length - 1] = InterpolationFix.TOGGLE_KEY;
        this.keyMappings = mappings;
    }
}
