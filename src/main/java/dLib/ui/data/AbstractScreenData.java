package dLib.ui.data;

import dLib.util.DLibLogger;

import java.io.*;
import java.util.ArrayList;

public class AbstractScreenData implements Serializable {
    private static final long serialVersionUID = 1L;

    public ArrayList<UIElementData> data = new ArrayList<>();

    public void serialize(String filePath){
        try (FileOutputStream file = new FileOutputStream(filePath);
             ObjectOutputStream out = new ObjectOutputStream(file)) {
            out.writeObject(this);
        }catch (Exception e){
            DLibLogger.log("Failed to serialize screen data due to " + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    public static AbstractScreenData deserialize(String filePath) {
        try (FileInputStream file = new FileInputStream(filePath);
             ObjectInputStream in = new ObjectInputStream(file)) {
            return (AbstractScreenData) in.readObject();
        }catch (Exception e){
            DLibLogger.log("Failed to deserialize screen data due to " + e.getLocalizedMessage());
            e.printStackTrace();
        }

        return null;
    }
}
