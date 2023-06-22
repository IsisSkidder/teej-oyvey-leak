package me.alpha432.oyvey.mixin.mixins;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.modules.render.SwingAnimation;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityLivingBase.class)
public class MixinEntityLivingBase {
    @Inject(method = "getArmSwingAnimationEnd",at = @At(value = "HEAD"), cancellable = true)
    public void getArmSwingAnimationEndHook(CallbackInfoReturnable<Integer> cir) {
        int stuff = OyVey.moduleManager.getModuleByClass(SwingAnimation.class).isEnabled() ? SwingAnimation.changeSwing.getValue() : 6;
        cir.setReturnValue(stuff);
    }
}