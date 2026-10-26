package gg.lode.interpolationfix.mixin;

import gg.lode.interpolationfix.InterpolationFix;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Inject(method = "handleKeybinds", at = @At("HEAD"))
    private void interpolationfix$handleToggle(CallbackInfo ci) {
        InterpolationFix.handleKeybind((Minecraft) (Object) this);
    }
}
