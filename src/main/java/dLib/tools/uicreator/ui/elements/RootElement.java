package dLib.tools.uicreator.ui.elements;

import com.badlogic.gdx.graphics.Color;
import dLib.properties.objects.templates.TProperty;
import dLib.ui.Alignment;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.components.ElementGroupModifierComponent;
import dLib.ui.elements.items.Renderable;
import dLib.ui.resources.UICommonResources;
import dLib.util.bindings.texture.Tex;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.dimensions.FillDimension;

import java.io.Serializable;
import java.util.ArrayList;

public class RootElement extends Renderable {
    private boolean inEditor = true;

    public RootElement(){
        super(Tex.stat(UICommonResources.white_pixel), Dim.px(1536), Dim.px(864));

        Color halfTransparent = new Color(1, 1, 1, 0.4f);
        setRenderColor(halfTransparent);

        setHorizontalAlignment(Alignment.HorizontalAlignment.CENTER);
        setVerticalAlignment(Alignment.VerticalAlignment.CENTER);
        setPassthrough(true);
    }

    public RootElement(RootElementData data){
        super(data);

        inEditor = data.inEditor;
    }

    @Override
    public void postConstruct() {
        super.postConstruct();

        if(inEditor){
            onLeftClickEvent.subscribeManaged(() -> ElementGroupModifierComponent.deselectGroupComponents("editorItem"));

            //* The fill dimension in-editor is set to 1920x1080, whereas it'll actually fill the parent container in-game
            if(getWidthRaw() instanceof FillDimension){
                setWidth(Dim.px(1920));
            }
            if(getHeightRaw() instanceof FillDimension){
                setHeight(Dim.px(1080));
            }
        }
        else{
            setRenderColor(new Color(0, 0, 0, 0));
        }
    }

    public static class RootElementData extends RenderableData implements Serializable {
        public static final long serialVersionUID = 1L;

        public boolean inEditor = true;

        public RootElementData(){
            textureBinding.setValue(Tex.resource(UICommonResources.class, "white_pixel"));
            renderColor.setValue(new Color(1, 1, 1, 0.4f));

            alignment.setValue(new Alignment(Alignment.HorizontalAlignment.CENTER, Alignment.VerticalAlignment.CENTER));
            isPassthrough.setValue(true);

            width.setValue(new FillDimension());
            height.setValue(new FillDimension());
        }

        @Override
        public ArrayList<TProperty<?, ?>> getEditableProperties() {
            ArrayList<TProperty<?, ?>> properties = new ArrayList<>();
            properties.add(width);
            properties.add(height);
            return properties;
        }

        @Override
        public UIElement makeUIElement_internal() {
            return new RootElement(this);
        }
    }
}
