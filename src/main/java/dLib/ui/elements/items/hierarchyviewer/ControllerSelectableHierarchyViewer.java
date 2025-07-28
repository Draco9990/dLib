package dLib.ui.elements.items.hierarchyviewer;

import dLib.ui.elements.UIElement;
import dLib.ui.elements.components.UIOverlayElementComponent;
import dLib.ui.elements.components.UITransientElementComponent;
import dLib.ui.elements.items.Image;
import dLib.ui.resources.UICommonResources;
import dLib.util.bindings.texture.Tex;
import dLib.util.bindings.texture.TextureStaticBinding;
import dLib.util.ui.dimensions.Dim;

public class ControllerSelectableHierarchyViewer extends HierarchyViewer{

    public ControllerSelectableHierarchyViewer(){
        super();

        UIElement.postSelectionStateChangedGlobalEvent.subscribe(this, (element, selected) -> {
            if(element.getTopParent() instanceof HierarchyViewerPopup){
                return;
            }

            delayedActions.add(() -> loadForElement(element.getTopParent()));
        });
    }

    @Override
    protected HierarchyViewerChildElementButton makeHierarchyViewerElementButton_internal(UIElement element) {
        HierarchyViewerChildElementButton button = super.makeHierarchyViewerElementButton_internal(element);

        TextureStaticBinding imageTexture = Tex.stat(UICommonResources.transparent_pixel);
        if((element.isControllerSelectable() && element.isEnabled()) || element.isSelected() || element.isControllerModal()) {
            imageTexture = Tex.stat(UICommonResources.dropZoneOptionBg);
        }

        Image payloadOverlay = new Image(imageTexture, Dim.fill(), Dim.fill());
        if(element.isSelected()) payloadOverlay.setHueShiftAmount(220);
        if(element.isControllerModal()) payloadOverlay.setHueShiftAmount(150);
        payloadOverlay.setPassthrough(true);
        payloadOverlay.postHoveredEvent.subscribe(payloadOverlay, () -> {
            Image hoverOverlay = new Image(Tex.stat(UICommonResources.advancedDebugOverlay), Dim.fill(), Dim.fill());
            hoverOverlay.setPassthrough(true);
            hoverOverlay.addComponent(new UITransientElementComponent());
            hoverOverlay.addComponent(new UIOverlayElementComponent());
            hoverOverlay.setID("ControllerSelectableHierarchyViewerHoverOverlay");
            element.addChild(hoverOverlay);
        });
        payloadOverlay.postUnhoveredEvent.subscribe(payloadOverlay, () -> {
            element.disposeChild("ControllerSelectableHierarchyViewerHoverOverlay");
        });
        button.addChild(payloadOverlay);

        return button;
    }

    @Override
    protected boolean shouldListChild(UIElement element) {
        return (element.isControllerSelectable() && element.isEnabled()) || element.isSelected() || element.isControllerModal();
    }

    @Override
    public boolean allowsReordering() {
        return false;
    }
}
