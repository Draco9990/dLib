package dLib.util;

import java.io.*;
import java.nio.Buffer;
import java.nio.ByteBuffer;
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
    public static <T extends Serializable> T fromString(String objectToDeserialize){
        byte[] data = Base64.getDecoder().decode(objectToDeserialize);
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data))) {
            return (T) ois.readObject();
        }catch (Exception e){
            DLibLogger.log("Failed to deserialize AbstractScreenData due to " + e.getLocalizedMessage());
            e.printStackTrace();
        }

        return null;
    }

    public static byte[] toByteArray(Object toSerialize){
        try{
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(out);
            os.writeObject(toSerialize);
            os.flush();
            os.reset();
            os.close();
            byte[] toReturn = out.toByteArray();
            out.flush();
            out.reset();
            out.close();
            return toReturn;
        } catch (IOException e) {
            DLibLogger.logError("Could not serialize due to: " + e);
            e.printStackTrace();
            if(e instanceof UTFDataFormatException){
                DLibLogger.logError("Throwable: " + e.getCause().getMessage());
                e.getCause().printStackTrace();
            }
        }
        return null;
    }
    public static ByteBuffer toByteBuffer(Object toSerialize){
        byte[] message = toByteArray(toSerialize);
        ByteBuffer buffer = ByteBuffer.allocateDirect(message.length);
        buffer.put(message);
        buffer.flip();
        return buffer;
    }

    public static <T extends Serializable> T fromByteBuffer(ByteBuffer in){
        byte[] message = new byte[in.remaining()];
        in.get(message);
        return fromByteArray(message);
    }
    public static <T extends Serializable> T fromByteArray(byte[] b){
        try{
            ByteArrayInputStream in = new ByteArrayInputStream(b);
            ObjectInputStream is = new ObjectInputStream(in);
            T m = (T) is.readObject();
            in.close();
            is.close();
            return m;
        } catch (IOException | ClassNotFoundException e) {
            DLibLogger.logError("Could not deserialize due to: " + e);
            e.printStackTrace();
            if(e instanceof UTFDataFormatException){
                DLibLogger.logError("Throwable: " + e.getCause().getMessage());
                e.getCause().printStackTrace();
            }
        }
        return null;
    }

    public static int sizeOf(Object toSerialize){
        byte[] message = toByteArray(toSerialize);
        return message.length;
    }

    public static <T extends Serializable> T deepCopySerializable(T toCopy){
        return (T) fromString(toString(toCopy));
    }
}
