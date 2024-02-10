package dLib.util.settings;

import dLib.ui.elements.settings.AbstractSettingUI;

import java.io.Serializable;

public abstract class Setting<T> implements Serializable {
    static final long serialVersionUID = 1L;

    /** Variables */
    private Class<? extends Object> valueClass;

    protected T defaultValue;
    protected T currentValue;

    private String title;

    private Runnable onValueChangedConsumer;

    /** Constructors */
    public Setting(T value){
        valueClass = value.getClass();
        currentValue = value;
        defaultValue = value;
    }

    public Setting(Class<T> valClass){
        valueClass = valClass;
        currentValue = null;
        defaultValue = null;
    }

    /** Title */
    public Setting<T> setTitle(String newTitle){
        this.title = newTitle;
        return this;
    }
    public String getTitle(){
        return title;
    }

    /** Value */
    /** Getters and Setters */
    public Class<? extends Object> getValueClass(){
        return valueClass;
    }

    public Setting<T> setCurrentValue(T currentValue){
        this.currentValue = currentValue;
        onValueChanged();
        return this;
    }
    public Setting<T> setCurrentValueFromObject(Object currentValue){
        T val = (T) currentValue;
        if(val != null) this.currentValue = val;
        return this;
    }
    public T getCurrentValue(){
        return currentValue;
    }
    public String getValueForDisplay(){
        return currentValue.toString();
    }

    /** Methods */
    public final void reset(){
        setCurrentValue(defaultValue);
    }

    /** Callbacks */
    public void onValueChanged(){
        if(onValueChangedConsumer != null){
            onValueChangedConsumer.run();
        }
    }
    public Setting<T> setOnValueChangedConsumer(Runnable consumer){
        onValueChangedConsumer = consumer;
        return this;
    }

    /** UI */
    public abstract AbstractSettingUI makeUIFor(int xPos, int yPos, int width, int height);
}
