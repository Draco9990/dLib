package dLib.util.bindings.texture.editors;

import dLib.properties.objects.TextureBindingProperty;
import dLib.ui.elements.prefabs.HorizontalBox;
import dLib.ui.elements.prefabs.ImageTextBox;
import dLib.ui.elements.prefabs.TextBox;
import dLib.ui.resources.UICommonResources;
import dLib.util.bindings.texture.Tex;
import dLib.util.bindings.texture.TextureNoneBinding;
import dLib.util.ui.dimensions.Dim;

public class TextureNoneBindingValueEditor extends TextureBindingValueEditor<TextureNoneBinding> {

    public TextureNoneBindingValueEditor(TextureNoneBinding binding) {
        this(new TextureBindingProperty(binding));
    }

    public TextureNoneBindingValueEditor(TextureBindingProperty property) {
        super(property);

        HorizontalBox contentBox = new HorizontalBox(Dim.fill(), Dim.px(50));
        {
            ImageTextBox valueBox = new ImageTextBox("NONE", Dim.fill(), Dim.fill());
            valueBox.setImage(Tex.stat(UICommonResources.button02_horizontal));
            contentBox.addItem(valueBox);
            contentBox.addItem(makeSwapComboBox());
        }
        addChildNCS(contentBox);
    }
}
