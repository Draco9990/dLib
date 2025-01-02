package dLib.tools.uicreator.ui.elements;

import com.badlogic.gdx.graphics.Color;
import dLib.ui.Alignment;
import dLib.ui.elements.components.ElementGroupModifierComponent;
import dLib.ui.elements.items.Renderable;
import dLib.ui.resources.UICommonResources;
import dLib.util.bindings.texture.Tex;
import dLib.util.ui.dimensions.Dim;

public class RootElement extends Renderable {
    public RootElement(){
        super(Tex.stat(UICommonResources.white_pixel), Dim.px(1536), Dim.px(864));

        Color halfTransparent = new Color(1, 1, 1, 0.4f);
        setRenderColor(halfTransparent);

        setHorizontalAlignment(Alignment.HorizontalAlignment.CENTER);
        setVerticalAlignment(Alignment.VerticalAlignment.CENTER);
        onLeftClickEvent.subscribeManaged(() -> ElementGroupModifierComponent.deselectGroupComponents("editorItem"));
        setPassthrough(true);
    }
}
