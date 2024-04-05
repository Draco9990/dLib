package dLib.util.settings.prefabs;

import dLib.ui.elements.propertyeditors.AbstractPropertyEditor;
import dLib.ui.elements.propertyeditors.IntegerVector2PropertyEditor;
import dLib.util.IntegerVector2;
import dLib.util.settings.Property;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;
import java.util.function.BiConsumer;

public class IntegerVector2Property extends Property<IntegerVector2> implements Serializable {
    static final long serialVersionUID = 1L;
    //region Variables

    private String xValName;
    private String yValName;

    private transient ArrayList<BiConsumer<Integer, Integer>> onXValueChangedListeners = new ArrayList<>();
    private transient ArrayList<BiConsumer<Integer, Integer>> onYValueChangedListeners = new ArrayList<>();

    //endregion

    //region Constructors

    public IntegerVector2Property(IntegerVector2 value) {
        super(value);
    }

    //endregion

    //region Methods

    //region Value


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

    public IntegerVector2Property setXValue(Integer value){
        IntegerVector2 currentValue = getValue();
        currentValue.x = value;
        setValue(currentValue);

        return this;
    }
    public Integer getXValue(){
        return getValue().x;
    }

    public void onXValueChanged(Integer oldValue, Integer newValue){
        if(onXValueChangedListeners == null) onXValueChangedListeners = new ArrayList<>();
        for(BiConsumer<Integer, Integer> listener : onXValueChangedListeners) listener.accept(oldValue, newValue);
    }
    public IntegerVector2Property addOnXValueChangedListener(BiConsumer<Integer, Integer> listener){
        if(onXValueChangedListeners == null) onXValueChangedListeners = new ArrayList<>();
        onXValueChangedListeners.add(listener);
        return this;
    }

    public IntegerVector2Property setYValue(Integer value){
        IntegerVector2 currentValue = getValue();
        currentValue.y = value;
        setValue(currentValue);

        return this;
    }
    public Integer getYValue(){
        return getValue().y;
    }

    public void onYValueChanged(Integer oldValue, Integer newValue){
        if(onYValueChangedListeners == null) onYValueChangedListeners = new ArrayList<>();
        for(BiConsumer<Integer, Integer> listener : onYValueChangedListeners) listener.accept(oldValue, newValue);
    }
    public IntegerVector2Property addOnYValueChangedListener(BiConsumer<Integer, Integer> listener){
        if(onYValueChangedListeners == null) onYValueChangedListeners = new ArrayList<>();
        onYValueChangedListeners.add(listener);
        return this;
    }

    //endregion

    //region Value Names

    public IntegerVector2Property setXValueName(String name){
        return setValueNames(name, yValName);
    }
    public String getXValueName(){
        return xValName;
    }

    public IntegerVector2Property setYValueName(String name){
        return setValueNames(xValName, name);
    }
    public String getYValueName(){
        return yValName;
    }

    public IntegerVector2Property setValueNames(String xName, String yName){
        this.xValName = xName;
        this.yValName = yName;
        return this;
    }

    //endregion

    //region Edit UI

    public AbstractPropertyEditor makeEditUI(int xPos, int yPos, int width, int height) {
        return new IntegerVector2PropertyEditor(this, xPos, yPos, width, height);
    }

    //endregion

    @Override
    public IntegerVector2Property setName(String newTitle) {
        return (IntegerVector2Property) super.setName(newTitle);
    }


    //endregion
}
