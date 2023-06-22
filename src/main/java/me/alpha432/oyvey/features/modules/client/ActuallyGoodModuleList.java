package me.alpha432.oyvey.features.modules.client;

import me.alpha432.oyvey.features.modules.*;
import me.alpha432.oyvey.features.setting.*;
import me.alpha432.oyvey.event.events.*;
import me.alpha432.oyvey.*;
import com.mojang.realmsclient.gui.*;
import java.awt.*;
import me.alpha432.oyvey.util.*;
import java.util.*;
import net.minecraftforge.fml.common.eventhandler.*;

public class ActuallyGoodModuleList extends Module
{
    Setting<Integer> mode;
    Setting<Float> saturation;
    Setting<Float> brightness;
    Setting<Integer> red;
    Setting<Integer> green;
    Setting<Integer> blue;
    LBFontRenderer renderer;

    public ActuallyGoodModuleList() {
        super("ModuleList", "i just hate oyvey's", Category.CLIENT, true, false, false);
        this.mode = (Setting<Integer>)this.register(new Setting("Mode", 0, 0, 1));
        this.saturation = (Setting<Float>)this.register(new Setting("Saturation", 0.9f, 0.0f, 1.0f));
        this.brightness = (Setting<Float>)this.register(new Setting("Brightness", 1.0f, 0.0f, 1.0f));
        this.red = (Setting<Integer>)this.register(new Setting("Red", 255, 0, 255));
        this.green = (Setting<Integer>)this.register(new Setting("Green", 255, 0, 255));
        this.blue = (Setting<Integer>)this.register(new Setting("Blue", 255, 0, 255));
    }

    @Override
    public void onEnable() {
        this.renderer = new LBFontRenderer(new Font("Arial", 0, 50));
    }

    @SubscribeEvent
    @Override
    public void onRender2D(final Render2DEvent event) {
        if (fullNullCheck()) {
            return;
        }
        int counter = 1;
        for (final Module module : OyVey.moduleManager.sortedModules) {
            if (this.mode.getValue() == 0) {
                final String display = module.getDisplayName() + ((module.getDisplayInfo() != null) ? (" " + ChatFormatting.GRAY + module.getDisplayInfo()) : "");
                final int color = SkyRainbow(counter * 100, this.saturation.getValue(), this.brightness.getValue());
                RenderUtil.drawRectangleCorrectly(0, 10 + counter * 11 - 2, ActuallyGoodModuleList.mc.fontRenderer.getStringWidth(display) + 4, ActuallyGoodModuleList.mc.fontRenderer.FONT_HEIGHT + 2, new Color(0, 0, 0, 100).getRGB());
                ActuallyGoodModuleList.mc.fontRenderer.drawString(display, 2, 10 + counter * 11, color);
                ++counter;
            }
            else {
                final String display = module.getDisplayName() + ((module.getDisplayInfo() != null) ? (" " + ChatFormatting.GRAY + module.getDisplayInfo()) : "");
                final int color = alphaStep(new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue()), 50, counter * 2 + 10).getRGB();
                ActuallyGoodModuleList.mc.fontRenderer.drawString(display, 2, 10 + counter * 11, color);
                ++counter;
            }
        }
    }

    public static Color alphaStep(final Color color, final int index, final int count) {
        final float[] hsb = new float[3];
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);
        float brightness = Math.abs((System.currentTimeMillis() % 2000L / 1000.0f + index / (float)count * 2.0f) % 2.0f - 1.0f);
        brightness = 0.5f + 0.5f * brightness;
        hsb[2] = brightness % 2.0f;
        return new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));
    }

    public static int SkyRainbow(final int var, final float bright, final float st) {
        double v1 = Math.ceil((double)(System.currentTimeMillis() + var * 109)) / 5.0;
        return Color.getHSBColor(((float)((v1 %= 360.0) / 360.0) < 0.5) ? (-(float)(v1 / 360.0)) : ((float)(v1 / 360.0)), st, bright).getRGB();
    }
}
