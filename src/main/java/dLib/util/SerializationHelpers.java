package dLib.util;

import java.io.*;
import java.util.Base64;

public class SerializationHelpers {
    public static String toString(Serializable objectToSerialize){
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream(); ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(objectToSerialize);
            return Base64.getEncoder().encodeToString(baos.toByteArray());
        }
        catch (Exception e){
            DLibLogger.logError("Failed to serialize object due to "+ e.getLocalizedMessage());
            e.printStackTrace();
        }

        return "";
    }

    public static Serializable fromString(String objectToDeserialize){
        byte[] data = Base64.getDecoder().decode(objectToDeserialize);
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data))) {
            return (Serializable) ois.readObject();
        }catch (Exception e){
            DLibLogger.log("Failed to deserialize AbstractScreenData due to " + e.getLocalizedMessage());
            e.printStackTrace();
        }

        return null;
    }

    public static <T extends Serializable> T deepCopySerializable(T toCopy){
        return (T) fromString(toString(toCopy));
    }
}
