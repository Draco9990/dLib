package dLib.ui.elements.prefabs;

import com.badlogic.gdx.graphics.Texture;
import dLib.ui.elements.implementations.Toggle;
import dLib.ui.themes.UIThemeManager;

import java.io.Serializable;

public class Checkbox extends Toggle {
    //region Variables

    //endregion

    //region Constructors

    public Checkbox(int xPos, int yPos, int width, int height) {
        super(UIThemeManager.getDefaultTheme().checkbox_unchecked, UIThemeManager.getDefaultTheme().checkbox_checked, xPos, yPos, width, height);
    }

    public Checkbox(CheckboxData data) {
        super(data);
    }

    //endregion

    //region Methods

    //endregion

    public static class CheckboxData extends ToggleData implements Serializable {
        private static final long serialVersionUID = 1L;

        //region Variables

        //endregion

        //region Constructors

        public CheckboxData() {
            super();
        }

        //endregion

        //region Methods

        @Override
        public Checkbox makeUIElement() {
            return new Checkbox(this);
        }

        //endregion
    }
}
