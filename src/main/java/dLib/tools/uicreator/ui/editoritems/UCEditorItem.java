package dLib.tools.uicreator.ui.editoritems;

import dLib.ui.elements.UIElement;
import dLib.ui.elements.implementations.Resizeable;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;

public class UCEditorItem<UIElementType extends UIElement> extends Resizeable {
    //region Variables

    private UIElementType elementPreviewInstance;
    private UIElement.UIElementData elementData;

    //endregion Variables

    //region Constructors

    public UCEditorItem(UIElementData data, int width, int height){
        super(null, width, height);

        elementData = data;
        makePreviewInstance(width, height);
    }

    //endregion Constructors

    //region Methods

    public void makePreviewInstance(int width, int height){
        elementPreviewInstance = (UIElementType) elementData.makeUIElement();

        elementPreviewInstance.setDimensions(Dim.px(width), Dim.px(height));

        elementPreviewInstance.setParent(getParent());
        setParent(elementPreviewInstance);
    }

    public void remakePreviewInstance(){
        UIElement newInstance = elementData.makeUIElement();
        newInstance.setParent(elementPreviewInstance.getParent());
        this.setParent(newInstance);

        elementPreviewInstance = (UIElementType) newInstance;
    }

    //endregion Methods
}
