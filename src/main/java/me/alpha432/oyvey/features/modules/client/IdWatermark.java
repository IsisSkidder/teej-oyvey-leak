package me.alpha432.oyvey.features.modules.client;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.alpha432.oyvey.OyVey;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import me.alpha432.oyvey.event.events.Render2DEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.ColorUtil;

public class IdWatermark extends Module {
    public Setting<String> watermarkName = this.register(new Setting<>("Text", "OyVey", ""));
    public Setting<Boolean> versionSetting = register(new Setting<>("Version", true));


    public IdWatermark() {
        super("Id Watermark", "made by teej", Category.CLIENT, true, false, false);
    }

    public static IdWatermark INSTANCE = new IdWatermark();
    private int color;

    @Override
    public void onRender2D(Render2DEvent event) {
        float yOffset = OyVey.textManager.scaledHeight / 2.0f;
        float offset = OyVey.textManager.scaledHeight / 2.0f - 30.0f;

        if ((ClickGui.getInstance()).rainbow.getValue() && ClickGui.getInstance().rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
            this.color = ColorUtil.rainbow(((Integer) (ClickGui.getInstance()).rainbowHue.getValue()).intValue()).getRGB();
        } else {
            this.color = ColorUtil.toRGBA(ClickGui.getInstance().red.getValue(), ClickGui.getInstance().green.getValue(), ClickGui.getInstance().blue.getValue());
        }
        if (versionSetting.getValue()) {
            OyVey.textManager.drawString(watermarkName.getValue() + ChatFormatting.GRAY + " v0.0.5", 2.0f, offset, this.color, true);
        } else {
            OyVey.textManager.drawString(watermarkName.getValue(), 2.0f, offset, this.color, true);
        }
    }
}