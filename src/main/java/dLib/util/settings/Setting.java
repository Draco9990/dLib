package dLib.util.settings;

import java.io.Serializable;

public abstract class Setting<T> implements Serializable {
    static final long serialVersionUID = 1L;

    /** Variables */
    private Class<? extends Object> valueClass;

    protected T defaultValue;
    protected T currentValue;

    private String title;
    private SettingCategory category = SettingCategory.NONE;
    private int priority = 0;
    private boolean spacedOut = false;

    private boolean isSubSetting;

    /** Constructors */
    public Setting(T value){
        valueClass = value.getClass();
        currentValue = value;
        defaultValue = value;
    }

    /** Getters and Setters */
    public Class<? extends Object> getValueClass(){
        return valueClass;
    }

    public final Setting<T> setCurrentValue(T currentValue){
        this.currentValue = currentValue;
        return this;
    }
    public final Setting<T> setCurrentValueFromObject(Object currentValue){
        T val = (T) currentValue;
        if(val != null) this.currentValue = val;
        return this;
    }
    public final T getCurrentValue(){
        return currentValue;
    }
    public String getValueForDisplay(){
        return currentValue.toString();
    }

    public Setting<T> setTitle(String newTitle){
        this.title = newTitle;
        return this;
    }
    public String getTitle(){
        return title;
    }

    public Setting<T> setCategory(SettingCategory category){
        this.category = category;
        return this;
    }
    public SettingCategory getCategory(){
        return category;
    }

    public Setting<T> setPriority(int priority){
        this.priority = priority;
        return this;
    }
    public int getPriority(){
        return priority;
    }

    public Setting<T> markForSpaceOut(){
        this.spacedOut = true;
        return this;
    }
    public boolean isSpacedOut(){
        return this.spacedOut;
    }

    public Setting<T> markAsSubSetting(){
        isSubSetting = true;
        return this;
    }
    public boolean isSubSetting(){
        return isSubSetting;
    }

    /** Methods */
    public final void reset(){
        currentValue = defaultValue;
        onValueChanged();
    }

    /** Callbacks */
    public void onValueChanged(){}

    /** Enums */
    public enum SettingCategory{
        NONE,
        GAME,
        MULTIPLAYER,
        RESURRECTING,
        MONSTER
    }
}
