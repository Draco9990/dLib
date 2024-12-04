package dLib.ui.elements.prefabs;

import dLib.properties.objects.Property;
import dLib.properties.objects.templates.TProperty;
import dLib.ui.Alignment;
import dLib.ui.elements.UIElement;
import dLib.ui.themes.UIThemeManager;
import dLib.ui.util.ESelectionMode;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.function.BiConsumer;

public class PropertyEditor extends UIElement {
    public TextBox descriptionBox;

    private ArrayList<TProperty<?, ?>> properties = new ArrayList<>();
    private VerticalBox propertyList;

    private LinkedHashMap<String, PropertyEditorBoxCategory> categories = new LinkedHashMap<>();

    public PropertyEditor(int xPos, int yPos, int width, int height) {
        super(xPos, yPos, width, height);

        propertyList = new VerticalBox(0, (int) (height * 0.2f), width, (int) (height * 0.8f));
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
        LinkedHashMap<String, ArrayList<TProperty<?, ?>>> propertiesByCategory = new LinkedHashMap<>();
        for (TProperty<?, ?> property : properties) {
            if(property.isVisible()){
                String category = property.getCategory();
                if(!propertiesByCategory.containsKey(category)){
                    propertiesByCategory.put(category, new ArrayList<>());
                }
                propertiesByCategory.get(category).add(property);
            }
        }

        for(String category : propertiesByCategory.keySet()){
            if(!categories.containsKey(category)){
                categories.put(category, new PropertyEditorBoxCategory(category, propertyList.getItemBox().getWidth()));
                addChildNCS(categories.get(category));
            }
            categories.get(category).setProperties(propertiesByCategory.get(category));
        }

        propertyList.updateItems(new ArrayList<>(categories.values()));
    }

    //region Child Elements

    public static class PropertyEditorBoxCategory extends UIElement{
        private TextBox titleBox;
        private VerticalListBox<TProperty<?, ?>> propertyList;

        public PropertyEditorBoxCategory(String categoryName, int width) {
            super(0, 0, width, 100);

            titleBox = new TextBox(categoryName, 0, 100-30, width, 30);
            addChildNCS(titleBox);

            propertyList = new VerticalListBox<TProperty<?, ?>>(0, 0, width, 70, true){
                @Override
                public UIElement makeUIForItem(TProperty<?, ?> item) {
                    return item.makePropertyEditor(0, 0, itemBox.getWidth(), 50)
                            .setOnPropertyHoveredConsumer(property -> {
                                if(getDescriptionBox() != null){
                                    getDescriptionBox().setText(((TProperty<?, ?>)property).getDescription());
                                }
                            })
                            .setOnPropertyUnhoveredConsumer(property -> {
                                if(getDescriptionBox() != null){
                                    getDescriptionBox().setText("");
                                }
                            });
                }
            };
            propertyList.setItemSpacing(10);
            propertyList.setSelectionMode(ESelectionMode.NONE);
            propertyList.disableItemWrapping();

            BiConsumer updateProperties = (__, ___) -> delayedActions.add(() -> (getParentOfType(PropertyEditor.class)).loadProperties());

            propertyList.addOnPropertyAddedConsumer(property -> property.addOnValueChangedListener(updateProperties));
            propertyList.addOnPropertyRemovedConsumer(property -> property.removeOnValueChangedListener(updateProperties));

            addChildNCS(propertyList);
        }

        public TextBox getDescriptionBox() {
            return (getParentOfType(PropertyEditor.class)).descriptionBox;
        }

        public void setProperties(ArrayList<TProperty<?, ?>> properties){
            int height = properties.size() * 50 + 30;

            setHeight(height);

            titleBox.setLocalPositionY(height-30);
            propertyList.setHeight(height-30);

            propertyList.updateItems(properties);
        }
    }

    //endregion
}
