package dLib.properties.ui.elements;

import dLib.properties.objects.templates.TProperty;
import dLib.properties.objects.templates.TPropertyArray;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.items.itembox.VerticalBox;
import dLib.ui.elements.items.itembox.VerticalDataBox;
import dLib.ui.elements.items.text.TextButton;
import dLib.ui.resources.UICommonResources;
import dLib.util.bindings.texture.Tex;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;

public class PropertyArrayValueEditor<EditingPropertyType, PropertyType extends TPropertyArray<EditingPropertyType, PropertyType>> extends PropertyValueEditor<PropertyType> {
    //region Variables

    //endregion

    //region Constructors

    public PropertyArrayValueEditor(TProperty<?, ?> property) {
        this(property, false);
    }

    public PropertyArrayValueEditor(TProperty<?, ?> property, boolean multiline) {
        super(property, multiline);
    }

    //endregion

    //region Methods

    @Override
    protected void buildValueContent(AbstractDimension width){
        VerticalBox builtContent = new VerticalBox(width, Dim.auto());
        {
            VerticalDataBox<TProperty<EditingPropertyType, ?>> valueBox = new VerticalDataBox<TProperty<EditingPropertyType, ?>>(Dim.fill(), Dim.auto()){
                @Override
                public UIElement makeUIForItem(TProperty<EditingPropertyType, ?> item) {
                    return item.makeEditorFor();
                }
            };
            valueBox.setCanDelete(boundProperty.canDelete());
            valueBox.disableToggleOverlay();
            valueBox.setChildren(boundProperty.getValue());
            valueBox.onItemDeletedEvent.subscribe(valueBox, (item) -> boundProperty.remove(item));
            //TODO disable child interactions once that's in to improve perf
            builtContent.addChild(valueBox);

            TextButton addButton = new TextButton("Add", Dim.fill(), Dim.px(50)){
                @Override
                public boolean isActive() {
                    return super.isActive() && !boundProperty.isFull();
                }
            };
            addButton.setTexture(Tex.stat(UICommonResources.button03_square));
            addButton.onLeftClickEvent.subscribe(addButton, () -> boundProperty.addBlank());
            builtContent.addChild(addButton);

            boundProperty.onSingleValueChangedEvent.subscribe(valueBox, (editingPropertyType, editingPropertyType2, integer) -> {
                valueBox.setCanDelete(boundProperty.canDelete());
                valueBox.setChildren(boundProperty.getValue());
            });
        }

        if(contentEditor != null){
            contentEditor.getParent().replaceChild(contentEditor, builtContent);
        }

        contentEditor = builtContent;
    }

    //endregion
}
