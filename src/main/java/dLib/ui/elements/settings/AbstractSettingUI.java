package dLib.ui.elements.settings;

import dLib.ui.HorizontalAlignment;
import dLib.ui.VerticalAlignment;
import dLib.ui.elements.CompositeUIElement;
import dLib.ui.elements.prefabs.TextBox;
import dLib.util.settings.Setting;

public abstract class AbstractSettingUI extends CompositeUIElement {
    /** Variables */
    private TextBox label;

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

        textPosY = yPos;
        valuePosY = yPos;

        textHeight = height;
        valueHeight = height;

        if(width < 500){
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

        this.label = new TextBox(setting.getTitle(), xPos, textPosY, (int)(width * textPercX), textHeight).setHorizontalAlignment(HorizontalAlignment.LEFT).setMarginPercX(0.01f).setMarginPercY(0.01f);
        this.other.add(label);
    }

    /* AbstractUISetting markAsSubsetting(){
        other.add(new Renderable(UIThemeManager.getDefaultTheme().arrow_right, xPos, yPos, height, height));

        label.offset(height, 0);
        label.setWidth(label.getWidth() - height);
        return this;
    }*/
}
