package me.alpha432.oyvey.features.modules.misc;

import me.alpha432.oyvey.features.modules.*;
import me.alpha432.oyvey.features.setting.*;
import java.util.*;
import net.minecraft.entity.player.*;

public class WhisperSpam extends Module
{
    public static Setting<ChatModes> chatModes;
    public final Setting<Integer> delay;
    List<String> chants;
    Random r;
    int tick_delay;

    public WhisperSpam() {
        super("WhisperSpam", "ez", Category.MISC, true, false, false);
        this.delay = (Setting<Integer>)this.register(new Setting("SpamDelay", 10, 0, 100));
        this.chants = new ArrayList<String>();
        this.r = new Random();
        chatModes = this.register(new Setting<ChatModes>("TYPE", ChatModes.bait));    }

    @Override
    public void onEnable() {
        this.tick_delay = 0;
        this.chants.clear();
        if (WhisperSpam.chatModes.getValue() == ChatModes.bait) {
            this.chants.clear();
            this.chants.add("/w <player> PK PK PK IS HERE");
            this.chants.add("/w <player> BITCOIN DID SOMEONE SAY BITCOIN??");
            this.chants.add("/w <player> CHECK MY NAME MC VIEWS LOL");
            this.chants.add("/w <player> COMPARE NAME MC VIEWS???");
            this.chants.add("/w <player> BROKE BOY GET YA BREAD UP GANG");
            this.chants.add("/w <player> PK IS HERE PK IS HERE");
            this.chants.add("/w <player> skeetless bum lelsauce");
            this.chants.add("/w <player> 2b pt?? compare pt??");
            this.chants.add("/w <player> you ran the rat roflsauce");
            this.chants.add("/w <player> randroidz");
            this.chants.add("/w <player> Nice jd 2023 lolz");
        }
        else if (WhisperSpam.chatModes.getValue() == ChatModes.summrs) {
            this.chants.clear();
            this.chants.add("/w <player> I LOVE SUMMRSXO");
            this.chants.add("/w <player> slayworld soldier....");
            this.chants.add("/w <player> lean belly lean belly");
            this.chants.add("/w <player> BELLWORLD22222");
            this.chants.add("/w <player> My boyfriend name kankan");
            this.chants.add("/w <player> BIRD BUSINESS");
            this.chants.add("/w <player> I miss old summrs");
            this.chants.add("/w <player> xangang beats makes the best beats");
            this.chants.add("/w <player> Houston final boss summrsxo");
            this.chants.add("/w <player> SummrsxoFan999");
        }
        else if (WhisperSpam.chatModes.getValue() == ChatModes.kankan) {
            this.chants.clear();
            this.chants.add("/w <player> DOUBLE R TRUCK");
            this.chants.add("/w <player> DOUBLE R GANG");
            this.chants.add("/w <player> ##RR");
            this.chants.add("/w <player> I used to be broke and ugly now im really rich");
            this.chants.add("/w <player> reallyrich.cc");
            this.chants.add("/w <player> Lean belly lean belly");
            this.chants.add("/w <player> I Luv kankan");
            this.chants.add("/w <player> Kumkum");
        }
        else if (WhisperSpam.chatModes.getValue() == ChatModes.dox) {
            this.chants.clear();
            this.chants.add("/w <player> IGN: Redd");
            this.chants.add("/w <player> Name: Enrique Dieguez");
            this.chants.add("/w <player> Phone Number: (954) 325-0540");
            this.chants.add("/w <player> Address: 5030 SW 120th Ave, Florida\n");
            this.chants.add("/w <player> Redd's Current School: 9401 Stirling Rd, Cooper City, FL 33328");
            this.chants.add("/w <player> School Zip Code: 33330");
            this.chants.add("/w <player> School Phone Number: (754) 323-4100");
            this.chants.add("/w <player> https://imgur.com/a/cHmayL0");
        }
        else if (WhisperSpam.chatModes.getValue() == ChatModes.random) {
            this.chants.clear();
            this.chants.add("/w <player> HUSH MODE BOY IS QUIET");
            this.chants.add("/w <player> Randroid randroid randroid");
            this.chants.add("/w <player> 2bpvp main lel");
            this.chants.add("/w <player> Nice pt bud");
            this.chants.add("/w <player> Nerd aa");
            this.chants.add("/w <player> binned binned binned");
            this.chants.add("/w <player> n1 n1 n1 n1 n1 n1n 1n 1");
            this.chants.add("/w <player> ALL MY OPPS SHOT");
        }
    }

    @Override
    public String onUpdate() {
        ++this.tick_delay;
        if (this.tick_delay < this.delay.getValue() * 10) {
        }
        final String s = this.chants.get(this.r.nextInt(this.chants.size()));
        final String name = this.get_random_name();
        if (name.equals(WhisperSpam.mc.player.getName())) {
        }
        WhisperSpam.mc.player.sendChatMessage(s.replace("<player>", name));
        this.tick_delay = 0;
        return s;
    }

    public String get_random_name() {
        final List<EntityPlayer> players = (List<EntityPlayer>)WhisperSpam.mc.world.playerEntities;
        return players.get(this.r.nextInt(players.size())).getName();
    }

    public enum ChatModes
    {
        bait,
        summrs,
        kankan,
        dox,
        random;
    }
}