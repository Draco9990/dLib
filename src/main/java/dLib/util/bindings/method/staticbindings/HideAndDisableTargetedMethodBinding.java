package dLib.util.bindings.method.staticbindings;

import dLib.properties.objects.BooleanProperty;
import dLib.properties.objects.Property;
import dLib.ui.bindings.AbstractUIElementBinding;
import dLib.ui.bindings.UIElementUndefinedRelativePathBinding;
import dLib.ui.elements.UIElement;

import java.io.Serializable;

public class HideAndDisableTargetedMethodBinding extends StaticMethodBinding implements Serializable {
    private static final long serialVersionUID = 1L;

    //For property editors
    private static final String PROPERTY_EDITOR_LONG_NAME = "Hide and Disable (Target)";

    private Property<AbstractUIElementBinding> target = new Property<AbstractUIElementBinding>(new UIElementUndefinedRelativePathBinding())
            .setName("Target");

    private BooleanProperty instant = new BooleanProperty(false)
            .setName("Instant");

    public HideAndDisableTargetedMethodBinding(){
        super();

        addDeclaredParam(target);
        addDeclaredParam(instant);
    }

    @Override
    public Object executeBinding(Object invoker, Object... args) {
        if(invoker instanceof UIElement){
            UIElement bound = target.getValue().resolve(invoker);
            if(bound != null){
                if(instant.getValue()){
                    bound.hideAndDisableInstantly();
                }
                else{
                    bound.hideAndDisable();
                }
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return PROPERTY_EDITOR_LONG_NAME;
    }
}
