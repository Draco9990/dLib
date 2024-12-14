package dLib.ui.elements.prefabs;

import com.badlogic.gdx.graphics.Texture;
import dLib.ui.elements.implementations.Toggle;
import dLib.ui.themes.UIThemeManager;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.Pos;

import java.io.Serializable;

public class Checkbox extends Toggle {
    //region Variables

    //endregion

    //region Constructors

    public Checkbox(AbstractPosition xPos, AbstractPosition yPos){
        this(xPos, yPos, Dim.fill(), Dim.fill());
    }
    public Checkbox(AbstractDimension width, AbstractDimension height){
        this(Pos.px(0), Pos.px(0), width, height);
    }
    public Checkbox(AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, AbstractDimension height) {
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
