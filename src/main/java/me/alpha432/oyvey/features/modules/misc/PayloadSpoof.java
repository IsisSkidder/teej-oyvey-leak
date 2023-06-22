package me.alpha432.oyvey.features.modules.misc;

import me.alpha432.oyvey.event.events.PacketEvent;
import me.alpha432.oyvey.features.command.Command;
import me.alpha432.oyvey.features.modules.Module;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
public class PayloadSpoof extends Module {

    public PayloadSpoof() {
        super("Payload Spoof", "blocks payloads and exploits", Category.MISC, true, false, false);
    }

    @SubscribeEvent(priority= EventPriority.HIGHEST)
    public void onPacketRecieve(PacketEvent.Receive event) {
        String text;
        if (event.getPacket() instanceof SPacketChat && ((text = ((SPacketChat)event.getPacket()).getChatComponent().getUnformattedText()).contains("${") || text.contains("$<") || text.contains("$:-") || text.contains("jndi:ldap"))) {
            Command.sendMessage("[PayloadSpoof] Blocked message: " + text);
            event.setCanceled(true);
        }
    }
}