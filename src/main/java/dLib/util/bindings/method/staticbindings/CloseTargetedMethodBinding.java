package dLib.util.bindings.method.staticbindings;

import dLib.tools.uicreator.ui.properties.objects.UCUIElementBindingProperty;
import dLib.ui.bindings.RelativeUIElementBinding;
import dLib.ui.elements.UIElement;

import java.io.Serializable;

public class CloseTargetedMethodBinding extends StaticMethodBinding implements Serializable {
    private static final long serialVersionUID = 1L;

    //For property editors
    private static final String PROPERTY_EDITOR_LONG_NAME = "Close (Target)";

    private UCUIElementBindingProperty target = new UCUIElementBindingProperty(new RelativeUIElementBinding())
            .setName("Target");

    public CloseTargetedMethodBinding(){
        super();

        addDeclaredParam(target);
    }

    @Override
    public Object executeBinding(Object invoker, Object... args) {
        if(invoker instanceof UIElement){
            UIElement bound = target.getValue().getBoundObject(invoker);
            if(bound != null){
                bound.close();
            }
        }
        return null;
    }

    @Override
    public String getDisplayValue() {
        return PROPERTY_EDITOR_LONG_NAME;
    }
}
