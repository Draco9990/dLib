package dLib.util.settings;

import dLib.ui.elements.settings.AbstractSettingUI;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.function.BiConsumer;

public abstract class Property<T> implements Serializable {
    static final long serialVersionUID = 1L;

    //region Variables

    private String name;

    protected T defaultValue;
    protected T value;

    private transient ArrayList<BiConsumer<T, T>> onValueChangedListeners = new ArrayList<>();

    //endregion

    //region Settings

    public Property(T value){
        this.value = value;
        defaultValue = value;
    }

    //endregion Settings

    //region Constructors
    //endregion

    //region Methods

    //region Name

    public Property<T> setName(String newTitle){
        this.name = newTitle;
        return this;
    }
    public String getName(){
        return name;
    }

    //endregion

    //region Value

    public final boolean setValue(T newValue){
        T sanitized = sanitizeValue(newValue);
        if(isValidValue(sanitized)){
            setValue_internal(sanitized);
            return true;
        }

        return false;
    }
    private void setValue_internal(T value){
        T oldValue = this.value;
        this.value = value;
        onValueChanged(oldValue, value);
    }

    public T getValue(){
        return value;
    }
    public String getValueForDisplay(){
        return value.toString();
    }

    public T sanitizeValue(T newValue){ return newValue; }
    public boolean isValidValue(T value){
        return true;
    }

    public final void resetDefaultValue(){
        setValue_internal(defaultValue);
    }

    public void onValueChanged(T oldValue, T newValue){
        for(BiConsumer<T, T> listener : onValueChangedListeners) listener.accept(oldValue, newValue);
    }
    public Property<T> addOnValueChangedListener(BiConsumer<T, T> listener){
        onValueChangedListeners.add(listener);
        return this;
    }

    //endregion

    //region Edit UI

    public abstract AbstractSettingUI makeUIForEdit(int xPos, int yPos, int width, int height);

    //endregion

    //endregion
}
