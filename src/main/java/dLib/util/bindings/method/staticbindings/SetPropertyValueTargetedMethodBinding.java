package dLib.util.bindings.method.staticbindings;

import dLib.properties.objects.BooleanProperty;
import dLib.tools.uicreator.ui.properties.objects.UCUIElementBindingProperty;
import dLib.ui.bindings.RelativeUIElementBinding;
import dLib.ui.elements.UIElement;

import java.io.Serializable;

public class SetPropertyValueTargetedMethodBinding extends StaticMethodBinding implements Serializable {
    private static final long serialVersionUID = 1L;

    //For property editors
    private static final String PROPERTY_EDITOR_LONG_NAME = "Set Property Value (Target)";

    private UCUIElementBindingProperty target = new UCUIElementBindingProperty(new RelativeUIElementBinding())
            .setName("Target");

    private BooleanProperty instant = new BooleanProperty(false)
            .setName("Property")
            .addIsPropertyVisibleFunction((_property) -> target.getValue().isBindingValid());

    public SetPropertyValueTargetedMethodBinding(){
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
    public String getDisplayValue() {
        return PROPERTY_EDITOR_LONG_NAME;
    }
}
