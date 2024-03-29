package me.alpha432.oyvey.features.modules.client;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.alpha432.oyvey.OyVey;
import java.awt.Color;
import me.alpha432.oyvey.event.events.ClientEvent;
import me.alpha432.oyvey.features.command.Command;
import me.alpha432.oyvey.features.gui.OyVeyGui;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import net.minecraft.client.settings.GameSettings;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ClickGui
        extends Module {
    private static ClickGui INSTANCE = new ClickGui();
    public Setting<String> prefix = this.register(new Setting<String>("Prefix", "."));
    public Setting<Boolean> customFov = this.register(new Setting<Boolean>("CustomFov", false));
    public Setting<Boolean> descriptions = this.register(new Setting<Boolean>("Descriptions", true));
    public Setting<Boolean> snowing = this.register(new Setting<Boolean>("Snowing", true));
    public Setting<Float> fov = this.register(new Setting<Float>("Fov", Float.valueOf(150.0f), Float.valueOf(-180.0f), Float.valueOf(180.0f)));
    public Setting<Integer> red = this.register(new Setting<Integer>("Red", 0, 0, 255));
    public Setting<Integer> green = this.register(new Setting<Integer>("Green", 0, 0, 255));
    public Setting<Integer> blue = this.register(new Setting<Integer>("Blue", 255, 0, 255));
    public Setting<Integer> hoverAlpha = this.register(new Setting<Integer>("Alpha", 180, 0, 255));
    public Setting<Integer> topRed = this.register(new Setting<Integer>("SecondRed", 0, 0, 255));
    public Setting<String> moduleButton = this.register(new Setting("Buttons", ""));
    public Setting<Boolean> colorSync = this.register(new Setting("DontClickYouWillCrash", false));
    public Setting<Integer> backgroundAlpha = this.register(new Setting("BackgroundAlpha", 140, 0, 255));
    public Setting<Integer> topGreen = this.register(new Setting<Integer>("SecondGreen", 0, 0, 255));
    public Setting<Integer> topBlue = this.register(new Setting<Integer>("SecondBlue", 150, 0, 255));
    public Setting<Integer> alpha = this.register(new Setting<Integer>("HoverAlpha", 240, 0, 255));
    public Setting<Boolean> shader = this.register(new Setting<Boolean>("Shader", true));
    public Setting<Integer> shaderRed = this.register(new Setting<Object>("ShaderRed", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255)));
    public Setting<Integer> shaderGreen = this.register(new Setting<Object>("ShaderGreen", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255)));
    public Setting<Integer> shaderBlue = this.register(new Setting<Object>("ShaderBlue", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255)));
    public Setting<Integer> shaderAlpha = this.register(new Setting<Object>("ShaderAlpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255)));
    public Setting<Integer> shaderRadius = this.register(new Setting<Object>("ShaderRadius", Integer.valueOf(2), Integer.valueOf(1), Integer.valueOf(10)));
    public Setting<Boolean> rainbow = this.register(new Setting<Boolean>("Rainbow", false));
    public Setting<rainbowMode> rainbowModeHud = this.register(new Setting<Object>("HRainbowMode", rainbowMode.Static, v -> this.rainbow.getValue()));
    public Setting<rainbowModeArray> rainbowModeA = this.register(new Setting<Object>("ARainbowMode", rainbowModeArray.Static, v -> this.rainbow.getValue()));
    public Setting<Integer> rainbowHue = this.register(new Setting<Object>("Delay", Integer.valueOf(240), Integer.valueOf(0), Integer.valueOf(600), v -> this.rainbow.getValue()));
    public Setting<Float> rainbowBrightness = this.register(new Setting<Object>("Brightness ", Float.valueOf(150.0f), Float.valueOf(1.0f), Float.valueOf(255.0f), v -> this.rainbow.getValue()));
    public Setting<Float> rainbowSaturation = this.register(new Setting<Object>("Saturation", Float.valueOf(150.0f), Float.valueOf(1.0f), Float.valueOf(255.0f), v -> this.rainbow.getValue()));
    private OyVeyGui click;

    public ClickGui() {
        super("ClickGui", "Opens the ClickGui", Module.Category.CLIENT, true, false, false);
        this.setInstance();
    }

    public static ClickGui getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ClickGui();
        }
        return INSTANCE;
    }
    public static Colors INSTANCE() {
        return null;
    }


    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        if (this.customFov.getValue().booleanValue()) {
            ClickGui.mc.gameSettings.setOptionFloatValue(GameSettings.Options.FOV, this.fov.getValue().floatValue());
        }
    }

    @SubscribeEvent
    public void onSettingChange(ClientEvent event) {
        if (event.getStage() == 2 && event.getSetting().getFeature().equals(this)) {
            if (event.getSetting().equals(this.prefix)) {
                OyVey.commandManager.setPrefix(this.prefix.getPlannedValue());
                Command.sendMessage("Prefix set to " + ChatFormatting.DARK_GRAY + OyVey.commandManager.getPrefix());
            }
            OyVey.colorManager.setColor(this.red.getPlannedValue(), this.green.getPlannedValue(), this.blue.getPlannedValue(), this.hoverAlpha.getPlannedValue());
        }
    }

    @Override
    public void onEnable() {
        mc.displayGuiScreen(OyVeyGui.getClickGui());
    }

    @Override
    public void onLoad() {
        OyVey.colorManager.setColor(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.hoverAlpha.getValue());
        OyVey.commandManager.setPrefix(this.prefix.getValue());
    }

    @Override
    public void onTick() {
        if (!(ClickGui.mc.currentScreen instanceof OyVeyGui)) {
            this.disable();
        }
    }
    public Color getColor() {
        return new Color((Integer) this.red.getValue(), (Integer) this.green.getValue(), (Integer) this.blue.getValue(), (Integer) this.alpha.getValue());
    }

    public enum rainbowModeArray {
        Static,
        Up

    }

    public enum rainbowMode {
        Static,
        Sideway

    }
}