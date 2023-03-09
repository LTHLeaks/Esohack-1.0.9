package com.esoterik.client.features.modules.misc;

import com.esoterik.client.esohack;
import com.esoterik.client.features.modules.Module;
import com.esoterik.client.features.setting.Setting;
import com.esoterik.client.util.FileUtil;
import com.esoterik.client.util.Timer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Spammer extends Module {

    public Setting<Integer> delay = this.register(new Setting<Integer>("Delay", 10, 1, 20));
    public Setting<Boolean> greentext = this.register(new Setting<Boolean>("Greentext", false));
    public Setting<Boolean> random = this.register(new Setting<Boolean>("Random", false));
    public Setting<Boolean> loadFile = this.register(new Setting<Boolean>("LoadFile", false));
    private final Timer timer = new Timer();
    private final List<String> sendPlayers = new ArrayList<String>();
    private static final String fileName = "client/util/Spammer.txt";
    private static final String defaultMessage = esohack.getName() + " owns all https://discord.gg/wJq5nMEdNT";
    private static final List<String> spamMessages = new ArrayList<String>();
    private static final Random rnd = new Random();

    public Spammer() {
        super("Spammer", "Spams stuff.", Module.Category.MISC, true, false, false);
    }

    @Override
    public void onLoad() {
        this.readSpamFile();
        this.disable();
    }

    @Override
    public void onEnable() {
        if (Spammer.fullNullCheck()) {
            this.disable();
            return;
        }
        this.readSpamFile();
    }

    @Override
    public void onLogin() {
        this.disable();
    }

    @Override
    public void onLogout() {
        this.disable();
    }

    @Override
    public void onDisable() {
        spamMessages.clear();
        this.timer.reset();
    }

    @Override
    public void onUpdate() {
        if (Spammer.fullNullCheck()) {
            this.disable();
            return;
        }
        if (this.loadFile.getValue().booleanValue()) {
            this.readSpamFile();
            this.loadFile.setValue(false);
        }
        if (!this.timer.passedS(this.delay.getValue().intValue())) {
            return;
        }
        if (spamMessages.size() > 0) {
            String messageOut;
            if (this.random.getValue().booleanValue()) {
                int index = rnd.nextInt(spamMessages.size());
                messageOut = spamMessages.get(index);
                spamMessages.remove(index);
            } else {
                messageOut = spamMessages.get(0);
                spamMessages.remove(0);
            }
            spamMessages.add(messageOut);
            if (this.greentext.getValue().booleanValue()) {
                messageOut = "> " + messageOut;
            }
            Spammer.mc.player.connection.sendPacket((Packet)new CPacketChatMessage(messageOut.replaceAll("\u00a7", "")));
        }
        this.timer.reset();
    }

    private void readSpamFile() {
        List<String> fileInput = FileUtil.readTextFileAllLines(fileName);
        Iterator<String> i = fileInput.iterator();
        spamMessages.clear();
        while (i.hasNext()) {
            String s = i.next();
            if (s.replaceAll("\\s", "").isEmpty()) continue;
            spamMessages.add(s);
        }
        if (spamMessages.size() == 0) {
            spamMessages.add(defaultMessage);
        }
    }
}

