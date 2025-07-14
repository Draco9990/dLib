package dLib.properties.objects.templates;

import dLib.util.SerializationHelpers;
import dLib.util.events.localevents.BiConsumerEvent;
import dLib.util.events.localevents.TriConsumerEvent;

import java.io.Serializable;
import java.util.ArrayList;

public class TPropertyArray<ValueType> extends TProperty<ArrayList<TProperty<ValueType, ?>>, ValueType> implements Serializable {
    static final long serialVersionUID = 1L;

    private TProperty<ValueType, ?> propertyTemplate;

    private int minValueCount = -1;
    private int maxValueCount = -1;

    private int valueCountOverride = -1;

    public transient TriConsumerEvent<ValueType, ValueType, Integer> onValueChangedEvent = new TriConsumerEvent<>();
    public transient BiConsumerEvent<ValueType, Integer> onValueAddedEvent = new BiConsumerEvent<>();
    public transient BiConsumerEvent<ValueType, Integer> onValueRemovedEvent = new BiConsumerEvent<>();

    public TPropertyArray(TProperty<ValueType, ?> propertyTemplate) {
        super(new ArrayList<>());

        this.propertyTemplate = propertyTemplate;
    }

    public TPropertyArray(int valueCountOverride, TProperty<ValueType, ?> propertyTemplate) {
        super(new ArrayList<>());

        this.propertyTemplate = propertyTemplate;
        this.valueCountOverride = valueCountOverride;
    }

    public TPropertyArray(int minValueCount, int maxValueCount, TProperty<ValueType, ?> propertyTemplate) {
        super(new ArrayList<>());

        this.propertyTemplate = propertyTemplate;
        this.minValueCount = minValueCount;
        this.maxValueCount = maxValueCount;
    }

    //region Methods

    //region Array Manipulation methods

    public boolean add(ValueType val){
        if(isFull()) return false;

        TProperty<ValueType, ?> newProperty = SerializationHelpers.deepCopySerializable(propertyTemplate);
        boolean validVal = newProperty.setValue(val);
        if(!validVal) return false;

        value.add(newProperty);

        onValueAddedEvent.invoke(val, value.size() - 1);
        onValueChangedEvent.invoke(null, val, value.size() - 1);

        return true;
    }

    public boolean set(int index, ValueType val){
        if(index < 0 || index >= value.size()) return false;

        TProperty<ValueType, ?> existingProperty = value.get(index);
        ValueType oldValue = existingProperty.getValue();

        boolean validVal = existingProperty.setValue(val);
        if(!validVal) return false;

        onValueChangedEvent.invoke(oldValue, val, index);

        return true;
    }

    //endregion Array Manipulation methods

    //region Context providers

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

    //endregion Methods
}
