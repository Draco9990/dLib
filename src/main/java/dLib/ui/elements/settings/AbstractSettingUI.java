package dLib.ui.elements.settings;

import com.badlogic.gdx.graphics.Color;
import dLib.ui.Alignment;
import dLib.ui.elements.CompositeUIElement;
import dLib.ui.elements.prefabs.TextBox;
import dLib.util.settings.Setting;

public abstract class AbstractSettingUI extends CompositeUIElement {
    /** Variables */
    protected TextBox label;

    protected float valuePercX = 1 - getTextPercX();
    protected float arrowPercX = 0.025f;

    protected Integer textPosY;
    protected Integer textHeight;
    protected Integer valuePosY;
    protected Integer valueHeight;

    protected Setting<?> setting;

    protected Integer xPos;
    protected Integer yPos;

    public AbstractSettingUI(Setting<?> setting, Integer xPos, Integer yPos, Integer width, Integer height){
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

        this.setting = setting;

        this.xPos = xPos;
        this.yPos = yPos;

        this.label = new TextBox(setting.getTitle(), 0, textPosY, (int)(width * getTextPercX()), textHeight).setHorizontalAlignment(Alignment.HorizontalAlignment.LEFT).setVerticalAlignment(Alignment.VerticalAlignment.BOTTOM).setMarginPercX(0f).setMarginPercY(0.25f).setTextRenderColor(Color.WHITE);
        this.foreground.add(label);
    }

    public boolean canDisplayMultiline(){
        return true;
    }

    protected float getTextPercX(){
        return (width < 500 && canDisplayMultiline() ? 1 : 0.25f);
    }
}
