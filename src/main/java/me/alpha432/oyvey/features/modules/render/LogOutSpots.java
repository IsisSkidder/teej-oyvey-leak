package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.features.modules.*;
import me.alpha432.oyvey.features.setting.*;

import java.util.List;
import java.util.concurrent.*;
import me.alpha432.oyvey.features.modules.client.*;
import java.awt.*;
import net.minecraft.util.math.*;
import net.minecraft.entity.*;
import me.alpha432.oyvey.event.events.*;
import me.alpha432.oyvey.features.command.*;
import java.util.*;
import net.minecraft.entity.player.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraft.client.renderer.*;
import me.alpha432.oyvey.util.*;

public class LogOutSpots extends Module
{
    private final Setting<Boolean> colorSync;
    private final Setting<Integer> red;
    private final Setting<Integer> green;
    private final Setting<Integer> blue;
    private final Setting<Integer> alpha;
    private final Setting<Boolean> scaleing;
    private final Setting<Float> scaling;
    private final Setting<Float> factor;
    private final Setting<Boolean> smartScale;
    private final Setting<Boolean> rect;
    private final Setting<Boolean> coords;
    private final Setting<Boolean> notification;
    private final List<LogoutPos> spots;
    public Setting<Float> range;
    public Setting<Boolean> message;

    public LogOutSpots() {
        super("LogOutSpots", "Renders LogOutSpots", Module.Category.RENDER, true, false, false);
        this.colorSync = (Setting<Boolean>)this.register(new Setting("Sync", false));
        this.red = (Setting<Integer>)this.register(new Setting("Red", 255, 0, 255));
        this.green = (Setting<Integer>)this.register(new Setting("Green", 0, 0, 255));
        this.blue = (Setting<Integer>)this.register(new Setting("Blue", 0, 0, 255));
        this.alpha = (Setting<Integer>)this.register(new Setting("Alpha", 255, 0, 255));
        this.scaleing = (Setting<Boolean>)this.register(new Setting("Scale", false));
        this.scaling = (Setting<Float>)this.register(new Setting("Size", 4.0f, 0.1f, 20.0f));
        this.factor = (Setting<Float>)this.register(new Setting("Factor", 0.3f, 0.1f, 1.0f, v -> this.scaleing.getValue()));
        this.smartScale = (Setting<Boolean>)this.register(new Setting("SmartScale", false, v -> this.scaleing.getValue()));
        this.rect = (Setting<Boolean>)this.register(new Setting("Rectangle", true));
        this.coords = (Setting<Boolean>)this.register(new Setting("Coords", true));
        this.notification = (Setting<Boolean>)this.register(new Setting("Notification", true));
        this.spots = new CopyOnWriteArrayList<LogoutPos>();
        this.range = (Setting<Float>)this.register(new Setting("Range", 300.0f, 50.0f, 500.0f));
        this.message = (Setting<Boolean>)this.register(new Setting("Message", false));
    }

    public void onLogout() {
        this.spots.clear();
    }

    public void onDisable() {
        this.spots.clear();
    }

