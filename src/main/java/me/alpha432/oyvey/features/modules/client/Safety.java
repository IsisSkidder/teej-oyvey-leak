package me.alpha432.oyvey.features.modules.client;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.alpha432.oyvey.util.BlockUtil;
import me.alpha432.oyvey.util.RenderUtil;
import me.alpha432.oyvey.features.modules.Module;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class Safety
        extends Module {
    SafetyMode safety;

    public Safety() {
        super("Safety", "safeeeeeee", Category.CLIENT, true, false, false);
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if (RenderUtil.nullCheck()) {
            return;
        }
        if (Safety.mc.world.getBlockState(Safety.mc.player.getPosition()).getMaterial().isSolid()) {
            this.safety = SafetyMode.Safe;
            return;
        }
        if (BlockUtil.isHole(Safety.mc.player.getPosition())) {
            this.safety = SafetyMode.Safe;
            return;
        }
        this.safety = SafetyMode.Unsafe;
    }

    @Override
    public String getDisplayInfo() {
        return this.safety != null ? this.safety.toString() : "";
    }

    static enum SafetyMode {
        Safe(ChatFormatting.GREEN),
        Unsafe(ChatFormatting.RED);

        ChatFormatting color;

        private SafetyMode(ChatFormatting color) {
            this.color = color;
        }

        public String toString() {
            return this.color.toString() + super.toString();
        }
    }
}