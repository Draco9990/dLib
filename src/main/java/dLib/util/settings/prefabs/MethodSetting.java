package dLib.util.settings.prefabs;

import dLib.util.bindings.method.MethodBinding;
import dLib.util.bindings.method.MethodBindingHelpers;
import dLib.util.bindings.method.NoneMethodBinding;
import dLib.util.bindings.texture.TextureBinding;
import dLib.util.bindings.texture.TextureBindingHelpers;
import dLib.util.settings.Setting;

import java.util.ArrayList;

public class MethodSetting extends CustomSetting<MethodBinding> {
    /** Variables */
    private Class<?>[] params = null;

    /** Constructors */
    public MethodSetting(MethodBinding value) {
        super(value);
    }

    public MethodSetting() {
        this(new NoneMethodBinding());
    }

    /** Title override */
    @Override
    public MethodSetting setTitle(String newTitle) {
        super.setTitle(newTitle);
        return this;
    }

    /** Params */
    public MethodSetting declareParams(Class<?>... paramsIn){
        this.params = paramsIn;
        return this;
    }

    /** All options */
    @Override
    public ArrayList<MethodBinding> getAllOptions() {
        return MethodBindingHelpers.getPremadeMethodBindings();
    }
}
