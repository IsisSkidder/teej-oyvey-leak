package me.alpha432.oyvey.features.modules.client;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.event.events.Render2DEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.ColorUtil;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import me.alpha432.oyvey.util.EntityUtil;
import net.minecraft.entity.player.EntityPlayer;
import com.mojang.realmsclient.gui.ChatFormatting;
public class TextRadar extends Module {
    private final Setting<Integer> amount = register(new Setting<>("PlayerCount", 10, 1, 100));
    public Setting<Integer> Y = register(new Setting<>("Y", 5, 0, 550));

    public TextRadar() {
        super("Text Radar", "Shows players in render distance on hud", Category.CLIENT, false, false, false);
    }

    public static TextRadar INSTANCE = new TextRadar();
    private int color;


    @Override
    public void onRender2D(Render2DEvent event) {
        int i = 0;
        for (Object o : mc.world.loadedEntityList) {
            if (o instanceof EntityPlayer && o != mc.player) {
                i++;
                if (i > amount.getValue()) return;
                EntityPlayer entity = (EntityPlayer) o;
                float health = Math.round(entity.getHealth()) + Math.round(entity.getAbsorptionAmount());
                final DecimalFormat dfDistance = new DecimalFormat("#.#");
                dfDistance.setRoundingMode(RoundingMode.CEILING);
                final StringBuilder distanceSB = new StringBuilder();
                color = ColorUtil.toRGBA(ClickGui.getInstance().red.getValue(), ClickGui.getInstance().green.getValue(), ClickGui.getInstance().blue.getValue());


                String heal;
                String health_str;
                health_str = String.valueOf(health);


                final int distanceInt = (int) EntityUtil.mc.player.getDistance(entity);
                final String distance = dfDistance.format(distanceInt);
                if (distanceInt >= 25) {
                    distanceSB.append(ChatFormatting.GREEN);
                } else if (distanceInt > 10) {
                    distanceSB.append(ChatFormatting.GOLD);
                } else if (distanceInt >= 50) {
                    distanceSB.append(ChatFormatting.GRAY);
                } else {
                    distanceSB.append(ChatFormatting.RED);
                }
                distanceSB.append(distance);

                if (health >= 12.0) {


                    String name = entity.getGameProfile().getName();
                    String str = " " + ChatFormatting.RESET;

                    if (OyVey.friendManager.isFriend(entity.getName())) {
                        OyVey.textManager.drawString(str + ChatFormatting.AQUA + name + " " + distanceSB.toString() + ChatFormatting.WHITE, -2.0F, Y.getValue() + i * 10, this.color, true);
                    } else if ((ClickGui.getInstance()).rainbow.getValue() && ClickGui.getInstance().rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                        OyVey.textManager.drawString(str + name + " " + distanceSB.toString() + ChatFormatting.RESET + "m" + "", -2.0F, Y.getValue() + i * 10, ColorUtil.rainbow(((Integer) (ClickGui.getInstance()).rainbowHue.getValue()).intValue()).getRGB(), true);
                    } else {
                        OyVey.textManager.drawString(str + name + " " + distanceSB + "m", -2.0F, Y.getValue() + i * 10, this.color, true);
                    }
                }
            }
        }
    }
}