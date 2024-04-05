package dLib.ui.elements.propertyeditors;

import com.badlogic.gdx.graphics.Color;
import dLib.ui.Alignment;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.prefabs.HorizontalBox;
import dLib.ui.elements.prefabs.TextBox;
import dLib.ui.elements.prefabs.VerticalBox;
import dLib.util.settings.Property;

public abstract class AbstractPropertyEditor<PropertyType extends Property<?>> extends UIElement {
    //region Variables

    protected PropertyType property;

    //endregion

    //region Constructors

    public AbstractPropertyEditor(PropertyType property, Integer xPos, Integer yPos, Integer width, Integer height){
        super(xPos, yPos, width, height);

        this.property = property;

        buildElement(property, xPos, yPos, width, height);
    }

    //endregion

    //region Methods

    protected void buildElement(PropertyType property, int xPos, int yPos, int width, int height){
        if(width < 500 && canDisplayMultiline()){
            buildMultiline(property, xPos, yPos, width, height);
        }
        else{
            setHeight((int) (height * 0.5f));
            buildSingleLine(property, xPos, yPos, width, height);
        }
    }

    private void buildMultiline(PropertyType property, int xPos, int yPos, int width, int height){
        VerticalBox vBox = new VerticalBox(xPos, yPos, width, height);
        vBox.addItem(buildTitle(property, width, (int)(height * 0.5f)));
        vBox.addItem(buildContent(property, width, (int)(height * 0.5f)));
        addChildCS(vBox);
    }

    private void buildSingleLine(PropertyType property, int xPos, int yPos, int width, int height){
        HorizontalBox hBox = new HorizontalBox(xPos, yPos, width, height);
        hBox.addItem(buildTitle(property, (int)(width * 0.8f), height));
        hBox.addItem(buildContent(property, (int)(width * 0.2f), height));
        addChildCS(hBox);
    }

    protected UIElement buildTitle(PropertyType property, int width, int height){
        return new TextBox(property.getName(), 0, 0, width, height).setHorizontalAlignment(Alignment.HorizontalAlignment.LEFT).setMarginPercX(0f).setMarginPercY(0.25f).setTextRenderColor(Color.WHITE);
    }

    protected abstract UIElement buildContent(PropertyType property, Integer width, Integer height);

    public boolean canDisplayMultiline(){
        return true;
    }

    //endregion
}
