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

    public Setting<T> trySetValue(T newValue){
        T sanitized = sanitizeValue(newValue);
        if(isValidValue(sanitized)){
            setCurrentValue(sanitized);
        }

        return this;
    }
    public Setting<T> trySetValueFromObject(Object newValue){
        T val = (T) newValue;
        if(newValue != null){
            trySetValue(val);
        }

        return this;
    }

    protected Setting<T> setCurrentValue(T currentValue){
        this.currentValue = currentValue;
        onValueChanged();
        return this;
    }
    public T getCurrentValue(){
        return currentValue;
    }
    public String getValueForDisplay(){
        return currentValue.toString();
    }

    public T sanitizeValue(T newValue){ return newValue; }
    public boolean isValidValue(T value){
        return true;
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
