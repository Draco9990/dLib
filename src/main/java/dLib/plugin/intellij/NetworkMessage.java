package dLib.plugin.intellij;

import com.google.gson.Gson;

import java.io.Serializable;

public class NetworkMessage implements Serializable {
    private static final long serialVersionUID = 1L;
    /** Variables */
    private String request;
    private String data;

    /** Constructors */
    public NetworkMessage(String request){
        this(request, "");
    }
    public NetworkMessage(String request, Object data){
        this.request = request;
        this.data = new Gson().toJson(data);
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public static NetworkMessage fromString(String message){
        if(message == null) return null;

        return new Gson().fromJson(message, NetworkMessage.class);
    }

    public String getRequest(){
        return request;
    }

    public <T> T getData(Class<T> dataClass){
        return new Gson().fromJson(data, dataClass);
    }

    public <T> T getDataFromObjectArr(Class<T> dataClass, int index){
        Object[] params = getData(Object[].class);
        return new Gson().fromJson((String) params[index], dataClass);
    }
}
