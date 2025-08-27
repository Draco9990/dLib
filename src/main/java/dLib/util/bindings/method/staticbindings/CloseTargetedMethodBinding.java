package dLib.util.bindings.method.staticbindings;

import dLib.properties.objects.Property;
import dLib.ui.bindings.AbstractUIElementBinding;
import dLib.ui.bindings.UIElementUndefinedRelativePathBinding;
import dLib.ui.elements.UIElement;

import java.io.Serializable;

public class CloseTargetedMethodBinding extends StaticMethodBinding implements Serializable {
    private static final long serialVersionUID = 1L;

    //For property editors
    private static final String PROPERTY_EDITOR_LONG_NAME = "Close (Target)";

    private Property<AbstractUIElementBinding> target = new Property<AbstractUIElementBinding>(new UIElementUndefinedRelativePathBinding())
            .setName("Target");

    public CloseTargetedMethodBinding(){
        super();

        addDeclaredParam(target);
    }

    @Override
    public Object resolve(Object invoker, Object... args) {
        if(invoker instanceof UIElement){
            UIElement bound = target.getValue().resolve(invoker);
            if(bound != null){
                bound.close();
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return PROPERTY_EDITOR_LONG_NAME;
    }
}
