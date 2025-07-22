package dLib.tools.uicreator.ui.properties.editors;

import com.badlogic.gdx.graphics.Texture;
import dLib.mousestates.MouseStateManager;
import dLib.properties.objects.Property;
import dLib.tools.uicreator.UCEditor;
import dLib.tools.uicreator.ui.components.data.UCEditorDataComponent;
import dLib.ui.bindings.AbstractUIElementBinding;
import dLib.ui.bindings.UIElementRelativePathBinding;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.items.buttons.Button;
import dLib.ui.elements.items.itembox.HorizontalBox;
import dLib.ui.elements.items.text.ImageTextBox;
import dLib.ui.mousestates.ReferencePickerMouseState;
import dLib.ui.UIManager;
import dLib.util.bindings.texture.Tex;
import dLib.util.ui.dimensions.Dim;

public class UCRelativeUIElementBindingValueEditor extends UCUIElementBindingValueEditor<UIElementRelativePathBinding> {
    private ImageTextBox bindingBox;

    public UCRelativeUIElementBindingValueEditor(UIElementRelativePathBinding value) {
        this(new Property<>(value));
    }

    public UCRelativeUIElementBindingValueEditor(Property<AbstractUIElementBinding> property) {
        super(property);

        HorizontalBox mainContentBox = new HorizontalBox(Dim.fill(), Dim.px(50));
        {
            Button referenceButton = new Button(Dim.mirror(), Dim.fill());
            referenceButton.setTexture(Tex.stat(new Texture("dLibResources/images/ui/uieditor/reference.png")));
            referenceButton.postLeftClickEvent.subscribe(this, () -> {
                UCEditor editor = UIManager.getOpenElementOfType(UCEditor.class);
                if(editor == null){
                    return;
                }

                ReferencePickerMouseState state = new ReferencePickerMouseState(editor.itemTree.rootElementData.getComponent(UCEditorDataComponent.class).liveElement);
                state.onReferencePickedEvent.subscribe(this, element -> {
                    UIElement liveOwner = ((UIElementData) property.getOwningContainer()).getComponent(UCEditorDataComponent.class).liveElement;
                    property.setValue(new UIElementRelativePathBinding(liveOwner, element));
                });
                MouseStateManager.get().enterMouseState(state);
            });
            mainContentBox.addChild(referenceButton);

            bindingBox = new ImageTextBox(property.getValue().toString(), Dim.fill(), Dim.fill());
            mainContentBox.addChild(bindingBox);
        }

        if(property.getValue() instanceof UIElementRelativePathBinding){
            UIElementRelativePathBinding binding = (UIElementRelativePathBinding) property.getValue();
            binding.getObjectRelativePathRaw().onValueChangedEvent.subscribe(this, (s, s2) -> bindingBox.textBox.setText(s2));
        }

        ((UIElementRelativePathBinding) property.getValue()).getObjectRelativePathRaw().onValueChangedEvent.subscribe(this, (uiElementBinding, uiElementBinding2) -> {
            bindingBox.textBox.setText(uiElementBinding2);
        });

        addChild(mainContentBox);
    }
}
