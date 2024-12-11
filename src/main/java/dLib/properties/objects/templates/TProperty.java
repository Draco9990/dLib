package dLib.properties.objects.templates;

import dLib.properties.ui.elements.AbstractPropertyEditor;
import dLib.util.DLibLogger;
import dLib.util.UIElementEvent;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.position.AbstractPosition;
import org.apache.logging.log4j.util.TriConsumer;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class TProperty<ValueType, PropertyType> implements Serializable {
    static final long serialVersionUID = 1L;

    //region Variables

    private String name;
    private String description;

    private String category = "Uncategorized";

    protected ValueType defaultValue;
    protected ValueType value;

    protected Class<? extends AbstractPropertyEditor> propertyEditorClass;

    public transient UIElementEvent<TriConsumer<PropertyType, ValueType, ValueType>> onValueChangedEvent = new UIElementEvent<>();

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
        if(value == this.value) return;

        ValueType oldValue = this.value;
        this.value = value;
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
        onValueChangedEvent.invoke(new Consumer<TriConsumer<PropertyType, ValueType, ValueType>>() {
            @Override
            public void accept(TriConsumer<PropertyType, ValueType, ValueType> propertyTypeValueTypeValueTypeTriConsumer) {
                propertyTypeValueTypeValueTypeTriConsumer.accept((PropertyType) this, oldValue, newValue);
            }
        });
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

    //region Property Editor

    public PropertyType setPropertyEditorClass(Class<? extends AbstractPropertyEditor> propertyEditorClass){
        this.propertyEditorClass = propertyEditorClass;
        return (PropertyType) this;
    }

    public <PropertyEditorClass extends AbstractPropertyEditor> PropertyEditorClass makePropertyEditor(AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, boolean multiline){
        try{
            if(propertyEditorClass.getConstructors().length == 0) return null;

            Constructor propertyMaker = propertyEditorClass.getConstructors()[0];
            return (PropertyEditorClass) propertyMaker.newInstance(this, xPos, yPos, width, multiline);
        }catch (Exception e){
            DLibLogger.logError("Failed to make a property editor due to " + e.getLocalizedMessage());
            e.printStackTrace();

            return null;
        }
    }

    //endregion Property Editor

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

    //endregion
}