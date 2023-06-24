package me.alpha432.oyvey.features.modules.misc;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;

public class UnfocusedCPU extends Module {

    public static UnfocusedCPU INSTANCE;

    public Setting<Integer> unfocusedFps = this.register(new Setting<>("UnfocusedFPS", 5, 1, 30));

    public UnfocusedCPU() {
        super("Unfocused CPU", "Decreases your framerate when minecraft is unfocused.", Category.MISC, false , false, false);
        INSTANCE = this;
    }
}