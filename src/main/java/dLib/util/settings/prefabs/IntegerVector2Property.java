package dLib.util.settings.prefabs;

import dLib.ui.elements.settings.AbstractSettingUI;
import dLib.ui.elements.settings.IntVector2SettingUI;
import dLib.util.IntVector2;
import dLib.util.IntegerVector2;
import dLib.util.settings.Property;

public class IntegerVector2Property extends Property<IntegerVector2> {
    //region Variables

    private String xValName;
    private String yValName;

    //endregion

    //region Constructors

    public IntegerVector2Property(IntegerVector2 value) {
        super(value);
    }

    //endregion

    //region Methods

    //region Value


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

    public IntegerVector2Property setYValue(Integer value){
        IntegerVector2 currentValue = getValue();
        currentValue.y = value;
        setValue(currentValue);

        return this;
    }
    public Integer getYValue(){
        return getValue().y;
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

    @Override
    public AbstractSettingUI makeUIForEdit(int xPos, int yPos, int width, int height) {
        return new IntVector2SettingUI(this, xPos, yPos, width, height);
    }

    //endregion

    //endregion
}