    public void onRender3D(final Render3DEvent event) {
        if (!this.spots.isEmpty()) {
            synchronized (this.spots) {
                final AxisAlignedBB[] interpolateAxis = new AxisAlignedBB[1];
                final AxisAlignedBB[] bb = new AxisAlignedBB[1];
                final Color[] currentColor = {null};
                final double[] x = new double[1];
                final double[] y = new double[1];
                final double[] z = new double[1];
                this.spots.forEach(spot -> {
                    if (spot.getEntity() != null) {
                        bb[0] = (interpolateAxis[0] = RenderUtil.interpolateAxis(spot.getEntity().getEntityBoundingBox()));
                        if (this.colorSync.getValue()) {
                            currentColor[0] = Colors.INSTANCE.getCurrentColor();
                        }
                        else {
                            // new(java.awt.Color.class)
                            new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue());
                        }
                        RenderUtil.drawBlockOutline(interpolateAxis[0], currentColor[0], 1.0f);
                        x[0] = this.interpolate(spot.getEntity().lastTickPosX, spot.getEntity().posX, event.getPartialTicks()) - LogOutSpots.mc.getRenderManager().renderPosX;
                        y[0] = this.interpolate(spot.getEntity().lastTickPosY, spot.getEntity().posY, event.getPartialTicks()) - LogOutSpots.mc.getRenderManager().renderPosY;
                        z[0] = this.interpolate(spot.getEntity().lastTickPosZ, spot.getEntity().posZ, event.getPartialTicks()) - LogOutSpots.mc.getRenderManager().renderPosZ;
                        this.renderNameTag(spot.getName(), x[0], y[0], z[0], event.getPartialTicks(), spot.getX(), spot.getY(), spot.getZ());
                    }
                });
            }
        }
    }

    public void onUpdate() {
        if (!fullNullCheck()) {
            this.spots.removeIf(spot -> LogOutSpots.mc.player.getDistanceSq((Entity)spot.getEntity()) >= MathUtil.square(this.range.getValue()));
        }
    }

    @SubscribeEvent
    public void onConnection(final ConnectionEvent event) {
        if (event.getStage() == 0) {
            final UUID uuid = event.getUuid();
            final EntityPlayer entity = LogOutSpots.mc.world.getPlayerEntityByUUID(uuid);
            if (entity != null && this.message.getValue()) {
                Command.sendMessage("§a" + entity.getName() + " just logged in");
            }
            this.spots.removeIf(pos -> pos.getName().equalsIgnoreCase(event.getName()));
        }
        else if (event.getStage() == 1) {
            final EntityPlayer entity2 = event.getEntity();
            final UUID uuid2 = event.getUuid();
            final String name = event.getName();
            if (this.message.getValue()) {
                Command.sendMessage("§c" + event.getName() + " just logged out");
            }
            if (name != null && entity2 != null && uuid2 != null) {
                this.spots.add(new LogoutPos(name, uuid2, entity2));
            }
        }
    }

    private void renderNameTag(final String name, final double x, final double yi, final double z, final float delta, final double xPos, final double yPos, final double zPos) {
        final double y = yi + 0.7;
        final Entity camera = LogOutSpots.mc.getRenderViewEntity();
        assert camera != null;
        final double originalPositionX = camera.posX;
        final double originalPositionY = camera.posY;
        final double originalPositionZ = camera.posZ;
        camera.posX = this.interpolate(camera.prevPosX, camera.posX, delta);
        camera.posY = this.interpolate(camera.prevPosY, camera.posY, delta);
        camera.posZ = this.interpolate(camera.prevPosZ, camera.posZ, delta);
        final String displayTag = name + " XYZ: " + (int)xPos + ", " + (int)yPos + ", " + (int)zPos;
        final double distance = camera.getDistance(x + LogOutSpots.mc.getRenderManager().viewerPosX, y + LogOutSpots.mc.getRenderManager().viewerPosY, z + LogOutSpots.mc.getRenderManager().viewerPosZ);
        final int width = this.renderer.getStringWidth(displayTag) / 2;
        double scale = (0.0018 + this.scaling.getValue() * (distance * this.factor.getValue())) / 1000.0;
        if (distance <= 8.0 && this.smartScale.getValue()) {
            scale = 0.0245;
        }
        if (!this.scaleing.getValue()) {
            scale = this.scaling.getValue() / 100.0;
        }
        GlStateManager.pushMatrix();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enablePolygonOffset();
        GlStateManager.doPolygonOffset(1.0f, -1500000.0f);
        GlStateManager.disableLighting();
        GlStateManager.translate((float)x, (float)y + 1.4f, (float)z);
        GlStateManager.rotate(-LogOutSpots.mc.getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(LogOutSpots.mc.getRenderManager().playerViewX, (LogOutSpots.mc.gameSettings.thirdPersonView == 2) ? -1.0f : 1.0f, 0.0f, 0.0f);
        GlStateManager.scale(-scale, -scale, scale);
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.enableBlend();
        if (this.rect.getValue()) {
            RenderUtil.drawRect((float)(-width - 2), (float)(-(this.renderer.getFontHeight() + 1)), width + 2.0f, 1.5f, 1426063360);
        }
        GlStateManager.disableBlend();
        this.renderer.drawStringWithShadow(displayTag, (float)(-width), (float)(-(this.renderer.getFontHeight() - 1)), ((boolean)this.colorSync.getValue()) ? Colors.INSTANCE.getCurrentColorHex() : ColorUtil.toRGBA(new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue())));
        camera.posX = originalPositionX;
        camera.posY = originalPositionY;
        camera.posZ = originalPositionZ;
        GlStateManager.enableDepth();
        GlStateManager.disableBlend();
        GlStateManager.disablePolygonOffset();
        GlStateManager.doPolygonOffset(1.0f, 1500000.0f);
        GlStateManager.popMatrix();
    }

    private double interpolate(final double previous, final double current, final float delta) {
        return previous + (current - previous) * delta;
    }

    private static class LogoutPos
    {
        private final String name;
        private final UUID uuid;
        private final EntityPlayer entity;
        private final double x;
        private final double y;
        private final double z;

        public LogoutPos(final String name, final UUID uuid, final EntityPlayer entity) {
            this.name = name;
            this.uuid = uuid;
            this.entity = entity;
            this.x = entity.posX;
            this.y = entity.posY;
            this.z = entity.posZ;
        }

        public String getName() {
            return this.name;
        }

        public UUID getUuid() {
            return this.uuid;
        }

        public EntityPlayer getEntity() {
            return this.entity;
        }

        public double getX() {
            return this.x;
        }

        public double getY() {
            return this.y;
        }

        public double getZ() {
            return this.z;
        }
    }
}