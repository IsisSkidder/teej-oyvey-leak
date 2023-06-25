package me.alpha432.oyvey.features.modules.misc;

import java.util.concurrent.atomic.AtomicBoolean;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;

public class Media
        extends Module {
    public Setting<Boolean> getName = this.register(new Setting<Boolean>("GetName", true));
    private final AtomicBoolean connected = new AtomicBoolean(false);
    private static Media instance;
    private StringBuffer name = null;

    public Media() {
        super("Media", "", Module.Category.CLIENT, false, false, false);
        instance = this;
    }

    public static Media getInstance() {
        if (instance == null) {
            instance = new Media();
        }
        return instance;
    }

    public String getPlayerName() {
        if (this.name == null) {
            return null;
        }
        return this.name.toString();
    }

    public boolean isConnected() {
        return this.connected.get();
    }

    @Override
    public void onTick() {
        if (mc.getConnection() != null && this.isConnected() && this.getName.getValue().booleanValue()) {
            this.getName.setValue(false);
        }
    }
}
