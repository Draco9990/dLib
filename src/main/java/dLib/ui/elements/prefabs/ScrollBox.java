package dLib.ui.elements.prefabs;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import dLib.properties.objects.*;
import dLib.ui.elements.UIElement;
import dLib.ui.util.ESelectionMode;
import dLib.util.IntegerVector2;

import javax.swing.text.html.Option;
import java.io.Serializable;
import java.util.Optional;

public abstract class ScrollBox extends UIElement {
    protected Scrollbar scrollbar;

    public ScrollBox(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public ScrollBox(ScrollBoxData data) {
        super(data);
    }

    protected void initialize(){
        scrollbar = buildScrollbar();
    }

    protected abstract Scrollbar buildScrollbar();

    @Override
    public UIElement addChild(UIElementChild child) {
        child.element.setElementMask(this);

        return super.addChild(child);
    }

    @Override
    protected void updateChildren() {
        if(scrollbar != null){
            scrollbar.update();
        }

        super.updateChildren();
    }

    @Override
    protected void renderChildren(SpriteBatch sb) {
        super.renderChildren(sb);

        if(scrollbar != null){
            scrollbar.render(sb);
        }
    }

    public static class ScrollBoxData extends UIElement.UIElementData implements Serializable {
        private static final long serialVersionUID = 1L;
    }
}
