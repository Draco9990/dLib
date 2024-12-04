package dLib.properties.objects;

import dLib.properties.ui.elements.AbstractPropertyEditor;
import dLib.util.DLibLogger;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.Function;

public abstract class Property<T> implements Serializable {
    static final long serialVersionUID = 1L;

    //region Variables

    private String name;
    private String description;

    private String category;

    protected T defaultValue;
    protected T value;

    protected Class<? extends AbstractPropertyEditor> propertyEditorClass;

    private transient ArrayList<BiConsumer<T, T>> onValueChangedListeners = new ArrayList<>();

    private transient ArrayList<Function<Property<T>, Boolean>> isPropertyVisibleFunctions = new ArrayList<>();

    //endregion

    //region Constructors

    public Property(T value){
        this.value = value;
        defaultValue = value;
    }

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
    protected void setValue_internal(T value){
        T oldValue = this.value;
        this.value = value;
        onValueChanged(oldValue, value);
    }

    public abstract boolean setValueFromString(String value);

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
        if(onValueChangedListeners == null) onValueChangedListeners = new ArrayList<>();
        for(BiConsumer<T, T> listener : onValueChangedListeners) listener.accept(oldValue, newValue);
    }

    public Property<?> addOnValueChangedListener(BiConsumer<T, T> listener){
        if(onValueChangedListeners == null) onValueChangedListeners = new ArrayList<>();
        onValueChangedListeners.add(listener);
        return this;
    }

    public Property<?> removeOnValueChangedListener(BiConsumer<T, T> listener){
        if(onValueChangedListeners == null) onValueChangedListeners = new ArrayList<>();
        onValueChangedListeners.remove(listener);
        return this;
    }

    //endregion

    //region Description

    public Property<T> setDescription(String description){
        this.description = description;
        return this;
    }

    public String getDescription(){
        return description;
    }

    //endregion Description

    //region Category

    public Property<?> setCategory(String category){
        this.category = category;
        return this;
    }

    public String getCategory(){
        return category;
    }

    //endregion Category

    //region Property Editor

    public Property<T> setPropertyEditorClass(Class<? extends AbstractPropertyEditor> propertyEditorClass){
        this.propertyEditorClass = propertyEditorClass;
        return this;
    }

    public <PropertyEditorClass extends AbstractPropertyEditor> PropertyEditorClass makePropertyEditor(int xPos, int yPos, int width, int height){
        try{
            if(propertyEditorClass.getConstructors().length == 0) return null;

            Constructor propertyMaker = propertyEditorClass.getConstructors()[0];
            return (PropertyEditorClass) propertyMaker.newInstance(this, xPos, yPos, width, height);
        }catch (Exception e){
            DLibLogger.logError("Failed to make a property editor due to " + e.getLocalizedMessage());
            e.printStackTrace();

            return null;
        }
    }

    //endregion Property Editor

    //region Visibility

    public boolean isVisible(){
        for(Function<Property<T>, Boolean> f : isPropertyVisibleFunctions){
            if(!f.apply(this)) return false;
        }
        return true;
    }

    public <PropertyClass extends Property<T>> PropertyClass addIsPropertyVisibleFunction(Function<Property<T>, Boolean> f){
        isPropertyVisibleFunctions.add(f);
        return (PropertyClass) this;
    }

    //endregion

    //endregion
}
