package dLib.properties.objects.templates;

import dLib.properties.ui.elements.AbstractValueEditor;
import dLib.properties.ui.elements.PropertyArrayValueEditor;
import dLib.util.SerializationHelpers;
import dLib.util.events.localevents.BiConsumerEvent;
import dLib.util.events.localevents.TriConsumerEvent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.function.BiConsumer;

public class TPropertyArray<ValueType, PropertyType> extends TProperty<ArrayList<TProperty<ValueType, ?>>, PropertyType> implements Serializable {
    static final long serialVersionUID = 1L;

    private TProperty<ValueType, ?> propertyTemplate;

    private int minValueCount = -1;
    private int maxValueCount = -1;

    private int valueCountOverride = -1;

    protected boolean bitwiseSelection = false;

    public transient TriConsumerEvent<ValueType, ValueType, Integer> onSingleValueChangedEvent = new TriConsumerEvent<>();
    public transient BiConsumerEvent<ValueType, Integer> onValueAddedEvent = new BiConsumerEvent<>();
    public transient BiConsumerEvent<ValueType, Integer> onValueRemovedEvent = new BiConsumerEvent<>();

    public TPropertyArray(TProperty<ValueType, ?> propertyTemplate) {
        super(new ArrayList<>());

        this.propertyTemplate = propertyTemplate;
    }

    public TPropertyArray(ArrayList<? extends TProperty<ValueType, ?>> defaults){
        super((ArrayList<TProperty<ValueType, ?>>) defaults);

        setValueCountOverride(defaults.size());
    }

    //region Methods

    //region Array Manipulation methods

    public boolean add(ValueType val){
        if(isFull()) return false;

        TProperty<ValueType, ?> newProperty = SerializationHelpers.deepCopySerializable(propertyTemplate);
        boolean validVal = newProperty.setValue(val);
        if(!validVal) return false;

        ArrayList<TProperty<ValueType, ?>> oldValArr = new ArrayList<>(value);
        value.add(newProperty);
        newProperty.onValueChangedEvent.subscribe(newProperty, (valueType, valueType2) -> onSingleValueChangedEvent.invoke(valueType, valueType2, value.size() - 1));

        onValueAddedEvent.invoke(val, value.size() - 1);
        onSingleValueChangedEvent.invoke(null, val, value.size() - 1);
        onValueChangedEvent.invoke(oldValArr, value);

        return true;
    }

    public boolean addBlank(){
        if(isFull()) return false;

        ArrayList<TProperty<ValueType, ?>> oldValArr = new ArrayList<>(value);

        TProperty<ValueType, ?> newProperty = SerializationHelpers.deepCopySerializable(propertyTemplate);
        value.add(newProperty);
        newProperty.onValueChangedEvent.subscribe(newProperty, (valueType, valueType2) -> onSingleValueChangedEvent.invoke(valueType, valueType2, value.size() - 1));

        onValueAddedEvent.invoke(null, value.size() - 1);
        onSingleValueChangedEvent.invoke(null, null, value.size() - 1);
        onValueChangedEvent.invoke(oldValArr, value);

        return true;
    }

    public boolean set(int index, ValueType val){
        if(index < 0 || index >= value.size()) return false;

        TProperty<ValueType, ?> existingProperty = value.get(index);
        ValueType oldValue = existingProperty.getValue();

        ArrayList<TProperty<ValueType, ?>> oldValArr = new ArrayList<>(value);

        boolean validVal = existingProperty.setValue(val);
        if(!validVal) return false;

        onSingleValueChangedEvent.invoke(oldValue, val, index);

        return true;
    }

    public boolean remove(int index){
        if(index < 0 || index >= value.size()) return false;

        ArrayList<TProperty<ValueType, ?>> oldValArr = new ArrayList<>(value);
        TProperty<ValueType, ?> removedProperty = value.remove(index);

        onValueRemovedEvent.invoke(removedProperty.getValue(), index);
        onSingleValueChangedEvent.invoke(removedProperty.getValue(), null, index);
        onValueChangedEvent.invoke(oldValArr, value);

        return true;
    }

    public boolean remove(TProperty<ValueType, ?> property){
        int index = value.indexOf(property);
        if(index < 0) return false;

        return remove(index);
    }

    public ArrayList<ValueType> getValues(){
        ArrayList<ValueType> values = new ArrayList<>();
        for (TProperty<ValueType, ?> property : value) {
            values.add(property.getValue());
        }
        return values;
    }

    //endregion Array Manipulation methods

    //region Context providers

    public boolean canDelete(){
        if(valueCountOverride > -1){
            return getValue().size() > valueCountOverride;
        }

        if(minValueCount > -1){
            return getValue().size() > minValueCount;
        }

        return !getValue().isEmpty();
    }

    public boolean isFull(){
        if(valueCountOverride > -1){
            return getValue().size() >= valueCountOverride;
        }

        if(maxValueCount > -1){
            return getValue().size() >= maxValueCount;
        }

        return false;
    }

    //endregion Context providers

    //region Value Count

    public PropertyType setMinValueCount(int minValueCount) {
        this.minValueCount = minValueCount;
        return (PropertyType) this;
    }
    public int getMinValueCount() {
        return minValueCount;
    }

    public PropertyType setMaxValueCount(int maxValueCount) {
        this.maxValueCount = maxValueCount;
        return (PropertyType) this;
    }
    public int getMaxValueCount() {
        return maxValueCount;
    }

    public PropertyType setValueCountOverride(int valueCountOverride) {
        this.valueCountOverride = valueCountOverride;
        return (PropertyType) this;
    }
    public int getValueCountOverride() {
        return valueCountOverride;
    }


    //endregion Value Count

    //region Bitwise Selection

    public boolean isBitwiseSelection() {
        return bitwiseSelection;
    }
    public void setBitwiseSelection(boolean bitwiseSelection) {
        this.bitwiseSelection = bitwiseSelection;
    }

    //endregion Bitwise Selection

    //region Value Editor

    @Override
    public AbstractValueEditor makeEditorFor() {
        return new PropertyArrayValueEditor(this);
    }

    @Override
    public AbstractValueEditor makeEditorFor(TProperty property) {
        return new PropertyArrayValueEditor<>(property);
    }

    @Override
    public AbstractValueEditor makeEditorFor(boolean tryMultiline) {
        return new PropertyArrayValueEditor<>(this, tryMultiline);
    }


    //endregion Value Editor

    //endregion Methods
}
