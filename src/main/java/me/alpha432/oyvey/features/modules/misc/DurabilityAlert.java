package me.alpha432.oyvey.features.modules.misc;

import me.alpha432.oyvey.features.modules.*;
import me.alpha432.oyvey.features.setting.*;
import net.minecraft.item.*;
import me.alpha432.oyvey.event.events.*;
import net.minecraft.client.gui.*;

public class DurabilityAlert extends Module
{
    private Setting<Integer> dura;
    private Setting<Boolean> chad;
    private boolean lowDura;

    public DurabilityAlert() {
        super("Durability Alert", "Alerts you and your friends if you have low durability", Category.MISC, true, false, false);
        this.dura = (Setting<Integer>)this.register(new Setting("Durability", 30, 1, 100));
        this.chad = (Setting<Boolean>)this.register(new Setting("Australian Mode", true));
        this.lowDura = false;
    }

    @Override
    public void onUpdate() {
        this.lowDura = false;
        try {
            for (final ItemStack is : DurabilityAlert.mc.player.getArmorInventoryList()) {
                final float green = (is.getMaxDamage() - (float)is.getItemDamage()) / is.getMaxDamage();
                final float red = 1.0f - green;
                final int dmg = 100 - (int)(red * 100.0f);
                if (dmg > (float)this.dura.getValue()) {
                    continue;
                }
                this.lowDura = true;
            }
        }
        catch (Exception ex) {}
    }

    @Override
    public void onRender2D(final Render2DEvent event) {
        if (this.lowDura) {
            final ScaledResolution sr = new ScaledResolution(DurabilityAlert.mc);
            DurabilityAlert.mc.fontRenderer.drawStringWithShadow("Warning: Your " + (this.chad.getValue() ? "armour" : "shits") + " is below " + this.dura.getValue() + "%", (float)(sr.getScaledWidth() / 2 - DurabilityAlert.mc.fontRenderer.getStringWidth("Warning: Your armour is below " + this.dura.getValue() + "%") / 2), 15.0f, -65536);
        }
    }
}