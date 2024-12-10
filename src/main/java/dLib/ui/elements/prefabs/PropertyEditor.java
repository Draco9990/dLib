package dLib.ui.elements.prefabs;

import dLib.properties.objects.templates.TProperty;
import dLib.ui.Alignment;
import dLib.ui.elements.UIElement;
import dLib.ui.themes.UIThemeManager;
import dLib.ui.util.ESelectionMode;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.padding.Padd;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.Pos;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.function.BiConsumer;

public class PropertyEditor extends UIElement {
    public TextBox descriptionBox;

    private ArrayList<TProperty<?, ?>> properties = new ArrayList<>();
    private VerticalBox propertyList;

    private LinkedHashMap<String, PropertyGroup> categories = new LinkedHashMap<>();

    public PropertyEditor(AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, AbstractDimension height) {
        super(xPos, yPos, width, height);

        VerticalBox elementList = new VerticalBox(Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill());
        {
            Scrollbox propertyListScrollbox = new Scrollbox(Pos.px(0), Pos.px(0), Dim.fill(), Dim.perc(0.8));
            {
                propertyListScrollbox.addChildNCS(propertyList = new VerticalBox(Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill()));
                propertyList.setItemSpacing(20);
            }
            propertyListScrollbox.setIsHorizontal(false);
            elementList.addItem(propertyListScrollbox);

            descriptionBox = new TextBox("", Pos.px(0), Pos.px(0), Dim.fill(), Dim.perc(0.2));
            descriptionBox.setWrap(true);
            descriptionBox.setImage(UIThemeManager.getDefaultTheme().inputfield);
            descriptionBox.setMaxFontScale(0.8f);
            descriptionBox.setMarginPercY(0.1f);
            descriptionBox.setPadding(Padd.px(10), Padd.px(0));
            descriptionBox.setHorizontalAlignment(Alignment.HorizontalAlignment.LEFT);
            descriptionBox.setVerticalAlignment(Alignment.VerticalAlignment.TOP);
            elementList.addItem(descriptionBox);
        }
        addChildNCS(elementList);
    }

    public void clearProperties(){
        properties.clear();
        categories.clear();
        loadProperties();
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
                categories.put(category, makePropertyGroup(category));
                addChildNCS(categories.get(category));
            }
            categories.get(category).propertyList.updateItems(propertiesByCategory.get(category));
        }

        propertyList.updateItems(new ArrayList<>(categories.values()));
    }

    protected PropertyGroup makePropertyGroup(String category){
        return new PropertyGroup(category);
    }

    //region Child Elements

    public static class PropertyGroup extends VerticalCollapsableBox{
        protected VerticalListBox<TProperty<?, ?>> propertyList;

        public PropertyGroup(String categoryName) {
            super(categoryName, Pos.px(0), Pos.px(0), Dim.fill(), Dim.auto());

            propertyList = new VerticalListBox<TProperty<?, ?>>(Pos.px(0), Pos.px(0), Dim.fill(), Dim.auto()){
                @Override
                public UIElement makeUIForItem(TProperty<?, ?> item) {
                    return item.makePropertyEditor(Pos.px(0), Pos.px(0), Dim.fill(), getParentOfType(PropertyEditor.class).shouldBuildMultiline())
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
            addItem(propertyList);
        }

        public TextBox getDescriptionBox() {
            return (getParentOfType(PropertyEditor.class)).descriptionBox;
        }
    }

    public boolean shouldBuildMultiline(){
        return false;
    }

    //endregion
}
