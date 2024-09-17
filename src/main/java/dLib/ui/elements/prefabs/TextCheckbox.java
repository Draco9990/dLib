package dLib.ui.elements.prefabs;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.helpers.FontHelper;
import dLib.ui.Alignment;
import dLib.ui.elements.UIElement;
import dLib.ui.themes.UIThemeManager;
import dLib.util.bindings.texture.TextureThemeBinding;

import java.io.Serializable;

public class TextCheckbox extends UIElement {
    //region Variables

    private Checkbox checkbox;
    private TextBox label;

    //endregion

    //region Constructors

    public TextCheckbox(String text, int xPos, int yPos, int width, int height, CheckboxOrientation orientation){
        super(xPos, yPos, width, height);

        int checkboxDims = Math.min(width, height);
        int textWidth = width - 5 - checkboxDims;
        if(textWidth < 0){
            textWidth = (int) (width * 0.75f);

            checkboxDims = width - textWidth - 5;
        }

        int checkboxYPos = (int) ((height - checkboxDims) * 0.5f);

        checkbox = new Checkbox(orientation == CheckboxOrientation.LEFT ? 0 : textWidth + 5, checkboxYPos, checkboxDims, checkboxDims);
        addChildCS(checkbox);

        label = new TextBox(text, orientation == CheckboxOrientation.RIGHT ? 0 : checkboxDims + 5, 0, textWidth, height);
        label.setFont(FontHelper.buttonLabelFont);
        label.setTextRenderColor(Color.GOLD);
        label.setMarginPercX(0);

        if(orientation == CheckboxOrientation.LEFT){
            label.setHorizontalAlignment(Alignment.HorizontalAlignment.LEFT);
        }
        else{
            label.setHorizontalAlignment(Alignment.HorizontalAlignment.RIGHT);
        }

        float scale = height / 46f;
        label.setFontScaleOverride(0.7f * scale);

        addChildNCS(label);
    }

    public TextCheckbox(TextCheckboxData data){
        super(data);

        checkbox = data.checkboxData.makeUIElement();
        addChildCS(checkbox);

        label = data.textBoxData.makeUIElement();
        addChildCS(label);
    }

    //endregion

    //region Methods

    public Checkbox getCheckbox(){
        return checkbox;
    }

    public TextBox getTextBox(){
        return label;
    }

    //endregion

    public enum CheckboxOrientation{
        LEFT,
        RIGHT
    }

    public static class TextCheckboxData extends UIElement.UIElementData implements Serializable {
        private static final long serialVersionUID = 1L;

        public TextBox.TextBoxData textBoxData = new TextBox.TextBoxData();
        public Checkbox.CheckboxData checkboxData = new Checkbox.CheckboxData();

        @Override
        public TextCheckbox makeUIElement() {
            return new TextCheckbox(this);
        }
    }
}
