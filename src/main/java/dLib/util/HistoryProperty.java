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

    public T get(){
        return values.get(0);
    }

    public void revert(){
        if (values.size() == 1) return;

        values.remove(0);
    }

    public void reset(){
        while (values.size() > 1){
            values.remove(0);
        }
    }
}
