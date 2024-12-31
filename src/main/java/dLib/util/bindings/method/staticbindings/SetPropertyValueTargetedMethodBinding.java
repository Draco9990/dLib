package dLib.util.bindings.method.staticbindings;

import dLib.properties.objects.Property;
import dLib.tools.uicreator.ui.properties.objects.UCUIElementBindingProperty;
import dLib.ui.bindings.UIElementUndefinedPathBinding;
import dLib.ui.elements.UIElement;
import dLib.util.bindings.property.AbstractPropertyBinding;
import dLib.util.bindings.property.PropertyElementPathBinding;
import dLib.util.bindings.property.PropertyElementPathUndefinedBinding;

import java.io.Serializable;

public class SetPropertyValueTargetedMethodBinding extends StaticMethodBinding implements Serializable {
    private static final long serialVersionUID = 1L;

    //For property editors
    private static final String PROPERTY_EDITOR_LONG_NAME = "Set Property Value (Target)";

    private UCUIElementBindingProperty target = new UCUIElementBindingProperty(new UIElementUndefinedPathBinding())
            .setName("Target");

    private Property<AbstractPropertyBinding> boundProperty = new Property<AbstractPropertyBinding>(new PropertyElementPathUndefinedBinding())
            .setName("Property")
            .addIsPropertyVisibleFunction((_property) -> target.getValue().isBindingValid());

    public SetPropertyValueTargetedMethodBinding(){
        super();

        addDeclaredParam(target);
        addDeclaredParam(boundProperty);

        target.onValueChangedEvent.subscribe(this, (oldValue, newValue) -> {
            if(newValue.getBoundObject() == null){
                boundProperty.setValue(new PropertyElementPathUndefinedBinding());
            }
            else{
                boundProperty.setValue(new PropertyElementPathBinding(newValue.getBoundObject().getElementPath(), "none"));
            }
        });
    }

    @Override
    public Object executeBinding(Object invoker, Object... args) {
        boundProperty.getValue().getBoundObject(((UIElement) target.getValue().getBoundObject())).setValue(args[0]);
        return null;
    }

    @Override
    public String getDisplayValue() {
        return PROPERTY_EDITOR_LONG_NAME;
    }
}
