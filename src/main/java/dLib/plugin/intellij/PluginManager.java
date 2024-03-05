package dLib.plugin.intellij;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PluginManager {
    /** Variables */
    private static PluginClient client;

    private static List<NetworkMessage> pendingMessages;

    private static boolean isEnabled = false;

    /** Methods */
    public static void start(){
        if(isRunning()) return;
        if(!isEnabled) return;

        pendingMessages = Collections.synchronizedList(new ArrayList<>());

        client = new PluginClient();
        client.initialize();
    }

    @SpirePatch(clz = CardCrawlGame.class, method = "update")
    public static class ClientUpdater{
        public static void Postfix(){
            if(isRunning()){
                if(!pendingMessages.isEmpty()){
                    NetworkMessage pendingMessage = pendingMessages.remove(0);
                    if(pendingMessage != null){
                        MessageAnalyzer.analyzeMessage(pendingMessage);
                    }
                }
            }
        }
    }

    public static void shutdown(){
        if(!isRunning()) return;

        client.terminate();
        pendingMessages.clear();

        client = null;
    }

    public static void addMessage(NetworkMessage message){
        pendingMessages.add(message);
    }

    public static boolean isRunning(){
        return client != null && client.isConnected();
    }
    public static boolean isEnabled() {
        return isEnabled;
    }

    public static void enable(){
        isEnabled = true;
    }

    public static void disable(){
        isEnabled = false;
    }

    public static void sendMessage(String request, Object data){
        if(!isRunning()) return;
        client.sendMessage(new NetworkMessage(request, data));
    }
}
