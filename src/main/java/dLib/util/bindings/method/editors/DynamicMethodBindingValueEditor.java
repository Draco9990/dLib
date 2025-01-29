package dLib.util.bindings.method.editors;

import dLib.properties.objects.MethodBindingProperty;
import dLib.ui.elements.items.Spacer;
import dLib.ui.elements.items.buttons.Button;
import dLib.ui.elements.items.input.Inputfield;
import dLib.ui.elements.items.itembox.HorizontalBox;
import dLib.ui.elements.items.itembox.VerticalBox;
import dLib.util.bindings.method.DynamicMethodBinding;
import dLib.util.bindings.texture.Tex;
import dLib.util.ui.dimensions.Dim;

public class DynamicMethodBindingValueEditor extends MethodBindingValueEditor<DynamicMethodBinding> {

    private Inputfield dynamicMethodBinding;

    Button bindDynamicBindingButton;

    public DynamicMethodBindingValueEditor(DynamicMethodBinding value) {
        this(new MethodBindingProperty(value));
    }

    public DynamicMethodBindingValueEditor(MethodBindingProperty property) {
        super(property);

        HorizontalBox propertyValueBox = new HorizontalBox(Dim.fill(), Dim.px(50));
        {
            dynamicMethodBinding = new Inputfield(((DynamicMethodBinding) property.getValue()).getBoundMethod(), Dim.fill(), Dim.fill());
            dynamicMethodBinding.textBox.onTextChangedEvent.subscribeManaged(s -> ((DynamicMethodBinding) boundProperty.getValue()).setBoundMethod(((UIElementData) boundProperty.getOwningContainer()).rootOwnerId, s));

            propertyValueBox.addChild(dynamicMethodBinding);

            VerticalBox methodBindingOptionsBox = new VerticalBox(Dim.px(28), Dim.fill());
            {
                bindDynamicBindingButton = new Button(Dim.fill(), Dim.px(15));
                bindDynamicBindingButton.setTexture(Tex.stat("dLibResources/images/ui/uieditor/BindButton.png"));
                bindDynamicBindingButton.hideAndDisableInstantly();
                bindDynamicBindingButton.onLeftClickEvent.subscribe(this, () -> {
                    if(boundProperty.getValue() instanceof DynamicMethodBinding){
                        ((DynamicMethodBinding) boundProperty.getValue()).setBoundMethod(((UIElementData) boundProperty.getOwningContainer()).rootOwnerId, boundProperty.getDynamicCreationDefaultMethodName());
                        boundProperty.createDynamicMethod();
                    }
                    bindDynamicBindingButton.hideAndDisableInstantly();
                });
                methodBindingOptionsBox.addChild(bindDynamicBindingButton);
                methodBindingOptionsBox.addChild(new Spacer(Dim.fill(), Dim.fill()));
                methodBindingOptionsBox.addChild(makeSwapComboBox());
            }
            propertyValueBox.addChild(methodBindingOptionsBox);
        }

        ((DynamicMethodBinding)property.getValue()).getBoundMethodRaw().onValueChangedEvent.subscribe(this, (methodBinding, methodBinding2) -> {
            dynamicMethodBinding.textBox.setText(methodBinding2);
        });

        addChild(propertyValueBox);
    }
}
