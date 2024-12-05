package dLib.properties.ui.elements;

import com.badlogic.gdx.graphics.Color;
import dLib.ui.Alignment;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.implementations.Hoverable;
import dLib.ui.elements.prefabs.*;
import dLib.properties.objects.templates.TProperty;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.Pos;

import java.util.function.Consumer;

public abstract class AbstractPropertyEditor<PropertyType extends TProperty<?, ?>> extends UIElement {
    //region Variables

    protected PropertyType property;

    private ItemBox ui;

    protected int originalHeight;

    public Consumer<PropertyType> onPropertyHovered;
    public Consumer<PropertyType> onPropertyUnhovered;

    //endregion

    //region Constructors

    public AbstractPropertyEditor(PropertyType property, AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, AbstractDimension height){
        super(xPos, yPos, width, height);

        this.property = property;
        buildElement(property);
    }

    //endregion

    //region Methods

    protected void buildElement(PropertyType property){
        if(ui != null){
            removeChild(ui);
            ui = null;
        }

        if(false){ //TODO add as parameter to build multiline or not
            buildMultiline(property);
        }
        else{
            buildSingleLine(property);
        }
    }

    private void buildMultiline(PropertyType property){
        //TODO padding on left and right side
        VerticalBox vBox = new VerticalBox(Pos.perc(0), Pos.perc(0), Dim.fill(), Dim.fill(), true){
            @Override
            public UIElement wrapUIForItem(UIElement item) {
                Hoverable hoverable = new Hoverable(Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill()){
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

        vBox.addItem(buildTitle(property, Dim.fill(), Dim.perc(0.5)));
        vBox.addItem(buildContent(property, Dim.fill(), Dim.perc(0.5)));
        ui = vBox;
        addChildCS(vBox);
    }

    private void buildSingleLine(PropertyType property){
        //TODO padding on left and right side
        HorizontalBox hBox = new HorizontalBox(Pos.px(15), Pos.perc(0), Dim.fill(), Dim.fill(), true){
            @Override
            public UIElement wrapUIForItem(UIElement item) {
                Hoverable hoverable = new Hoverable(Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill()){
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

        hBox.addItem(buildTitle(property, Dim.perc(0.5), Dim.fill()));
        hBox.addItem(buildContent(property, Dim.perc(0.5), Dim.fill()));
        ui = hBox;
        addChildCS(hBox);
    }

    protected UIElement buildTitle(PropertyType property, AbstractDimension width, AbstractDimension height){
        return new TextBox(property.getName(), Pos.perc(0), Pos.perc(0), width, height).setTextRenderColor(Color.WHITE).setHorizontalAlignment(Alignment.HorizontalAlignment.LEFT);
    }

    protected abstract UIElement buildContent(PropertyType property, AbstractDimension width, AbstractDimension height);

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
