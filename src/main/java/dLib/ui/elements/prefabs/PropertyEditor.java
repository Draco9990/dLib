package dLib.ui.elements.prefabs;

import dLib.properties.objects.Property;
import dLib.ui.Alignment;
import dLib.ui.elements.UIElement;
import dLib.ui.themes.UIThemeManager;
import dLib.ui.util.ESelectionMode;

public class PropertyEditor extends UIElement {
    public TextBox descriptionBox;

    public VerticalListBox<Property<?>> propertyList;

    public PropertyEditor(int xPos, int yPos, int width, int height) {
        super(xPos, yPos, width, height);

        propertyList = new VerticalListBox<Property<?>>(0, (int) (height * 0.2f), width, (int) (height * 0.8f)){
            @Override
            public UIElement makeUIForItem(Property<?> item) {
                return item.makePropertyEditor(0, 0, itemBox.getWidth(), 50)
                        .setOnPropertyHoveredConsumer(property -> {
                            if(descriptionBox != null){
                                descriptionBox.setText(((Property<?>)property).getDescription());
                            }
                        })
                        .setOnPropertyUnhoveredConsumer(property -> {
                            if(descriptionBox != null){
                                descriptionBox.setText("");
                            }
                        });
            }
        };
        propertyList.setItemSpacing(10);
        propertyList.setSelectionMode(ESelectionMode.NONE);
        propertyList.disableItemWrapping();
        addChildNCS(propertyList);

        descriptionBox = new TextBox("", 0, 0, width, (int) (height * 0.2f));
        descriptionBox.setWrap(true);
        descriptionBox.setImage(UIThemeManager.getDefaultTheme().inputfield);
        descriptionBox.setMaxFontScale(0.8f);
        descriptionBox.setMarginPercY(0.1f);
        descriptionBox.setPadding(20, 0, 0, 0);
        descriptionBox.setHorizontalAlignment(Alignment.HorizontalAlignment.LEFT);
        descriptionBox.setVerticalAlignment(Alignment.VerticalAlignment.TOP);
        addChildNCS(descriptionBox);
    }
}
