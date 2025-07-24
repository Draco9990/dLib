package dLib.util;

import java.util.ArrayList;

public class HistoryProperty<T> {
    private ArrayList<T> values = new ArrayList<>();

    public HistoryProperty(T value){
        values.add(value);
    }

    public void set(T value){
        values.add(0, value);
    }

    public void insertBeforeTop(T value){
        if (!values.isEmpty()) {
            values.add(1, value);
        } else {
            values.add(value);
        }
    }

    public void removeAnyInstance(T value){
        values.removeIf(v -> v != null && v.equals(value));
    }

    public void removeAnyClassInstance(Class<? extends T> value){
        values.removeIf(v -> v != null && v.getClass().equals(value));
    }

    public T get(){
        return values.get(0);
    }

    public T revert(){
        if (values.size() == 1) return null;

        return values.remove(0);
    }

    public void reset(){
        while (values.size() > 1){
            values.remove(0);
        }
    }
}
