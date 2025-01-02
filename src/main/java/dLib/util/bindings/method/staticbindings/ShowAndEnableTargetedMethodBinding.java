package dLib.util.bindings.method.staticbindings;

import dLib.properties.objects.BooleanProperty;
import dLib.tools.uicreator.ui.properties.objects.UCUIElementBindingProperty;
import dLib.ui.bindings.UIElementUndefinedRelativePathBinding;
import dLib.ui.elements.UIElement;

import java.io.Serializable;

public class ShowAndEnableTargetedMethodBinding extends StaticMethodBinding implements Serializable {
    private static final long serialVersionUID = 1L;

    //For property editors
    private static final String PROPERTY_EDITOR_LONG_NAME = "Show and Enable (Target)";

    private UCUIElementBindingProperty target = new UCUIElementBindingProperty(new UIElementUndefinedRelativePathBinding())
            .setName("Target");

    private BooleanProperty instant = new BooleanProperty(false)
            .setName("Instant");

    public ShowAndEnableTargetedMethodBinding(){
        super();

        addDeclaredParam(target);
        addDeclaredParam(instant);
    }

    @Override
    public Object executeBinding(Object invoker, Object... args) {
        if(invoker instanceof UIElement){
            UIElement bound = target.getValue().getBoundObject(invoker);
            if(bound != null){
                if(instant.getValue()){
                    bound.showAndEnableInstantly();
                }
                else{
                    bound.showAndEnable();
                }
            }
        }
        return null;
    }

    @Override
    public String getDisplayValue() {
        return PROPERTY_EDITOR_LONG_NAME;
    }
}
