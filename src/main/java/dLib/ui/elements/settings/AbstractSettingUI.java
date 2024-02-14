package dLib.ui.elements.settings;

import com.badlogic.gdx.graphics.Color;
import dLib.ui.Alignment;
import dLib.ui.elements.CompositeUIElement;
import dLib.ui.elements.prefabs.TextBox;
import dLib.util.settings.Setting;

public abstract class AbstractSettingUI extends CompositeUIElement {
    /** Variables */
    protected TextBox label;

    protected float textPercX = 0.25f;
    protected float valuePercX = 1 - textPercX;
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

        textPosY = yPos;
        valuePosY = yPos;

        textHeight = height;
        valueHeight = height;

        if(width < 500 && canDisplayMultiline()){
            textPercX = 1;
            valuePercX = 1;

            float textHeaderPerc = 0.5f;
            textPosY = (int) (yPos + height * (1-textHeaderPerc));
            textHeight = (int) (height * textHeaderPerc);
            valueHeight = (int) (height * (1-textHeaderPerc));
        }

        this.setting = setting;

        this.xPos = xPos;
        this.yPos = yPos;

        this.label = new TextBox(setting.getTitle(), xPos, textPosY, (int)(width * textPercX), textHeight).setHorizontalAlignment(Alignment.HorizontalAlignment.LEFT).setVerticalAlignment(Alignment.VerticalAlignment.BOTTOM).setMarginPercX(0f).setMarginPercY(0.25f).setTextRenderColor(Color.WHITE);
        this.foreground.add(label);
    }

    public boolean canDisplayMultiline(){
        return true;
    }
}
