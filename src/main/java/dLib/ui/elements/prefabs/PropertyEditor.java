package dLib.ui.elements.prefabs;

import dLib.properties.objects.templates.TProperty;
import dLib.ui.Alignment;
import dLib.ui.elements.UIElement;
import dLib.ui.resources.UICommonResources;
import dLib.ui.util.ESelectionMode;
import dLib.util.bindings.texture.Tex;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.padding.Padd;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.Pos;
import java.util.function.BiConsumer;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.UUID;

public class PropertyEditor extends UIElement {
    public ImageTextBox descriptionBox;

    private ArrayList<TProperty<?, ?>> properties = new ArrayList<>();
    private VerticalBox propertyList;

    private LinkedHashMap<String, PropertyGroup> categories = new LinkedHashMap<>();

    private boolean tryMultiline = false;

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

            descriptionBox = new ImageTextBox("", Pos.px(0), Pos.px(0), Dim.fill(), Dim.perc(0.2));
            descriptionBox.textBox.setWrap(true);
            descriptionBox.textBox.setFontScale(0.8f);
            descriptionBox.textBox.setHorizontalContentAlignment(Alignment.HorizontalAlignment.LEFT);
            descriptionBox.textBox.setVerticalContentAlignment(Alignment.VerticalAlignment.TOP);
            elementList.addItem(descriptionBox);
        }
        addChildNCS(elementList);
    }

    public void setTryMultiline(boolean tryMultiline){
        this.tryMultiline = tryMultiline;
    }

    public boolean shouldBuildMultiline(){
        return tryMultiline;
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
            }
            categories.get(category).propertyList.updateItems(propertiesByCategory.get(category));
        }

        propertyList.updateItems(new ArrayList<>(categories.values()));
    }

    protected PropertyGroup makePropertyGroup(String category){
        return new PropertyGroup(category, shouldBuildMultiline());
    }

    //region Child Elements

    public static class PropertyGroup extends VerticalCollapsableBox{
        protected VerticalListBox<TProperty<?, ?>> propertyList;

        private UUID valueChangedEventId;

        private boolean multiline;

        public PropertyGroup(String categoryName, boolean multiline) {
            super(categoryName, Pos.px(0), Pos.px(0), Dim.fill(), Dim.auto());

            this.multiline = multiline;

            propertyList = new VerticalListBox<TProperty<?, ?>>(Pos.px(0), Pos.px(0), Dim.fill(), Dim.auto()){
                @Override
                public UIElement makeUIForItem(TProperty<?, ?> item) {
                    //TODO getParentOfType(PropertyEditor.class).shouldBuildMultiline()
                    UIElement editor = item.makeEditorFor(getParentOfType(PropertyGroup.class).multiline);
                    if(editor == null) {
                        return new Spacer(Dim.fill(), Dim.px(1)); //TODO breakpoint to verify none fire before removing
                    }

                    editor.onHoveredEvent.subscribe(PropertyGroup.this, () -> {
                        if(getDescriptionBox() != null){
                            getDescriptionBox().setText(item.getDescription());
                        }
                    });
                    editor.onHoveredChildEvent.subscribe(PropertyGroup.this, (child) -> {
                        if(getDescriptionBox() != null){
                            getDescriptionBox().setText(item.getDescription());
                        }
                    });

                    editor.onUnhoveredEvent.subscribe(PropertyGroup.this, () -> {
                        if(getDescriptionBox() != null){
                            getDescriptionBox().setText("");
                        }
                    });
                    editor.onUnhoveredChildEvent.subscribe(PropertyGroup.this, (child) -> {
                        if(getDescriptionBox() != null){
                            getDescriptionBox().setText("");
                        }
                    });

                    return editor;
                }
            };
            propertyList.setItemSpacing(10);
            propertyList.setSelectionMode(ESelectionMode.NONE);
            propertyList.disableItemWrapping();

            BiConsumer updateProperties = (__, ___) -> delayedActions.add(() -> (getParentOfType(PropertyEditor.class)).loadProperties());

            propertyList.addOnPropertyAddedConsumer(property -> valueChangedEventId = property.onValueChangedEvent.subscribeManaged(updateProperties));
            propertyList.addOnPropertyRemovedConsumer(property -> property.onValueChangedEvent.unsubscribeManaged(valueChangedEventId));
            addItem(propertyList);
        }

        public TextBox getDescriptionBox() {
            return (getParentOfType(PropertyEditor.class)).descriptionBox.textBox;
        }
    }

    //endregion
}
