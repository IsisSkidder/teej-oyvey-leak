package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.features.modules.*;
import me.alpha432.oyvey.features.setting.*;
import net.minecraft.init.*;
import me.alpha432.oyvey.util.*;
import me.alpha432.oyvey.features.command.*;
import net.minecraft.block.*;
import net.minecraft.network.*;
import net.minecraft.util.*;
import net.minecraft.network.play.client.*;
import net.minecraft.entity.*;
import net.minecraft.entity.item.*;
import net.minecraft.util.math.*;

public class Burrow extends Module
{
    private final Setting<Integer> offset;
    private final Setting<Boolean> rotate;
    private final Setting<Mode> mode;
    private BlockPos originalPos;
    private int oldSlot;
    Block returnBlock;

    public Burrow() {
        super("Burrow", "TPs you into a block", Module.Category.COMBAT, true, false, false);
        this.offset = (Setting<Integer>)this.register(new Setting("Offset", (Object)3, (Object)(-10), (Object)10));
        this.rotate = (Setting<Boolean>)this.register(new Setting("Rotate", (Object)false));
        this.mode = (Setting<Mode>)this.register(new Setting("Mode", (Object)Mode.OBBY));
        this.oldSlot = -1;
        this.returnBlock = null;
    }

    public void onEnable() {
        super.onEnable();
        this.originalPos = new BlockPos(Burrow.mc.player.posX, Burrow.mc.player.posY, Burrow.mc.player.posZ);
        switch ((Mode)this.mode.getValue()) {
            case OBBY: {
                this.returnBlock = Blocks.OBSIDIAN;
                break;
            }
            case ECHEST: {
                this.returnBlock = Blocks.ENDER_CHEST;
                break;
            }
            case EABypass: {
                this.returnBlock = (Block)Blocks.CHEST;
                break;
            }
        }
        if (Burrow.mc.world.getBlockState(new BlockPos(Burrow.mc.player.posX, Burrow.mc.player.posY, Burrow.mc.player.posZ)).getBlock().equals(this.returnBlock) || this.intersectsWithEntity(this.originalPos)) {
            this.toggle();
            return;
        }
        this.oldSlot = Burrow.mc.player.inventory.currentItem;
    }

    public String onUpdate() {
        switch ((Mode)this.mode.getValue()) {
            case OBBY: {
                if (BurrowUtil.findHotbarBlock((Class)BlockObsidian.class) != -1) {
                    break;
                }
                Command.sendMessage("Unable to find Obsidian in your Hotbar! Disabling!");
                this.disable();
                return null;
            }
            case ECHEST: {
                if (BurrowUtil.findHotbarBlock((Class)BlockEnderChest.class) != -1) {
                    break;
                }
                Command.sendMessage("Unable to find EnderChests in your Hotbar! Disabling!");
                this.disable();
                return null;
            }
            case EABypass: {
                if (BurrowUtil.findHotbarBlock((Class)BlockChest.class) != -1) {
                    break;
                }
                Command.sendMessage("Unable to find Chests in your Hotbar! Disabling!");
                this.disable();
                return null;
            }
        }
        BurrowUtil.switchToSlot((this.mode.getValue() == Mode.OBBY) ? BurrowUtil.findHotbarBlock((Class)BlockObsidian.class) : ((this.mode.getValue() == Mode.ECHEST) ? BurrowUtil.findHotbarBlock((Class)BlockEnderChest.class) : BurrowUtil.findHotbarBlock((Class)BlockChest.class)));
        Burrow.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Burrow.mc.player.posX, Burrow.mc.player.posY + 0.41999998688698, Burrow.mc.player.posZ, true));
        Burrow.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Burrow.mc.player.posX, Burrow.mc.player.posY + 0.7531999805211997, Burrow.mc.player.posZ, true));
        Burrow.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Burrow.mc.player.posX, Burrow.mc.player.posY + 1.00133597911214, Burrow.mc.player.posZ, true));
        Burrow.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Burrow.mc.player.posX, Burrow.mc.player.posY + 1.16610926093821, Burrow.mc.player.posZ, true));
        BurrowUtil.placeBlock(this.originalPos, EnumHand.MAIN_HAND, (boolean)this.rotate.getValue(), true, false);
        Burrow.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Burrow.mc.player.posX, Burrow.mc.player.posY + (int)this.offset.getValue(), Burrow.mc.player.posZ, false));
        Burrow.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)Burrow.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
        Burrow.mc.player.setSneaking(false);
        BurrowUtil.switchToSlot(this.oldSlot);
        this.toggle();
        return null;
    }

    private boolean intersectsWithEntity(final BlockPos pos) {
        for (final Entity entity : Burrow.mc.world.loadedEntityList) {
            if (!entity.equals((Object)Burrow.mc.player) && !(entity instanceof EntityItem)) {
                if (!new AxisAlignedBB(pos).intersects(entity.getEntityBoundingBox())) {
                    continue;
                }
                return true;
            }
        }
        return false;
    }

    public enum Mode
    {
        OBBY,
        ECHEST,
        EABypass;
    }
}