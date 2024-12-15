package dLib.tools.uicreator.ui.properties.editors;

import com.badlogic.gdx.graphics.Texture;
import dLib.mousestates.MouseStateManager;
import dLib.properties.ui.elements.AbstractPropertyEditor;
import dLib.tools.uicreator.UCEditor;
import dLib.ui.bindings.RelativeUIElementBinding;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.prefabs.Button;
import dLib.ui.elements.prefabs.HorizontalBox;
import dLib.ui.elements.prefabs.TextBox;
import dLib.tools.uicreator.ui.properties.objects.UCUIElementBindingProperty;
import dLib.ui.mousestates.ReferencePickerMouseState;
import dLib.ui.screens.UIManager;
import dLib.ui.themes.UIThemeManager;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.AbstractPosition;

import java.util.function.Consumer;

public class UCUIElementBindingPropertyEditor extends AbstractPropertyEditor<UCUIElementBindingProperty> {
    private TextBox bindingBox;

    public UCUIElementBindingPropertyEditor(UCUIElementBindingProperty property, AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, boolean multiline) {
        super(property, xPos, yPos, width, multiline);

        if(UIManager.getOpenElementOfType(UCEditor.class) == null){
            throw new UnsupportedOperationException("UCUIBindingPropertyEditor only works within UI element creation editors");
        }
    }

    @Override
    protected UIElement buildContent(UCUIElementBindingProperty property, AbstractDimension width, AbstractDimension height) {
        HorizontalBox mainBox = new HorizontalBox(width, height);
        {
            Button referenceButton = new Button(Dim.height(), Dim.fill());
            referenceButton.setImage(new Texture("dLibResources/images/ui/uieditor/reference.png"));
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
            mainBox.addItem(referenceButton);

            bindingBox = new TextBox(property.getValue().getDisplayValue(), Dim.fill(), Dim.fill());
            bindingBox.setImage(UIThemeManager.getDefaultTheme().inputfield);
            mainBox.addItem(bindingBox);
        }

        if(property.getValue() instanceof RelativeUIElementBinding){
            RelativeUIElementBinding binding = (RelativeUIElementBinding) property.getValue();
            binding.getObjectRelativePathRaw().onValueChangedEvent.subscribe(this, (s, s2) -> bindingBox.setText(s2));
        }

        return mainBox;
    }
}
