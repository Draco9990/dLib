package dLib.util.settings.prefabs;

import dLib.ui.elements.settings.AbstractSettingUI;
import dLib.ui.elements.settings.IntVector2SettingUI;
import dLib.util.IntVector2;
import dLib.util.settings.Property;

public class IntVector2Property extends Property<IntVector2> {
    /** Variables */
    private String xAxisName;
    private String yAxisName;

    /** Constructors */
    public IntVector2Property(IntVector2 value) {
        super(value);
    }

    /** Text */
    public IntVector2Property setXAxisName(String name){
        return setAxisNames(name, yAxisName);
    }
    public String getXAxisName(){
        return xAxisName;
    }

    public IntVector2Property setYAxisName(String name){
        return setAxisNames(xAxisName, name);
    }
    public String getYAxisName(){
        return yAxisName;
    }

    public IntVector2Property setAxisNames(String xName, String yName){
        this.xAxisName = xName;
        this.yAxisName = yName;
        return this;
    }

    /** UI */
    @Override
    public AbstractSettingUI makeUIForEdit(int xPos, int yPos, int width, int height) {
        return new IntVector2SettingUI(this, xPos, yPos, width, height);
    }
}
