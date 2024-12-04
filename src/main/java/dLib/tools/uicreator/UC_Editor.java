package dLib.tools.uicreator;

import com.badlogic.gdx.graphics.Color;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.implementations.Renderable;
import dLib.ui.themes.UITheme;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.Pos;

public class UC_Editor extends Renderable {

    public UC_Editor(){
        super(UITheme.whitePixel, Pos.perc(0), Pos.perc(0), Dim.fill(), Dim.fill());
        setRenderColor(Color.valueOf("#151515FF"));
    }

    //region Subclasses

    //region Subclasses
}
