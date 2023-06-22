package me.alpha432.oyvey.features.modules.misc;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import me.alpha432.oyvey.features.modules.Module;

public class Coords
        extends Module {
    public Coords() {
        super("Coords", "Copies coords to clipboard", Category.MISC, true, false, false);
    }

    @Override
    public void onEnable() {
        int posX = (int)Coords.mc.player.posX;
        int posY = (int)Coords.mc.player.posY;
        int posZ = (int)Coords.mc.player.posZ;
        String coords = "X: " + posX + " Y: " + posY + " Z: " + posZ;
        StringSelection stringSelection = new StringSelection(coords);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
        this.toggle();
    }
}