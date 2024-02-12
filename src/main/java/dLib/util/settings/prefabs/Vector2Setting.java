package dLib.util.settings.prefabs;

import com.badlogic.gdx.math.Vector2;
import dLib.ui.elements.settings.AbstractSettingUI;
import dLib.util.settings.Setting;

public class Vector2Setting extends Setting<Vector2> {
    /** Variables */
    private String xAxisName;
    private String yAxisName;

    /** Constructors */
    public Vector2Setting(Vector2 value) {
        super(value);
    }

    /** Text */
    public Vector2Setting setXAxisName(String name){
        return setAxisNames(name, yAxisName);
    }
    public String getXAxisName(){
        return xAxisName;
    }

    public Vector2Setting setYAxisName(String name){
        return setAxisNames(xAxisName, name);
    }
    public String getYAxisName(){
        return yAxisName;
    }

    public Vector2Setting setAxisNames(String xName, String yName){
        this.xAxisName = xName;
        this.yAxisName = yName;
        return this;
    }

    /** UI */
    @Override
    public AbstractSettingUI makeUIFor(int xPos, int yPos, int width, int height) {
        return null;
    }
}
