package me.alpha432.oyvey.features.modules.render;

import java.awt.Color;
import me.alpha432.oyvey.event.events.Render3DEvent;
import me.alpha432.oyvey.manager.*;
import me.alpha432.oyvey.util.BlockUtil;
import me.alpha432.oyvey.util.ColorUtil;
import me.alpha432.oyvey.util.RenderUtil;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

public class HoleESP
        extends Module {
    private final Setting<Boolean> renderOwn = register(new Setting<Boolean>("RenderOwn", true));
    private final Setting<Boolean> fov = register(new Setting<Boolean>("FovOnly", true));
    private final Setting<Integer> range = register(new Setting<Integer>("Range", 5, 0, 25));
    private final Setting<Boolean> box = register(new Setting<Boolean>("Box", true));
    private final Setting<Boolean> gradientBox = register(new Setting<Boolean>("FadeBox", false));
    private final Setting<Boolean> invertGradientBox = register(new Setting<Boolean>("InvertBoxFade", false));
    private final Setting<Boolean> outline = register(new Setting<Boolean>("Outline", true));
    private final Setting<Boolean> gradientOutline = register(new Setting<Boolean>("FadeLine", false));
    private final Setting<Boolean> invertGradientOutline = register(new Setting<Boolean>("InvertLineFade", false));
    private final Setting<Boolean> separateHeight = register(new Setting<Boolean>("SeparateHeight", false));
    private final Setting<Double> lineHeight = register(new Setting<Double>("LineHeight", -1.1, -2.0, 2.0));
    private final Setting<Boolean> wireframe = register(new Setting<Boolean>("Wireframe", true));
    private final Setting<WireframeMode> wireframeMode = register(new Setting<WireframeMode>("Mode", WireframeMode.FLAT));
    private final Setting<Double> height = register(new Setting<Double>("Height", 0., -2.0, 2.0));
    private final Setting<Integer> boxAlpha = register(new Setting<Integer>("BoxAlpha", 80, 0, 255));
    private final Setting<Float> lineWidth = register(new Setting<Float>("LineWidth", Float.valueOf(0.5f), Float.valueOf(0.1f), Float.valueOf(5.0f), v -> (this.outline.getValue() != false || this.wireframe.getValue() != false)));
    private final Setting<Boolean> rainbow = register(new Setting<Boolean>("Rainbow", false));
    private final Setting<Color> obbyColor = register(new Setting<Color>("Bedrock", new Color(12721437)));
    private final Setting<Color> brockColor = register(new Setting<Color>("Bedrock", new Color(12721437)));
    private final Setting<Boolean> customOutline = register(new Setting<Boolean>("LinColor", false));
    private final Setting<Color> obbyLineColor = register(new Setting<Color>("Bedrock", new Color(12721437)));
    private final Setting<Color> brockLineColor = register(new Setting<Color>("Bedrock", new Color(12721437)));

    public HoleESP() {
        super("HoleESP", "Shows safe spots near you.", Category.RENDER,false,false,false);
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        assert (HoleESP.mc.renderViewEntity != null);
        Vec3i playerPos = new Vec3i(HoleESP.mc.renderViewEntity.posX, HoleESP.mc.renderViewEntity.posY, HoleESP.mc.renderViewEntity.posZ);
        for (int x = playerPos.getX() - this.range.getValue(); x < playerPos.getX() + this.range.getValue(); ++x) {
            for (int z = playerPos.getZ() - this.range.getValue(); z < playerPos.getZ() + this.range.getValue(); ++z) {
                int rangeY = 5;
                for (int y = playerPos.getY() + rangeY; y > playerPos.getY() - rangeY; --y) {
                    BlockPos pos = new BlockPos(x, y, z);
                    Color safeColor = this.rainbow.getValue() != false ? ColorManager.getRainbow() : this.brockColor.getValue();
                    Color color = this.rainbow.getValue() != false ? ColorManager.getRainbow() : this.obbyColor.getValue();
                    Color safecColor = this.brockLineColor.getValue();
                    Color cColor = this.obbyLineColor.getValue();
                    if (!HoleESP.mc.world.getBlockState(pos).getBlock().equals((Object)Blocks.AIR) || !HoleESP.mc.world.getBlockState(pos.add(0, 1, 0)).getBlock().equals((Object)Blocks.AIR) || !HoleESP.mc.world.getBlockState(pos.add(0, 2, 0)).getBlock().equals((Object)Blocks.AIR) || pos.equals((Object)new BlockPos(HoleESP.mc.player.posX, HoleESP.mc.player.posY, HoleESP.mc.player.posZ)) && !this.renderOwn.getValue().booleanValue() || !RotationManager.isInFov(pos) && this.fov.getValue().booleanValue()) continue;
                    if (HoleESP.mc.world.getBlockState(pos.north()).getBlock() == Blocks.AIR && HoleESP.mc.world.getBlockState(pos.north().up()).getBlock() == Blocks.AIR && HoleESP.mc.world.getBlockState(pos.north().down()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.north(2)).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.east()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.north().east()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.west()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.north().west()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.south()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.down()).getBlock() == Blocks.BEDROCK) {
                        this.drawDoubles(true, pos, safeColor, this.customOutline.getValue(), safecColor, this.lineWidth.getValue().floatValue(), this.outline.getValue(), this.box.getValue(), this.boxAlpha.getValue(), true, this.height.getValue(), this.separateHeight.getValue() != false ? this.lineHeight.getValue().doubleValue() : this.height.getValue().doubleValue(), this.gradientBox.getValue(), this.gradientOutline.getValue(), this.invertGradientBox.getValue(), this.invertGradientOutline.getValue(), 0, this.wireframe.getValue(), this.wireframeMode.getValue() == WireframeMode.FLAT);
                    } else if (!(HoleESP.mc.world.getBlockState(pos.north()).getBlock() != Blocks.AIR || HoleESP.mc.world.getBlockState(pos.north().up()).getBlock() != Blocks.AIR || HoleESP.mc.world.getBlockState(pos.north().down()).getBlock() != Blocks.OBSIDIAN && HoleESP.mc.world.getBlockState(pos.north().down()).getBlock() != Blocks.BEDROCK || HoleESP.mc.world.getBlockState(pos.north(2)).getBlock() != Blocks.OBSIDIAN && HoleESP.mc.world.getBlockState(pos.north(2)).getBlock() != Blocks.BEDROCK || HoleESP.mc.world.getBlockState(pos.east()).getBlock() != Blocks.OBSIDIAN && HoleESP.mc.world.getBlockState(pos.east()).getBlock() != Blocks.BEDROCK || HoleESP.mc.world.getBlockState(pos.north().east()).getBlock() != Blocks.OBSIDIAN && HoleESP.mc.world.getBlockState(pos.north().east()).getBlock() != Blocks.BEDROCK || HoleESP.mc.world.getBlockState(pos.west()).getBlock() != Blocks.OBSIDIAN && HoleESP.mc.world.getBlockState(pos.west()).getBlock() != Blocks.BEDROCK || HoleESP.mc.world.getBlockState(pos.north().west()).getBlock() != Blocks.OBSIDIAN && HoleESP.mc.world.getBlockState(pos.north().west()).getBlock() != Blocks.BEDROCK || HoleESP.mc.world.getBlockState(pos.south()).getBlock() != Blocks.OBSIDIAN && HoleESP.mc.world.getBlockState(pos.south()).getBlock() != Blocks.BEDROCK || HoleESP.mc.world.getBlockState(pos.down()).getBlock() != Blocks.OBSIDIAN && HoleESP.mc.world.getBlockState(pos.down()).getBlock() != Blocks.BEDROCK)) {
                        this.drawDoubles(true, pos, color, this.customOutline.getValue(), cColor, this.lineWidth.getValue().floatValue(), this.outline.getValue(), this.box.getValue(), this.boxAlpha.getValue(), true, this.height.getValue(), this.separateHeight.getValue() != false ? this.lineHeight.getValue().doubleValue() : this.height.getValue().doubleValue(), this.gradientBox.getValue(), this.gradientOutline.getValue(), this.invertGradientBox.getValue(), this.invertGradientOutline.getValue(), 0, this.wireframe.getValue(), this.wireframeMode.getValue() == WireframeMode.FLAT);
                    }
                    if (HoleESP.mc.world.getBlockState(pos.east()).getBlock() == Blocks.AIR && HoleESP.mc.world.getBlockState(pos.east().up()).getBlock() == Blocks.AIR && HoleESP.mc.world.getBlockState(pos.east().down()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.east(2)).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.east(2).down()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.north()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.east().north()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.west()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.east().south()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.south()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.down()).getBlock() == Blocks.BEDROCK) {
                        this.drawDoubles(false, pos, safeColor, this.customOutline.getValue(), safecColor, this.lineWidth.getValue().floatValue(), this.outline.getValue(), this.box.getValue(), this.boxAlpha.getValue(), true, this.height.getValue(), this.separateHeight.getValue() != false ? this.lineHeight.getValue().doubleValue() : this.height.getValue().doubleValue(), this.gradientBox.getValue(), this.gradientOutline.getValue(), this.invertGradientBox.getValue(), this.invertGradientOutline.getValue(), 0, this.wireframe.getValue(), this.wireframeMode.getValue() == WireframeMode.FLAT);
                    } else if (!(HoleESP.mc.world.getBlockState(pos.east()).getBlock() != Blocks.AIR || HoleESP.mc.world.getBlockState(pos.east().up()).getBlock() != Blocks.AIR || HoleESP.mc.world.getBlockState(pos.east().down()).getBlock() != Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.east().down()).getBlock() != Blocks.OBSIDIAN || HoleESP.mc.world.getBlockState(pos.east(2)).getBlock() != Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.east(2)).getBlock() != Blocks.OBSIDIAN || HoleESP.mc.world.getBlockState(pos.north()).getBlock() != Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.north()).getBlock() != Blocks.OBSIDIAN || HoleESP.mc.world.getBlockState(pos.east().north()).getBlock() != Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.east().north()).getBlock() != Blocks.OBSIDIAN || HoleESP.mc.world.getBlockState(pos.west()).getBlock() != Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.west()).getBlock() != Blocks.OBSIDIAN || HoleESP.mc.world.getBlockState(pos.east().south()).getBlock() != Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.east().south()).getBlock() != Blocks.OBSIDIAN || HoleESP.mc.world.getBlockState(pos.south()).getBlock() != Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.south()).getBlock() != Blocks.OBSIDIAN || HoleESP.mc.world.getBlockState(pos.down()).getBlock() != Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.down()).getBlock() != Blocks.OBSIDIAN)) {
                        this.drawDoubles(false, pos, color, this.customOutline.getValue(), cColor, this.lineWidth.getValue().floatValue(), this.outline.getValue(), this.box.getValue(), this.boxAlpha.getValue(), true, this.height.getValue(), this.separateHeight.getValue() != false ? this.lineHeight.getValue().doubleValue() : this.height.getValue().doubleValue(), this.gradientBox.getValue(), this.gradientOutline.getValue(), this.invertGradientBox.getValue(), this.invertGradientOutline.getValue(), 0, this.wireframe.getValue(), this.wireframeMode.getValue() == WireframeMode.FLAT);
                    }
                    if (HoleESP.mc.world.getBlockState(pos.north()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.east()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.west()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.south()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.down()).getBlock() == Blocks.BEDROCK) {
                        this.drawHoleESP(pos, safeColor, this.customOutline.getValue(), safecColor, this.lineWidth.getValue().floatValue(), this.outline.getValue(), this.box.getValue(), this.boxAlpha.getValue(), true, this.height.getValue(), this.separateHeight.getValue() != false ? this.lineHeight.getValue().doubleValue() : this.height.getValue().doubleValue(), this.gradientBox.getValue(), this.gradientOutline.getValue(), this.invertGradientBox.getValue(), this.invertGradientOutline.getValue(), 0, this.wireframe.getValue(), this.wireframeMode.getValue() == WireframeMode.FLAT);
                        continue;
                    }
                    if (!BlockUtil.isUnsafe(HoleESP.mc.world.getBlockState(pos.down()).getBlock()) || !BlockUtil.isUnsafe(HoleESP.mc.world.getBlockState(pos.east()).getBlock()) || !BlockUtil.isUnsafe(HoleESP.mc.world.getBlockState(pos.west()).getBlock()) || !BlockUtil.isUnsafe(HoleESP.mc.world.getBlockState(pos.south()).getBlock()) || !BlockUtil.isUnsafe(HoleESP.mc.world.getBlockState(pos.north()).getBlock())) continue;
                    this.drawHoleESP(pos, color, this.customOutline.getValue(), cColor, this.lineWidth.getValue().floatValue(), this.outline.getValue(), this.box.getValue(), this.boxAlpha.getValue(), true, this.height.getValue(), this.separateHeight.getValue() != false ? this.lineHeight.getValue().doubleValue() : this.height.getValue().doubleValue(), this.gradientBox.getValue(), this.gradientOutline.getValue(), this.invertGradientBox.getValue(), this.invertGradientOutline.getValue(), 0, this.wireframe.getValue(), this.wireframeMode.getValue() == WireframeMode.FLAT);
                }
            }
        }
    }

    public void drawDoubles(boolean faceNorth, BlockPos pos, Color color, boolean secondC, Color secondColor, float lineWidth, boolean outline, boolean box, int boxAlpha, boolean air, double height, double lineHeight, boolean gradientBox, boolean gradientOutline, boolean invertGradientBox, boolean invertGradientOutline, int gradientAlpha, boolean cross, boolean flatCross) {
        this.drawHoleESP(pos, color, secondC, secondColor, lineWidth, outline, box, boxAlpha, air, height, lineHeight, gradientBox, gradientOutline, invertGradientBox, invertGradientOutline, gradientAlpha, cross, flatCross);
        this.drawHoleESP(faceNorth ? pos.north() : pos.east(), color, secondC, secondColor, lineWidth, outline, box, boxAlpha, air, height, lineHeight, gradientBox, gradientOutline, invertGradientBox, invertGradientOutline, gradientAlpha, cross, flatCross);
    }

    public void drawHoleESP(BlockPos pos, Color color, boolean secondC, Color secondColor, float lineWidth, boolean outline, boolean box, int boxAlpha, boolean air, double height, double lineHeight, boolean gradientBox, boolean gradientOutline, boolean invertGradientBox, boolean invertGradientOutline, int gradientAlpha, boolean cross, boolean flatCross) {
        if (box) {
            RenderUtil.drawBox(pos, ColorUtil.injectAlpha(color, boxAlpha), height, gradientBox, invertGradientBox, gradientAlpha);
        }
        if (outline) {
            RenderUtil.drawBlockOutline(pos, secondC ? secondColor : color, lineWidth, air, lineHeight, gradientOutline, invertGradientOutline, gradientAlpha, false);
        }
        if (cross) {
            RenderUtil.drawBlockWireframe(pos, secondC ? secondColor : color, lineWidth, height, flatCross);
        }
    }

    private enum WireframeMode {
        FLAT,
        FULL;
    }
}
