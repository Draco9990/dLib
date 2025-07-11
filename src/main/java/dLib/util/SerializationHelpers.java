package dLib.util;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class SerializationHelpers {
    //region Methods

    //region Byte Array

    public static byte[] toByteArray(Object toSerialize){
        return toByteArray(toSerialize, true);
    }

    public static byte[] toByteArray(Object toSerialize, boolean compress){
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

            if(compress) {
                toReturn = compress(toReturn);
            }

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

    public static <T extends Serializable> T fromByteArray(byte[] b){
        return fromByteArray(b, true);
    }

    public static <T extends Serializable> T fromByteArray(byte[] b, boolean decompress){
        try{
            if(decompress) {
                b = decompress(b);
            }

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

    public static List<byte[]> splitByteArray(byte[] source, int chunksize) {
        List<byte[]> result = new ArrayList<byte[]>();
        int start = 0;
        while (start < source.length) {
            int end = Math.min(source.length, start + chunksize);
            result.add(Arrays.copyOfRange(source, start, end));
            start += chunksize;
        }

        return result;
    }

    public static byte[] mergeByteArray(ArrayList<byte[]> byteArrayList) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        if (byteArrayList != null) {
            byteArrayList.stream().filter(Objects::nonNull).forEach(array -> out.write(array, 0, array.length));
        }
        return out.toByteArray();
    }

    //endregion Byte Array

    //region Byte Buffer

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

    //endregion Byte Buffer

    //region String

    public static String toString(Serializable objectToSerialize){
        try {
            return Base64.getEncoder().encodeToString(toByteArray(objectToSerialize));
        }
        catch (Exception e){
            DLibLogger.logError("Failed to serialize object due to "+ e.getLocalizedMessage());
            e.printStackTrace();
        }

        return "";
    }

    public static <T extends Serializable> T fromString(String objectToDeserialize){
        try {
            T target = fromByteArray(Base64.getDecoder().decode(objectToDeserialize));
            if(target == null) target = fromByteArray(Base64.getDecoder().decode(objectToDeserialize), false);
            if(target == null) throw new Exception("Deserialization returned null");
            return target;
        }catch (Exception e){
            DLibLogger.log("Failed to deserialize from string due to " + e.getLocalizedMessage());
            e.printStackTrace();
        }

        return null;
    }

    //endregion String

    //region Compression

    public static byte[] compress(byte[] data) {
        Deflater deflater = new Deflater(Deflater.BEST_SPEED);
        deflater.setInput(data);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        return outputStream.toByteArray();
    }

    public static byte[] decompress(byte[] compressedData) {
        Inflater inflater = new Inflater();
        inflater.setInput(compressedData);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        try{
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
        } catch (DataFormatException e){
            DLibLogger.logError("Failed to decompress data due to " + e.getLocalizedMessage());
            e.printStackTrace();
        }
        return outputStream.toByteArray();
    }

    //endregion Compression

    public static int sizeOf(Object toSerialize){
        byte[] message = toByteArray(toSerialize);
        return message.length;
    }

    public static <T extends Serializable> T deepCopySerializable(T toCopy){
        return fromByteArray(toByteArray(toCopy));
    }

    //endregion Methods
}
