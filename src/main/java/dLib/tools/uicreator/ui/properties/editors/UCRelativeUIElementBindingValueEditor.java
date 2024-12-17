package dLib.tools.uicreator.ui.properties.editors;

import com.badlogic.gdx.graphics.Texture;
import dLib.mousestates.MouseStateManager;
import dLib.tools.uicreator.UCEditor;
import dLib.tools.uicreator.ui.properties.objects.UCUIElementBindingProperty;
import dLib.ui.bindings.RelativeUIElementBinding;
import dLib.ui.elements.prefabs.Button;
import dLib.ui.elements.prefabs.HorizontalBox;
import dLib.ui.elements.prefabs.TextBox;
import dLib.ui.mousestates.ReferencePickerMouseState;
import dLib.ui.resources.UICommonResources;
import dLib.ui.screens.UIManager;
import dLib.util.bindings.texture.Tex;
import dLib.util.ui.dimensions.Dim;

public class UCRelativeUIElementBindingValueEditor extends UCUIElementBindingValueEditor<RelativeUIElementBinding> {
    private TextBox bindingBox;

    public UCRelativeUIElementBindingValueEditor(RelativeUIElementBinding value) {
        this(new UCUIElementBindingProperty(value));
    }

    public UCRelativeUIElementBindingValueEditor(UCUIElementBindingProperty property) {
        super(property);

        HorizontalBox mainContentBox = new HorizontalBox(Dim.fill(), Dim.px(50));
        {
            Button referenceButton = new Button(Dim.height(), Dim.fill());
            referenceButton.setImage(Tex.stat(new Texture("dLibResources/images/ui/uieditor/reference.png")));
            referenceButton.onLeftClickEvent.subscribe(this, () -> {
                UCEditor editor = UIManager.getOpenElementOfType(UCEditor.class);
                if(editor == null){
                    return;
                }

                ReferencePickerMouseState state = new ReferencePickerMouseState(editor.itemTree.rootElement, element -> {
                    ((RelativeUIElementBinding)property.getValue()).getObjectRelativePathRaw().setValue(element.getRelativePath());
                });

                MouseStateManager.get().enterMouseState(state);
            });
            mainContentBox.addItem(referenceButton);

            bindingBox = new TextBox(property.getValue().getDisplayValue(), Dim.fill(), Dim.fill());
            bindingBox.setImage(Tex.stat(UICommonResources.inputfield));
            mainContentBox.addItem(bindingBox);
        }

        if(property.getValue() instanceof RelativeUIElementBinding){
            RelativeUIElementBinding binding = (RelativeUIElementBinding) property.getValue();
            binding.getObjectRelativePathRaw().onValueChangedEvent.subscribe(this, (s, s2) -> bindingBox.setText(s2));
        }

        ((RelativeUIElementBinding) property.getValue()).getObjectRelativePathRaw().onValueChangedEvent.subscribe(this, (uiElementBinding, uiElementBinding2) -> {
            bindingBox.setText(uiElementBinding2);
        });

        addChildNCS(mainContentBox);
    }
}
