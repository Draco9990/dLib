package dLib.propertyeditors.ui.elements;

import com.badlogic.gdx.graphics.Color;
import dLib.ui.Alignment;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.prefabs.HorizontalBox;
import dLib.ui.elements.prefabs.ListBox;
import dLib.ui.elements.prefabs.TextBox;
import dLib.ui.elements.prefabs.VerticalBox;
import dLib.util.settings.Property;

public abstract class AbstractPropertyEditor<PropertyType extends Property<?>> extends UIElement {
    //region Variables

    protected PropertyType property;

    private ListBox ui;

    private int originalHeight;

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
        VerticalBox vBox = new VerticalBox(0, 0, width, height);
        vBox.addItem(buildTitle(property, width, (int)(height * 0.5f)));
        vBox.addItem(buildContent(property, width, (int)(height * 0.5f)));
        ui = vBox;
        addChildCS(vBox);
    }

    private void buildSingleLine(PropertyType property, int width, int height){
        HorizontalBox hBox = new HorizontalBox(0, 0, width, height);
        hBox.addItem(buildTitle(property, (int)(width * 0.8f), height));
        hBox.addItem(buildContent(property, (int)(width * 0.2f), height));
        ui = hBox;
        addChildCS(hBox);
    }

    protected UIElement buildTitle(PropertyType property, int width, int height){
        return new TextBox(property.getName(), 0, 0, width, height).setHorizontalAlignment(Alignment.HorizontalAlignment.LEFT).setMarginPercX(0f).setMarginPercY(0.25f).setTextRenderColor(Color.WHITE);
    }

    protected abstract UIElement buildContent(PropertyType property, Integer width, Integer height);

    public boolean canDisplayMultiline(){
        return true;
    }

    @Override
    protected void onRefreshElement() {
        super.onRefreshElement();

        buildElement(property, getWidthUnscaled(), originalHeight);
    }

    //endregion
}
