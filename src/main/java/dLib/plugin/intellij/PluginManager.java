package dLib.plugin.intellij;

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
