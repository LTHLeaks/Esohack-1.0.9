package com.esoterik.client.features.modules.misc;

import com.esoterik.client.esohack;
import com.esoterik.client.event.events.PacketEvent;
import com.esoterik.client.features.modules.Module;
import com.esoterik.client.features.setting.Setting;
import com.esoterik.client.util.TextUtil;
import com.esoterik.client.util.Timer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatModifier extends Module {

    public Setting<TextUtil.Color> timeStamps = this.register(new Setting<TextUtil.Color>("Time", TextUtil.Color.NONE));
    public Setting<TextUtil.Color> bracket = this.register(new Setting<Object>("BracketColor", (Object)TextUtil.Color.WHITE, v -> this.timeStamps.getValue() != TextUtil.Color.NONE));
    public Setting<Boolean> suffix = this.register(new Setting<Boolean>("ChatSuffix", Boolean.valueOf(true), "Appends esohack suffix to all messages."));
    public Setting<Boolean> versionSuffix = this.register(new Setting<Object>("IncludeVersion", Boolean.valueOf(true), v -> this.suffix.getValue()));
    public Setting<Boolean> clean = this.register(new Setting<Boolean>("CleanChat", Boolean.valueOf(false), "Cleans your chat."));
    public Setting<Boolean> infinite = this.register(new Setting<Boolean>("InfiniteLength", Boolean.valueOf(false), "Makes your chat infinitely scrollable."));
    private final Timer timer = new Timer();
    private static ChatModifier INSTANCE = new ChatModifier();

    public ChatModifier() {
        super("CustomChat", "Customises aspects of the chat", Module.Category.MISC, true, false, false);
        this.setInstance();
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public static ChatModifier getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ChatModifier();
        }
        return INSTANCE;
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (event.getStage() == 0 && event.getPacket() instanceof CPacketChatMessage) {
            CPacketChatMessage packet = (CPacketChatMessage)event.getPacket();
            String s = packet.getMessage();
            if (s.startsWith("/") || s.startsWith("!")) {
                return;
            }
            if (this.suffix.getValue().booleanValue()) {
                s = s + " \u23d0 " + esohack.getName();
                if (this.versionSuffix.getValue().booleanValue()) {
                    s = s + " v1.0.5";
                }
            }
            if (s.length() >= 256) {
                s = s.substring(0, 256);
            }
            //packet.message = s;
        }
    }

    @SubscribeEvent
    public void onPacketReceive(final PacketEvent.Receive event) {
        if (event.getStage() == 0 && this.timeStamps.getValue() != TextUtil.Color.NONE && event.getPacket() instanceof SPacketChat) {
            if (!((SPacketChat) event.getPacket()).isSystem()) {
                return;
            }
            SPacketChat packet = ( SPacketChat )event.getPacket( );
            final String originalMessage = packet.getChatComponent().getUnformattedText();
            final String message = this.getTimeString() + originalMessage;
        }
    }

    public String getTimeString() {
        String date = new SimpleDateFormat("k:mm").format(new Date());
        return (this.bracket.getValue() == TextUtil.Color.NONE ? "" : TextUtil.coloredString("<", this.bracket.getValue())) + TextUtil.coloredString(date, this.timeStamps.getValue()) + (this.bracket.getValue() == TextUtil.Color.NONE ? "" : TextUtil.coloredString(">", this.bracket.getValue())) + " " + "\u00a7r";
    }

    private boolean shouldSendMessage(EntityPlayer player) {
        if (player.dimension != 1) {
            return false;
        }
        return player.getPosition().equals((Object)new Vec3i(0, 240, 0));
    }
}

