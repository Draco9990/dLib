package dLib.util.bindings.method.editors;

import dLib.properties.objects.MethodBindingProperty;
import dLib.properties.objects.templates.TMethodBindingProperty;
import dLib.properties.objects.templates.TProperty;
import dLib.properties.ui.elements.ValueEditorManager;
import dLib.ui.elements.prefabs.*;
import dLib.util.TextureManager;
import dLib.util.bindings.method.DynamicMethodBinding;
import dLib.util.bindings.method.staticbindings.StaticMethodBinding;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.padding.Padd;

import java.util.Map;

public class DynamicMethodBindingValueEditor extends MethodBindingValueEditor<DynamicMethodBinding> {

    private Inputfield dynamicMethodBinding;

    Button bindDynamicBindingButton;

    public DynamicMethodBindingValueEditor(DynamicMethodBinding value, AbstractDimension width, AbstractDimension height) {
        this(new MethodBindingProperty(value), width, height);
    }

    public DynamicMethodBindingValueEditor(MethodBindingProperty property, AbstractDimension width, AbstractDimension height) {
        super(property, width, height);

        HorizontalBox propertyValueBox = new HorizontalBox(width, Dim.px(50));
        {
            dynamicMethodBinding = new Inputfield(((DynamicMethodBinding) property.getValue()).getBoundMethod(), Dim.fill(), Dim.fill());
            dynamicMethodBinding.getTextBox().addOnTextChangedConsumer(s -> ((DynamicMethodBinding) boundProperty.getValue()).setBoundMethod(s));

            propertyValueBox.addItem(dynamicMethodBinding);

            VerticalBox methodBindingOptionsBox = new VerticalBox(Dim.px(28), Dim.fill());
            {
                bindDynamicBindingButton = new Button(Dim.fill(), Dim.px(15));
                bindDynamicBindingButton.setImage(TextureManager.getTexture("dLibResources/images/ui/uieditor/BindButton.png"));
                bindDynamicBindingButton.hideAndDisableInstantly();
                bindDynamicBindingButton.onLeftClickEvent.subscribe(this, () -> {
                    if(boundProperty.getValue() instanceof DynamicMethodBinding){
                        ((DynamicMethodBinding) boundProperty.getValue()).setBoundMethod(boundProperty.getDynamicCreationDefaultMethodName());
                        boundProperty.createDynamicMethod();
                    }
                    bindDynamicBindingButton.hideAndDisableInstantly();
                });
                methodBindingOptionsBox.addItem(bindDynamicBindingButton);
                methodBindingOptionsBox.addItem(new Spacer(Dim.fill(), Dim.fill()));
                methodBindingOptionsBox.addItem(makeSwapComboBox());
            }
            propertyValueBox.addItem(methodBindingOptionsBox);
        }

        property.onValueChangedEvent.subscribe(this, (methodBinding, methodBinding2) -> {
            if(!isEditorValidForPropertyChange()) return;

            dynamicMethodBinding.getTextBox().setText(((DynamicMethodBinding) methodBinding2).getBoundMethod());
        });

        addChildNCS(propertyValueBox);
    }
}
