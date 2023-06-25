package me.alpha432.oyvey.features.modules.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.alpha432.oyvey.features.command.Command;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import net.minecraft.init.MobEffects;

public class EffectAlert
        extends Module {
    public final Setting<Boolean> weakness = this.register(new Setting<Boolean>("Weakness", true));
    public final Setting<Boolean> slowness = this.register(new Setting<Boolean>("Slowness", true));
    public final Setting<Boolean> swiftness = this.register(new Setting<Boolean>("Swiftness", true));
    private boolean hasAnnouncedWeakness = false;
    private boolean hasAnnouncedSlowness = false;
    private boolean hasAnnouncedSwiftness = false;
    public EffectAlert() {
        super("EffectAlert", "Announces in chat when you have specific potion effects.", Category.MISC, true, false, false);
    }

    @Override
    public String onUpdate() {
        if (this.weakness.getValue().booleanValue()) {
            if (EffectAlert.mc.player.isPotionActive(MobEffects.WEAKNESS) && !this.hasAnnouncedWeakness) {
                Command.sendMessage("You now have " + (Object)ChatFormatting.DARK_GRAY + (Object)ChatFormatting.BOLD + "Weakness" + (Object)ChatFormatting.RESET + "!");
                this.hasAnnouncedWeakness = true;
            }
            if (!EffectAlert.mc.player.isPotionActive(MobEffects.WEAKNESS) && this.hasAnnouncedWeakness) {
                Command.sendMessage("You no longer have " + (Object)ChatFormatting.DARK_GRAY + (Object)ChatFormatting.BOLD + "Weakness" + (Object)ChatFormatting.RESET + "!");
                this.hasAnnouncedWeakness = false;
            }
        }
        if (this.slowness.getValue().booleanValue()) {
            if (EffectAlert.mc.player.isPotionActive(MobEffects.SLOWNESS) && !this.hasAnnouncedSlowness) {
                Command.sendMessage("You now have " + (Object)ChatFormatting.GRAY + (Object)ChatFormatting.BOLD + "Slowness" + (Object)ChatFormatting.RESET + "!");
                this.hasAnnouncedSlowness = true;
            }
            if (!EffectAlert.mc.player.isPotionActive(MobEffects.SLOWNESS) && this.hasAnnouncedSlowness) {
                Command.sendMessage("You no longer have " + (Object)ChatFormatting.GRAY + (Object)ChatFormatting.BOLD + "Slowness" + (Object)ChatFormatting.RESET + "!");
                this.hasAnnouncedSlowness = false;
            }
        }
        if (this.swiftness.getValue().booleanValue()) {
            if (EffectAlert.mc.player.isPotionActive(MobEffects.SPEED) && !this.hasAnnouncedSwiftness) {
                Command.sendMessage("You now have " + (Object) ChatFormatting.AQUA + (Object) ChatFormatting.BOLD + "Speed" + (Object) ChatFormatting.RESET + "!");
                this.hasAnnouncedSwiftness = true;
            }
            if (!EffectAlert.mc.player.isPotionActive(MobEffects.SPEED) && this.hasAnnouncedSwiftness) {
                Command.sendMessage("You no longer have " + (Object) ChatFormatting.AQUA + (Object) ChatFormatting.BOLD + "Speed" + (Object) ChatFormatting.RESET + "!");
                this.hasAnnouncedSwiftness = false;
            }
        }
        return null;
    }
}