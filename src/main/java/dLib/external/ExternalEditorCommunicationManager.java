package dLib.external;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.core.CardCrawlGame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExternalEditorCommunicationManager {
    /** Variables */
    private static ExternalEditorCommunicationClient client;

    private static List<NetworkMessage> pendingMessages;

    private static boolean isEnabled = false;

    /** Methods */
    public static void start(){
        if(isRunning()) return;
        if(!isEnabled) return;

        pendingMessages = Collections.synchronizedList(new ArrayList<>());

        client = new ExternalEditorCommunicationClient();
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

    public static void sendMessage(String request, Object... data){
        if(!isRunning()) return;

        if(data.length == 1){
            client.sendMessage(new NetworkMessage(request, data[0]));
            return;
        }

        String[] messageArr = new String[data.length];
        for (int i = 0; i < data.length; i++) {
            Object dataObj = data[i];
            messageArr[i] = new Gson().toJson(dataObj);
        }
        client.sendMessage(new NetworkMessage(request, messageArr));
    }
}
