package dLib.util.utils;

import java.util.ArrayList;

public class ArrayUtils {
    public static <T extends Object> ArrayList<T> listGetAdded(ArrayList<T> oldList, ArrayList<T> newList){
        ArrayList<T> objects = new ArrayList<>();

        for(T newObj : newList){
            boolean found = false;
            for(T oldObj : oldList){
                if(newObj.equals(oldObj)){
                    found = true;
                }
            }

            if(!found){
                objects.add(newObj);
            }
        }

        return objects;
    }

    public static <T extends Object> ArrayList<T> listGetRemoved(ArrayList<T> oldList, ArrayList<T> newList){
        return listGetAdded(newList, oldList);
    }
}
