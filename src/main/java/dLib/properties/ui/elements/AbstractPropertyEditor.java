package dLib.properties.ui.elements;

import com.badlogic.gdx.graphics.Color;
import dLib.ui.Alignment;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.implementations.Hoverable;
import dLib.ui.elements.prefabs.*;
import dLib.properties.objects.Property;

import java.util.function.Consumer;

public abstract class AbstractPropertyEditor<PropertyType extends Property<?>> extends UIElement {
    //region Variables

    protected PropertyType property;

    private ItemBox ui;

    protected int originalHeight;

    public Consumer<PropertyType> onPropertyHovered;
    public Consumer<PropertyType> onPropertyUnhovered;

    //endregion

    //region Constructors

    public AbstractPropertyEditor(PropertyType property, Integer xPos, Integer yPos, Integer width, Integer height){
        super(xPos, yPos, width, height);

        this.property = property;
        this.originalHeight = height;

        buildElement(property, width, originalHeight);
    }

    //endregion

    //region Methods

    protected void buildElement(PropertyType property, int width, int height){
        if(ui != null){
            removeChild(ui);
            ui = null;
        }

        if(width < 500 && canDisplayMultiline()){
            setHeight((int) (height * 2f));
            buildMultiline(property, width, height);
        }
        else{
            buildSingleLine(property, width, height);
        }
    }

    private void buildMultiline(PropertyType property, int width, int height){
        width -= 35;
        VerticalBox vBox = new VerticalBox(15, 0, width, height, true){
            @Override
            public UIElement wrapUIForItem(UIElement item) {
                Hoverable hoverable = new Hoverable(0, 0, item.getWidthUnscaled(), item.getHeightUnscaled()){
                    @Override
                    public void onHovered() {
                        onPropertyHovered(property);
                    }

                    @Override
                    public void onUnhovered() {
                        onPropertyUnhovered(property);
                    }
                };
                hoverable.setClickthrough(true);
                item.addChildNCS(hoverable);

                return item;
            }
        };

        vBox.addItem(buildTitle(property, width, (int)(height * 0.5f)));
        vBox.addItem(buildContent(property, width, (int)(height * 0.5f)));
        ui = vBox;
        addChildCS(vBox);
    }

    private void buildSingleLine(PropertyType property, int width, int height){
        width -= 35;
        HorizontalBox hBox = new HorizontalBox(15, 0, width, height, true){
            @Override
            public UIElement wrapUIForItem(UIElement item) {
                Hoverable hoverable = new Hoverable(0, 0, item.getWidthUnscaled(), item.getHeightUnscaled()){
                    @Override
                    public void onHovered() {
                        onPropertyHovered(property);
                    }

                    @Override
                    public void onUnhovered() {
                        onPropertyUnhovered(property);
                    }
                };
                hoverable.setClickthrough(true);
                item.addChildNCS(hoverable);

                return item;
            }
        };

        hBox.addItem(buildTitle(property, (int)(width * 0.8f), height));
        hBox.addItem(buildContent(property, (int)(width * 0.2f), height));
        ui = hBox;
        addChildCS(hBox);
    }

    protected UIElement buildTitle(PropertyType property, int width, int height){
        return new TextBox(property.getName(), 0, 0, width, height).setHorizontalAlignment(Alignment.HorizontalAlignment.LEFT).setTextRenderColor(Color.WHITE);
    }

    protected abstract UIElement buildContent(PropertyType property, Integer width, Integer height);

    public boolean canDisplayMultiline(){
        return true;
    }

    private void onPropertyHovered(PropertyType property){
        if(onPropertyHovered != null){
            onPropertyHovered.accept(property);
        }
    }

    private void onPropertyUnhovered(PropertyType property){
        if(onPropertyUnhovered != null){
            onPropertyUnhovered.accept(property);
        }
    }

    public AbstractPropertyEditor<PropertyType> setOnPropertyHoveredConsumer(Consumer<PropertyType> onPropertyHovered){
        this.onPropertyHovered = onPropertyHovered;
        return this;
    }

    public AbstractPropertyEditor<PropertyType> setOnPropertyUnhoveredConsumer(Consumer<PropertyType> onPropertyUnhovered){
        this.onPropertyUnhovered = onPropertyUnhovered;
        return this;
    }

    //endregion
}
