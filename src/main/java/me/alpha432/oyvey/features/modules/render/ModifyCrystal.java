package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.features.modules.*;
import me.alpha432.oyvey.features.setting.*;
import me.alpha432.oyvey.*;

public class ModifyCrystal extends Module
{
    public static Setting<Float> spin;
    public static Setting<Float> bounce;
    public static Setting<Float> scale;

    public ModifyCrystal() {
        super("ModifyCrystal", "modifies crystals", Module.Category.RENDER, true, false, false);
        this.register((Setting)ModifyCrystal.spin);
        this.register((Setting)ModifyCrystal.scale);
        this.register((Setting)ModifyCrystal.bounce);
    }

    public static float[] getSpeed() {
        return OyVey.moduleManager.isModuleEnabled("Modify Crystal") ? new float[] { ModifyCrystal.spin.getValue(), ModifyCrystal.bounce.getValue() } : new float[] { 1.0f, 1.0f };
    }

    static {
        ModifyCrystal.spin = new Setting<Float>("Spin", 1.0f, 0.0f, 10.0f);
        ModifyCrystal.bounce = new Setting<Float>("Bounce", 1.0f, 0.0f, 10.0f);
        ModifyCrystal.scale = new Setting<Float>("Scale", 1.0f, 0.0f, 1.0f);
    }
}
