package dLib.properties.objects.templates;

import dLib.properties.ui.elements.IntegerVector2ValueEditor;
import dLib.util.IntegerVector2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;
import java.util.function.BiConsumer;

public abstract class TIntegerVector2Property<PropertyType> extends TProperty<IntegerVector2, PropertyType> implements Serializable {
    static final long serialVersionUID = 1L;
    //region Variables

    private String xValName;
    private String yValName;

    private transient ArrayList<BiConsumer<Integer, Integer>> onXValueChangedListeners = new ArrayList<>();
    private transient ArrayList<BiConsumer<Integer, Integer>> onYValueChangedListeners = new ArrayList<>();

    //endregion

    //region Constructors

    public TIntegerVector2Property(IntegerVector2 value) {
        super(value);
    }

    //endregion

    //region Methods

    //region Value

    @Override
    public boolean setValueFromString(String value) {
        throw new UnsupportedOperationException("IntegerVector2Property does not support setting value from string");
    }

    @Override
    public void onValueChanged(IntegerVector2 oldValue, IntegerVector2 newValue) {
        super.onValueChanged(oldValue, newValue);

        if(!Objects.equals(oldValue.x, newValue.x)) onXValueChanged(oldValue.x, oldValue.y);
        if(!Objects.equals(oldValue.y, newValue.y)) onYValueChanged(oldValue.x, oldValue.y);
    }

    @Override
    public IntegerVector2 getValue() {
        return new IntegerVector2(super.getValue());
    }

    public PropertyType setXValue(Integer value){
        IntegerVector2 currentValue = getValue();
        currentValue.x = value;
        setValue(currentValue);

        return (PropertyType) this;
    }
    public Integer getXValue(){
        return getValue().x;
    }

    public void onXValueChanged(Integer oldValue, Integer newValue){
        if(onXValueChangedListeners == null) onXValueChangedListeners = new ArrayList<>();
        for(BiConsumer<Integer, Integer> listener : onXValueChangedListeners) listener.accept(oldValue, newValue);
    }
    public PropertyType addOnXValueChangedListener(BiConsumer<Integer, Integer> listener){
        if(onXValueChangedListeners == null) onXValueChangedListeners = new ArrayList<>();
        onXValueChangedListeners.add(listener);
        return (PropertyType) this;
    }

    public PropertyType setYValue(Integer value){
        IntegerVector2 currentValue = getValue();
        currentValue.y = value;
        setValue(currentValue);

        return (PropertyType) this;
    }
    public Integer getYValue(){
        return getValue().y;
    }

    public void onYValueChanged(Integer oldValue, Integer newValue){
        if(onYValueChangedListeners == null) onYValueChangedListeners = new ArrayList<>();
        for(BiConsumer<Integer, Integer> listener : onYValueChangedListeners) listener.accept(oldValue, newValue);
    }
    public PropertyType addOnYValueChangedListener(BiConsumer<Integer, Integer> listener){
        if(onYValueChangedListeners == null) onYValueChangedListeners = new ArrayList<>();
        onYValueChangedListeners.add(listener);
        return (PropertyType) this;
    }

    //endregion

    //region Value Names

    public PropertyType setXValueName(String name){
        return setValueNames(name, yValName);
    }
    public String getXValueName(){
        return xValName;
    }

    public PropertyType setYValueName(String name){
        return setValueNames(xValName, name);
    }
    public String getYValueName(){
        return yValName;
    }

    public PropertyType setValueNames(String xName, String yName){
        this.xValName = xName;
        this.yValName = yName;
        return (PropertyType) this;
    }

    //endregion

    //endregion
}
