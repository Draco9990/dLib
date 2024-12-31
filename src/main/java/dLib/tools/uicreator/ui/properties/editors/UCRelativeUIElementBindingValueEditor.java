package dLib.tools.uicreator.ui.properties.editors;

import com.badlogic.gdx.graphics.Texture;
import dLib.mousestates.MouseStateManager;
import dLib.tools.uicreator.UCEditor;
import dLib.tools.uicreator.ui.properties.objects.UCUIElementBindingProperty;
import dLib.ui.bindings.UIElementPathBinding;
import dLib.ui.elements.items.Button;
import dLib.ui.elements.items.itembox.HorizontalBox;
import dLib.ui.elements.items.text.ImageTextBox;
import dLib.ui.mousestates.ReferencePickerMouseState;
import dLib.ui.screens.UIManager;
import dLib.util.bindings.texture.Tex;
import dLib.util.ui.dimensions.Dim;

public class UCRelativeUIElementBindingValueEditor extends UCUIElementBindingValueEditor<UIElementPathBinding> {
    private ImageTextBox bindingBox;

    public UCRelativeUIElementBindingValueEditor(UIElementPathBinding value) {
        this(new UCUIElementBindingProperty(value));
    }

    public UCRelativeUIElementBindingValueEditor(UCUIElementBindingProperty property) {
        super(property);

        HorizontalBox mainContentBox = new HorizontalBox(Dim.fill(), Dim.px(50));
        {
            Button referenceButton = new Button(Dim.mirror(), Dim.fill());
            referenceButton.setImage(Tex.stat(new Texture("dLibResources/images/ui/uieditor/reference.png")));
            referenceButton.onLeftClickEvent.subscribe(this, () -> {
                UCEditor editor = UIManager.getOpenElementOfType(UCEditor.class);
                if(editor == null){
                    return;
                }

                ReferencePickerMouseState state = new ReferencePickerMouseState(editor.itemTree.rootElement);
                state.onReferencePickedEvent.subscribe(this, element -> {
                    property.setValue(new UIElementPathBinding(element));
                });
                MouseStateManager.get().enterMouseState(state);
            });
            mainContentBox.addItem(referenceButton);

            bindingBox = new ImageTextBox(property.getValue().getDisplayValue(), Dim.fill(), Dim.fill());
            mainContentBox.addItem(bindingBox);
        }

        if(property.getValue() instanceof UIElementPathBinding){
            UIElementPathBinding binding = (UIElementPathBinding) property.getValue();
            binding.getObjectRelativePathRaw().onValueChangedEvent.subscribe(this, (s, s2) -> bindingBox.textBox.setText(s2));
        }

        ((UIElementPathBinding) property.getValue()).getObjectRelativePathRaw().onValueChangedEvent.subscribe(this, (uiElementBinding, uiElementBinding2) -> {
            bindingBox.textBox.setText(uiElementBinding2);
        });

        addChildNCS(mainContentBox);
    }
}
