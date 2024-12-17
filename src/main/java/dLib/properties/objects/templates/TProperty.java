package dLib.properties.objects.templates;

import dLib.properties.ui.elements.AbstractValueEditor;
import dLib.properties.ui.elements.IEditableValue;
import dLib.properties.ui.elements.PropertyValueEditor;
import dLib.util.events.Event;
import org.apache.logging.log4j.util.BiConsumer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Function;

public abstract class TProperty<ValueType, PropertyType> implements Serializable, IEditableValue {
    static final long serialVersionUID = 1L;

    //region Variables

    private String name;
    private String description;

    private String category = "Uncategorized";

    protected ValueType defaultValue;
    protected ValueType value;

    protected ValueType previousValue;

    public transient Event<BiConsumer<ValueType, ValueType>> onValueChangedEvent = new Event<>();

    private transient ArrayList<Function<PropertyType, Boolean>> isPropertyVisibleFunctions = new ArrayList<>();

    //endregion

    //region Constructors

    public TProperty(ValueType value){
        this.value = value;
        defaultValue = value;
    }

    //endregion

    //region Methods

    //region Name

    public PropertyType setName(String newTitle){
        this.name = newTitle;
        return (PropertyType) this;
    }
    public String getName(){
        return name;
    }

    //endregion

    //region Value

    public final boolean setValue(ValueType newValue){
        ValueType sanitized = sanitizeValue(newValue);
        if(isValidValue(sanitized)){
            setValue_internal(sanitized);
            return true;
        }

        return false;
    }
    protected void setValue_internal(ValueType value){
        if(Objects.equals(this.value, value)) return;

        ValueType oldValue = this.value;
        this.value = value;
        this.previousValue = oldValue;
        onValueChanged(oldValue, value);
    }

    public abstract boolean setValueFromString(String value);

    public ValueType getValue(){
        return value;
    }
    public String getValueForDisplay(){
        return value.toString();
    }

    public ValueType sanitizeValue(ValueType newValue){ return newValue; }
    public boolean isValidValue(ValueType value){
        return true;
    }

    public final void resetDefaultValue(){
        setValue_internal(defaultValue);
    }

    public void onValueChanged(ValueType oldValue, ValueType newValue){
        onValueChangedEvent.invoke(propertyTypeValueTypeValueTypeTriConsumer -> propertyTypeValueTypeValueTypeTriConsumer.accept(oldValue, newValue));
    }

    public ValueType getPreviousValue(){
        return previousValue;
    }

    //endregion

    //region Description

    public PropertyType setDescription(String description){
        this.description = description;
        return (PropertyType) this;
    }

    public String getDescription(){
        return description;
    }

    //endregion Description

    //region Category

    public PropertyType setCategory(String category){
        this.category = category;
        return (PropertyType) this;
    }

    public String getCategory(){
        return category;
    }

    //endregion Category

    //region Visibility

    public boolean isVisible(){
        for(Function<PropertyType, Boolean> f : isPropertyVisibleFunctions){
            if(!f.apply((PropertyType) this)) return false;
        }
        return true;
    }

    public PropertyType addIsPropertyVisibleFunction(Function<PropertyType, Boolean> f){
        isPropertyVisibleFunctions.add(f);
        return (PropertyType) this;
    }

    //endregion

    //region Editor

    @Override
    public AbstractValueEditor makeEditorFor() {
        return new PropertyValueEditor(this);
    }

    @Override
    public AbstractValueEditor makeEditorFor(TProperty property) {
        return new PropertyValueEditor(property);
    }

    public AbstractValueEditor makeEditorFor(boolean tryMultiline) {
        return new PropertyValueEditor(this, tryMultiline);
    }

    //endregion

    //endregion
}