package dLib.util;

import java.util.ArrayList;
import java.util.Objects;

public class EnumHelpers {
    public static <T extends Enum<T>> Enum<T> previousEnum(Enum<T> currentValue){
        Object[] enums = currentValue.getDeclaringClass().getEnumConstants();
        for(int i = 0; i < enums.length; i++){
            if(Objects.equals(enums[i], currentValue)){
                i--;
                if(i < 0){
                    i = enums.length - 1;
                }

                return (Enum<T>) enums[i];
            }
        }

        return null;
    }

    public static <T extends Enum<T>> Enum<T> nextEnum(Enum<T> currentValue){
        Object[] enums = currentValue.getDeclaringClass().getEnumConstants();
        for(int i = 0; i < enums.length; i++){
            if(Objects.equals(enums[i], currentValue)){
                i++;
                if(i >= enums.length){
                    i = 0;
                }

                return (Enum<T>) enums[i];
            }
        }

        return null;
    }

    public static <T extends Enum<T>> ArrayList<Enum<T>> getAllEntries(Enum<T> currentValue){
        ArrayList<Enum<T>> allEntries = new ArrayList<>();

        Object[] enums = currentValue.getDeclaringClass().getEnumConstants();
        for(Object enumInstance : enums){
            allEntries.add((Enum<T>) enumInstance);
        }

        return allEntries;
    }

    public static String betterToString(Enum<?> e){
        return e.name().toLowerCase().replace("_", " ");
    }
}
