package dLib.patchnotes.ui;

import dLib.patchnotes.Patchnotes;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.items.Image;
import dLib.ui.elements.items.VerticalCollapsableBox;
import dLib.ui.elements.items.buttons.CancelButton;
import dLib.ui.elements.items.itembox.VerticalBox;
import dLib.ui.elements.items.scroll.Scrollbox;
import dLib.ui.elements.items.text.TextBox;
import dLib.ui.resources.UICommonResources;
import dLib.util.bindings.texture.Tex;
import dLib.util.ui.dimensions.Dim;

public class PatchnotesScreen extends UIElement {
    public PatchnotesScreen(Patchnotes patchnotes){
        super();

        CancelButton cancelButton = new CancelButton();
        cancelButton.postLeftClickEvent.subscribe(cancelButton, this::dispose);
        addChild(cancelButton);

        Image outer = new Image(Tex.stat(UICommonResources.bg02_inner), Dim.fill(), Dim.fill());
        {
            Image inner = new Image(Tex.stat(UICommonResources.bg02_inner), Dim.fill(), Dim.fill());
            {
                Scrollbox contentBox = new Scrollbox(Dim.fill(), Dim.fill());
                contentBox.setIsHorizontal(false);
                {
                    VerticalBox vBox = new VerticalBox(Dim.fill(), Dim.fill());
                    {
                        for(Patchnotes.PatchnotesEntry entry : patchnotes.entries){
                            VerticalCollapsableBox entryBox = new VerticalCollapsableBox(entry.header.resolve());
                            {
                                TextBox descriptionBox = new TextBox(entry.description.resolve(), Dim.fill(), Dim.auto());
                                entryBox.addItem(descriptionBox);
                            }
                            vBox.addChild(entryBox);
                        }
                    }
                    contentBox.addChild(vBox);
                }
                inner.addChild(contentBox);
            }
            outer.addChild(inner);
        }
        addChild(outer);
    }
}
