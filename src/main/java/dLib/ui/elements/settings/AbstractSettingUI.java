package dLib.ui.elements.settings;

import com.badlogic.gdx.graphics.Color;
import dLib.ui.Alignment;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.prefabs.TextBox;
import dLib.util.settings.Property;

public abstract class AbstractSettingUI extends UIElement {
    //region Variables

    protected TextBox label;

    protected float valuePercX = 1 - getTextWidthPerc();
    protected float arrowPercX = 0.025f;

    protected Integer textPosY;
    protected Integer textHeight;
    protected Integer valuePosY;
    protected Integer valueHeight;

    protected Property<?> property;

    //endregion

    //region Constructors

    public AbstractSettingUI(Property<?> property, Integer xPos, Integer yPos, Integer width, Integer height){
        super(xPos, yPos, width, height);

        if(width < 500 && !canDisplayMultiline()){
            height = (int)(height * 0.5f);
            setHeight(height);
        }

        textPosY = 0;
        valuePosY = 0;

        textHeight = height;
        valueHeight = height;

        if(width < 500 && canDisplayMultiline()){
            valuePercX = 1;

            float textHeaderPerc = 0.5f;
            textPosY = (int) (height * (1-textHeaderPerc));
            textHeight = (int) (height * textHeaderPerc);
            valueHeight = (int) (height * (1-textHeaderPerc));
        }

        this.property = property;

        this.label = new TextBox(property.getName(), 0, textPosY, (int)(width * getTextWidthPerc()), textHeight).setHorizontalAlignment(Alignment.HorizontalAlignment.LEFT).setVerticalAlignment(Alignment.VerticalAlignment.BOTTOM).setMarginPercX(0f).setMarginPercY(0.25f).setTextRenderColor(Color.WHITE);
        addChildNCS(this.label);
    }

    //endregion

    //region Methods

    public boolean canDisplayMultiline(){
        return true;
    }

    protected float getTextWidthPerc(){
        return (width < 500 && canDisplayMultiline() ? 1 : 0.25f);
    }

    //endregion
}
