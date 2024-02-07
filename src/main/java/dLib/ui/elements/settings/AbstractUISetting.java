package dLib.ui.elements.settings;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import dLib.ui.HorizontalAlignment;
import dLib.ui.elements.CompositeUIElement;
import dLib.ui.elements.implementations.Renderable;
import dLib.ui.elements.prefabs.TextBox;
import dLib.ui.themes.UIThemeManager;
import dLib.util.settings.Setting;

public abstract class AbstractUISetting extends CompositeUIElement {
    /** Variables */
    private TextBox label;

    protected float textPerc = 0.25f;
    protected float arrowPerc = 0.025f;

    protected Setting<?> setting;

    public Integer xPos;
    public Integer yPos;

    public AbstractUISetting(Setting<?> setting, Integer xPos, Integer yPos, Integer width, Integer height){
        super(xPos, yPos, width, height);

        this.setting = setting;

        this.xPos = xPos;
        this.yPos = yPos;

        this.label = new TextBox(setting.getTitle(), xPos, yPos, (int)(width * textPerc), height).setHorizontalAlignment(HorizontalAlignment.LEFT);
        this.other.add(label);
    }

    /* AbstractUISetting markAsSubsetting(){
        other.add(new Renderable(UIThemeManager.getDefaultTheme().arrow_right, xPos, yPos, height, height));

        label.offset(height, 0);
        label.setWidth(label.getWidth() - height);
        return this;
    }*/
}
