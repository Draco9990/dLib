package dLib.properties.objects.templates;

import com.badlogic.gdx.math.Vector2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;
import java.util.function.BiConsumer;

public abstract class TFloatVector2Property<PropertyType> extends TProperty<Vector2, PropertyType> implements Serializable {
    static final long serialVersionUID = 1L;
    //region Variables

    private String xValName;
    private String yValName;

    private transient ArrayList<BiConsumer<Float, Float>> onXValueChangedListeners = new ArrayList<>();
    private transient ArrayList<BiConsumer<Float, Float>> onYValueChangedListeners = new ArrayList<>();

    private Float minimumX;
    private Float maximumX;

    private Float minimumY;
    private Float maximumY;

    //endregion

    //region Constructors

    public TFloatVector2Property(Vector2 value) {
        super(value);
    }

    //endregion

    //region Methods

    //region Value

    @Override
    public boolean setValueFromString(String value) {
        throw new UnsupportedOperationException("FloatVector2Property does not support setting value from string");
    }

    @Override
    public void onValueChanged(Vector2 oldValue, Vector2 newValue) {
        super.onValueChanged(oldValue, newValue);

        if(!Objects.equals(oldValue.x, newValue.x)) onXValueChanged(oldValue.x, oldValue.y);
        if(!Objects.equals(oldValue.y, newValue.y)) onYValueChanged(oldValue.x, oldValue.y);
    }

    @Override
    public Vector2 getValue() {
        return new Vector2(super.getValue());
    }

    public TFloatVector2Property setXValue(float value){
        Vector2 currentValue = getValue();
        currentValue.x = value;
        setValue(currentValue);

        return this;
    }
    public float getXValue(){
        return getValue().x;
    }

    public void onXValueChanged(float oldValue, float newValue){
        if(onXValueChangedListeners == null) onXValueChangedListeners = new ArrayList<>();
        for(BiConsumer<Float, Float> listener : onXValueChangedListeners) listener.accept(oldValue, newValue);
    }
    public TFloatVector2Property addOnXValueChangedListener(BiConsumer<Float, Float> listener){
        if(onXValueChangedListeners == null) onXValueChangedListeners = new ArrayList<>();
        onXValueChangedListeners.add(listener);
        return this;
    }

    public TFloatVector2Property setYValue(float value){
        Vector2 currentValue = getValue();
        currentValue.y = value;
        setValue(currentValue);

        return this;
    }
    public float getYValue(){
        return getValue().y;
    }

    public void onYValueChanged(Float oldValue, Float newValue){
        if(onYValueChangedListeners == null) onYValueChangedListeners = new ArrayList<>();
        for(BiConsumer<Float, Float> listener : onYValueChangedListeners) listener.accept(oldValue, newValue);
    }
    public TFloatVector2Property addOnYValueChangedListener(BiConsumer<Float, Float> listener){
        if(onYValueChangedListeners == null) onYValueChangedListeners = new ArrayList<>();
        onYValueChangedListeners.add(listener);
        return this;
    }

    @Override
    public Vector2 sanitizeValue(Vector2 newValue) {
        if(minimumX != null && newValue.x < minimumX) newValue.x = minimumX;
        if(maximumX != null && newValue.x > maximumX) newValue.x = maximumX;

        if(minimumY != null && newValue.y < minimumY) newValue.y = minimumY;
        if(maximumY != null && newValue.y > maximumY) newValue.y = maximumY;

        newValue.x = (float) Math.round(newValue.x * 100) / 100;
        newValue.y = (float) Math.round(newValue.y * 100) / 100;

        return super.sanitizeValue(newValue);
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

    //region Value Bounds

    public PropertyType setMinimumX(Float minimumX){
        this.minimumX = minimumX;
        return (PropertyType) this;
    }

    public float getMinimumX(){
        return minimumX;
    }

    public PropertyType setMaximumX(Float maximumX){
        this.maximumX = maximumX;
        return (PropertyType) this;
    }

    public float getMaximumX(){
        return maximumX;
    }

    public PropertyType setMinimumY(Float minimumY){
        this.minimumY = minimumY;
        return (PropertyType) this;
    }

    public float getMinimumY(){
        return minimumY;
    }

    public PropertyType setMaximumY(Float maximumY){
        this.maximumY = maximumY;
        return (PropertyType) this;
    }

    public float getMaximumY(){
        return maximumY;
    }

    //endregion Value Bounds

    //endregion
}
