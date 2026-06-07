package top.fifthlight.touchcontroller.neoforge.v1_21_10.mixin;

import net.neoforged.neoforge.client.settings.KeyModifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.fifthlight.touchcontroller.neoforge.v1_21_10.TouchController;

import java.util.List;

@Mixin(KeyModifier.class)
public class KeyModifierMixin {
    @Inject(method = "getActiveModifiers", at = @At("HEAD"), cancellable = true, remap = false)
    private static void overrideGetActiveModifiers(CallbackInfoReturnable<List<KeyModifier>> cir) {
        var modifier = TouchController.getCurrentModifier();
        if (modifier != null) {
            cir.setReturnValue(List.of(modifier));
        }
    }
}
