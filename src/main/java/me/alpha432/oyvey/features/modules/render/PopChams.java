package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.event.events.PacketEvent;
import me.alpha432.oyvey.event.events.Render3DEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import com.mojang.authlib.GameProfile;
import java.awt.Color;

import me.alpha432.oyvey.util.RenderUtil;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class PopChams
        extends Module {
    public Setting<Boolean> angel = this.register(new Setting<Boolean>("Angel",true));
    public Setting<Integer> angelSpeed = this.register(new Setting<Integer>("AngelSpeed", 150, 10, 500));
    public Setting<Integer> fadeSpeed = this.register(new Setting<Integer>("FadeSpeed", 200, 10, 500));
    public Setting<Boolean> outline = this.register(new Setting<Boolean>("SyncColor",true));
    public Setting<Integer> width = this.register(new Setting<Integer>("Width", (int) Double.longBitsToDouble(Double.doubleToLongBits(0.10667784123174527) ^ 0x7FB34F3D2F4C588FL), (int) Double.longBitsToDouble(Double.doubleToLongBits(2.8356779810862056) ^ 0x7FE6AF77EFF6053EL), (int) Double.longBitsToDouble(Double.doubleToLongBits(0.14239240361793695) ^ 0x7FD639EA0E5E7291L)));
    public Setting<Boolean> syncColor = this.register(new Setting<Boolean>("SyncColor",true));
    public Setting<Color> fillColor = this.register(new Setting<Color>("FillColor", new Color(255, 255, 255, 180)));
    public Setting<Color> outColor = this.register(new Setting<Color>("OutlineColor", new Color(255, 255, 255, 180)));
    public Color color;
    public static Color outlineColor;
    public static EntityOtherPlayerMP player;
    public static EntityPlayer entity;
    public long startTime;
    public static float opacity;
    public static long time;
    public static long duration;
    public static float startAlpha;

    public PopChams() {
        super("PopChams","Renders a cham which fades out when a player pops.", Category.RENDER,false,false,false);
    }

    @SubscribeEvent
    public void onReceive(final PacketEvent.Receive event) {
        if (PopChams.mc.player != null && PopChams.mc.world != null) {
            if (event.getPacket() instanceof SPacketEntityStatus) {
                final SPacketEntityStatus packet = (SPacketEntityStatus)event.getPacket();
                if (packet.getEntity((World)PopChams.mc.world) instanceof EntityPlayer) {
                    PopChams.entity = (EntityPlayer)packet.getEntity((World)PopChams.mc.world);
                    if (packet.getOpCode() == 35) {
                        if (PopChams.entity != null) {
                            if (PopChams.entity != PopChams.mc.player) {
                                final GameProfile profile = new GameProfile(PopChams.mc.player.getUniqueID(), "");
                                (PopChams.player = new EntityOtherPlayerMP((World)PopChams.mc.world, profile)).copyLocationAndAnglesFrom(packet.getEntity((World)PopChams.mc.world));
                                PopChams.player.rotationYaw = PopChams.entity.rotationYaw;
                                PopChams.player.rotationYawHead = PopChams.entity.rotationYawHead;
                                PopChams.player.rotationPitch = PopChams.entity.rotationPitch;
                                PopChams.player.prevRotationPitch = PopChams.entity.prevRotationPitch;
                                PopChams.player.prevRotationYaw = PopChams.entity.prevRotationYaw;
                                PopChams.player.renderYawOffset = PopChams.entity.renderYawOffset;
                                this.startTime = System.currentTimeMillis();
                            }
                        }
                    }
                }
            }
        }
    }



    @Override
    public void onRender3D(Render3DEvent event) {
        block6: {
            if (PopChams.mc.player == null || PopChams.mc.world == null) {
                return;
            }
            if (syncColor.getValue()) {
                color = PopChams.globalColor(255);
                outlineColor = PopChams.globalColor(255);
            } else {
                color = fillColor.getValue();
                outlineColor = outColor.getValue();
            }
            opacity = Float.intBitsToFloat(Float.floatToIntBits(1.6358529E38f) ^ 0x7EF622C3);
            time = System.currentTimeMillis();
            duration = time - this.startTime;
            startAlpha = (float)fillColor.getValue().getAlpha() / Float.intBitsToFloat(Float.floatToIntBits(0.0119778095f) ^ 0x7F3B3E93);
            if (player == null || entity == null) break block6;
            if (duration < (long)(fadeSpeed.getValue().intValue() * 10)) {
                opacity = startAlpha - (float)duration / (float)(fadeSpeed.getValue().intValue() * 10);
            }
            if (duration < (long)(fadeSpeed.getValue().intValue() * 10)) {
                GL11.glPushMatrix();
                if (angel.getValue()) {
                    GlStateManager.translate((float)Float.intBitsToFloat(Float.floatToIntBits(1.240196E38f) ^ 0x7EBA9A9D), (float)((float)duration / (float)(angelSpeed.getValue().intValue() * 10)), (float)Float.intBitsToFloat(Float.floatToIntBits(3.0414126E38f) ^ 0x7F64CF7A));
                }
                RenderUtil.renderEntityStatic((Entity)player, Float.intBitsToFloat(Float.floatToIntBits(6.159893f) ^ 0x7F451DD8), false);
                GlStateManager.translate((float)Float.intBitsToFloat(Float.floatToIntBits(3.0715237E38f) ^ 0x7F671365), (float)Float.intBitsToFloat(Float.floatToIntBits(1.9152719E37f) ^ 0x7D668ADF), (float)Float.intBitsToFloat(Float.floatToIntBits(1.9703683E38f) ^ 0x7F143BEA));
                GL11.glPopMatrix();
            }
        }
    }
}
