package dLib.util.bindings.method.staticbindings;

import dLib.properties.objects.BooleanProperty;
import dLib.properties.objects.Property;
import dLib.tools.uicreator.ui.properties.objects.UCUIElementBindingProperty;
import dLib.ui.bindings.RelativeUIElementBinding;
import dLib.ui.elements.UIElement;
import dLib.util.bindings.property.AbstractPropertyBinding;
import dLib.util.bindings.property.PropertyElementPathBinding;

import java.io.Serializable;

public class SetPropertyValueTargetedMethodBinding extends StaticMethodBinding implements Serializable {
    private static final long serialVersionUID = 1L;

    //For property editors
    private static final String PROPERTY_EDITOR_LONG_NAME = "Set Property Value (Target)";

    private UCUIElementBindingProperty target = new UCUIElementBindingProperty(new RelativeUIElementBinding())
            .setName("Target");

    private Property<AbstractPropertyBinding> instant = new Property<AbstractPropertyBinding>(new PropertyElementPathBinding("none", "none"))
            .setName("Property")
            .addIsPropertyVisibleFunction((_property) -> target.getValue().isBindingValid());

    public SetPropertyValueTargetedMethodBinding(){
        super();

        addDeclaredParam(target);
        addDeclaredParam(instant);

        target.onValueChangedEvent.subscribe(this, (oldValue, newValue) -> {
            instant.setValue(new PropertyElementPathBinding(newValue.getBoundObject().getRelativePath(), "none"));
        });
    }

    @Override
    public Object executeBinding(Object invoker, Object... args) {
        instant.getValue().getBoundObject(((UIElement) target.getValue().getBoundObject())).setValue(args[0]);
        return null;
    }

    @Override
    public String getDisplayValue() {
        return PROPERTY_EDITOR_LONG_NAME;
    }
}
