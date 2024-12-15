package dLib.util.bindings.method;

import dLib.util.bindings.method.staticbindings.CloseTopmostMethodBinding;
import dLib.util.bindings.method.staticbindings.NoneMethodBinding;

import java.util.ArrayList;

public class MethodBindingHelpers {
    public static ArrayList<MethodBinding> getPremadeMethodBindings(){
        ArrayList<MethodBinding> bindings = new ArrayList<>();
        bindings.add(new CloseTopmostMethodBinding());
        bindings.add(new DynamicMethodBinding(""));
        bindings.add(new NoneMethodBinding());
        return bindings;
    }
}
