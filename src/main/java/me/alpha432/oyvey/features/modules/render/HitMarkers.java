package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.event.events.PacketEvent;
import me.alpha432.oyvey.event.events.Render2DEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.Timer;
import me.alpha432.oyvey.util.ColorUtil;
import me.alpha432.oyvey.util.RenderUtil;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HitMarkers extends Module {

    public HitMarkers() {
        super("Hitmarkers", "Shows safe spots.", Module.Category.RENDER, false, false, false);
    }

    Setting<Integer> time = register(new Setting("Time", 50, 0, 1000));
    Setting<Float> size = register(new Setting("Size", 6.0f, 0.0f, 8.0f));
    Timer timer = new Timer();

    @Override
    public void onDisable() {
        timer.reset();
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send e) {
        if (isDisabled() || fullNullCheck()) {
            return;
        }
        if (e.getPacket() instanceof CPacketUseEntity) {
            CPacketUseEntity p = e.getPacket();
            if (p.getAction() == CPacketUseEntity.Action.ATTACK) {
                timer.reset();
            }
        }
    }

    @Override
    public void onRender2D(Render2DEvent e) {
        if (fullNullCheck()) {
            return;
        }
        if (!timer.passedMs(time.getValue())) {
            ScaledResolution resolution = new ScaledResolution(mc);
            RenderUtil.drawLine(resolution.getScaledWidth() / 2.0f - 4.0f, resolution.getScaledHeight() / 2.0f - 4.0f, resolution.getScaledWidth() / 2.0f - size.getValue(), resolution.getScaledHeight() / 2.0f - size.getValue(), 1.0f, ColorUtil.toRGBA(255, 255, 255, 255));
            RenderUtil.drawLine(resolution.getScaledWidth() / 2.0f + 4.0f, resolution.getScaledHeight() / 2.0f - 4.0f, resolution.getScaledWidth() / 2.0f + size.getValue(), resolution.getScaledHeight() / 2.0f - size.getValue(), 1.0f, ColorUtil.toRGBA(255, 255, 255, 255));
            RenderUtil.drawLine(resolution.getScaledWidth() / 2.0f - 4.0f, resolution.getScaledHeight() / 2.0f + 4.0f, resolution.getScaledWidth() / 2.0f - size.getValue(), resolution.getScaledHeight() / 2.0f + size.getValue(), 1.0f, ColorUtil.toRGBA(255, 255, 255, 255));
            RenderUtil.drawLine(resolution.getScaledWidth() / 2.0f + 4.0f, resolution.getScaledHeight() / 2.0f + 4.0f, resolution.getScaledWidth() / 2.0f + size.getValue(), resolution.getScaledHeight() / 2.0f + size.getValue(), 1.0f, ColorUtil.toRGBA(255, 255, 255, 255));
        }
    }
}