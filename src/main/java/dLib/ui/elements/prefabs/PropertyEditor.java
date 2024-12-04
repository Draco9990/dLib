package dLib.ui.elements.prefabs;

import dLib.properties.objects.templates.TProperty;
import dLib.ui.Alignment;
import dLib.ui.elements.UIElement;
import dLib.ui.themes.UIThemeManager;
import dLib.ui.util.ESelectionMode;

import java.util.ArrayList;
import java.util.function.BiConsumer;

public class PropertyEditor extends UIElement {
    public TextBox descriptionBox;

    public ArrayList<TProperty<?, ?>> properties = new ArrayList<>();
    public VerticalListBox<TProperty<?, ?>> propertyList;

    public PropertyEditor(int xPos, int yPos, int width, int height) {
        super(xPos, yPos, width, height);

        propertyList = new VerticalListBox<TProperty<?, ?>>(0, (int) (height * 0.2f), width, (int) (height * 0.8f)){
            @Override
            public UIElement makeUIForItem(TProperty<?, ?> item) {
                return item.makePropertyEditor(0, 0, itemBox.getWidth(), 50)
                        .setOnPropertyHoveredConsumer(property -> {
                            if(descriptionBox != null){
                                descriptionBox.setText(((TProperty<?, ?>)property).getDescription());
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

        BiConsumer updateProperties = (__, ___) -> delayedActions.add(this::loadProperties);

        propertyList.addOnPropertyAddedConsumer(property -> property.addOnValueChangedListener(updateProperties));
        propertyList.addOnPropertyRemovedConsumer(property -> property.removeOnValueChangedListener(updateProperties));

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

    public PropertyEditor setProperties(ArrayList<TProperty<?, ?>> properties){
        this.properties = properties;
        loadProperties();
        return this;
    }

    private void loadProperties(){
        ArrayList<TProperty<?, ?>> propertiesToAdd = new ArrayList<>();

        for (TProperty<?, ?> property : properties) {
            if(property.isVisible()){
                propertiesToAdd.add(property);
            }
        }

        propertyList.updateItems(propertiesToAdd);
    }
}
