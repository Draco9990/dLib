package dLib.util.settings.prefabs;

import dLib.ui.elements.settings.AbstractSettingUI;
import dLib.ui.elements.settings.DynamicMethodSettingUI;
import dLib.util.bindings.method.DynamicMethodBinding;
import dLib.util.bindings.method.MethodBinding;
import dLib.util.bindings.method.MethodBindingHelpers;
import dLib.util.bindings.method.NoneMethodBinding;

import java.util.ArrayList;

public class MethodBindingSetting extends CustomSetting<MethodBinding> {
    /** Variables */
    private Class<?>[] params = null;

    /** Constructors */
    public MethodBindingSetting(MethodBinding value) {
        super(value);
    }

    public MethodBindingSetting() {
        this(new NoneMethodBinding());
    }

    /** Title override */
    @Override
    public MethodBindingSetting setTitle(String newTitle) {
        super.setTitle(newTitle);
        return this;
    }

    /** Params */
    public MethodBindingSetting declareParams(Class<?>... paramsIn){
        this.params = paramsIn;
        return this;
    }

    /** All options */
    @Override
    public ArrayList<MethodBinding> getAllOptions() {
        return MethodBindingHelpers.getPremadeMethodBindings();
    }

    /** UI */
    @Override
    public AbstractSettingUI makeUIFor(int xPos, int yPos, int width, int height) {
        if(getCurrentValue() instanceof DynamicMethodBinding){
            return new DynamicMethodSettingUI(this, xPos, yPos, width, height);
        }
        else{
            return super.makeUIFor(xPos, yPos, width, height);
        }
    }
}
