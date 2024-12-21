package dLib.ui.screens;

import dLib.ui.elements.UIElement;

import dLib.util.DLibLogger;
import dLib.util.IntegerVector2;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.Pos;

import java.io.*;
import java.util.ArrayList;

// Abstract version of a screen. Deprecated. Code remains as reference.
public final class AbstractScreen_DEPRECATED extends UIElement {
    //region Constructors

    public AbstractScreen_DEPRECATED(){
        this(Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill());
    }
    public AbstractScreen_DEPRECATED(AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, AbstractDimension height){
        super(xPos, yPos, width, height);
    }

    public AbstractScreen_DEPRECATED(AbstractScreenData data){
        this();
        ArrayList<UIElement> makeLiveItems = data.makeLiveItems();
        for (int i = 0; i < makeLiveItems.size(); i++) {
            UIElement liveElement = makeLiveItems.get(i);
            addChild(liveElement, true);
        }
    }

    //endregion

    //region Methods

    //region Update & Render

    //endregion

    //region Open & Close

    //endregion

    //region Interactions

    @Override
    public boolean onDownInteraction() {
        if(parent != null) return super.onDownInteraction();

        selectNextChild();
        return true;
    }

    @Override
    public boolean onUpInteraction() {
        if(parent != null) return super.onUpInteraction();

        selectPreviousChild();
        return true;
    }

    //endregion

    //region Theme


    //endregion

    //endregion

    public static class AbstractScreenData extends UIElementData implements Serializable {
        private static final long serialVersionUID = 1L;

        public int referenceWidth = 1490;
        public int referenceHeight = 840;

        public ArrayList<UIElementData> data = new ArrayList<>();

        public String modID;

        @Override
        public UIElement makeUIElement() {
            return new AbstractScreen_DEPRECATED();
        }

        public ArrayList<UIElement> makeLiveItems(){
            ArrayList<UIElement> elements = new ArrayList<>();
            for(UIElementData elementData : data){
                UIElement liveInstance = elementData.makeUIElement();
                if(liveInstance == null){
                    DLibLogger.log("Failed to create a live instance of an element!");
                    continue;
                }

                rescaleElement(liveInstance, new IntegerVector2(1920, 1080));
                elements.add(liveInstance);
            }

            return elements;
        }

        private void rescaleElement(UIElement element, IntegerVector2 targetResolution){
            float scaleMultX = (float)targetResolution.x / referenceWidth;
            float scaleMultY = (float)targetResolution.y / referenceHeight;

            element.setWorldPosition((int) (element.getWorldPositionX() * scaleMultX), (int) (element.getWorldPositionY() * scaleMultY));
            element.setDimensions((int) (element.getWidth() * scaleMultX), (int) (element.getHeight() * scaleMultY));
        }
    }
}
