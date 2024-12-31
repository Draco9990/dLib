package dLib.properties.ui.elements;

import com.badlogic.gdx.graphics.Texture;
import dLib.properties.objects.AlignmentProperty;
import dLib.ui.Alignment;
import dLib.ui.elements.items.Toggle;
import dLib.ui.elements.items.*;
import dLib.util.helpers.EnumHelpers;
import dLib.util.TextureManager;
import dLib.util.bindings.texture.Tex;
import dLib.util.ui.dimensions.Dim;

import java.util.ArrayList;

public class AlignmentValueEditor extends AbstractValueEditor<Alignment, AlignmentProperty> {
    //region Variables

    private Toggle[][] alignmentButtons;

    //endregion

    //region Constructors

    public AlignmentValueEditor(Alignment value){
        this(new AlignmentProperty(value));
    }

    public AlignmentValueEditor(AlignmentProperty property) {
        super(property);

        alignmentButtons = new Toggle[3][3];

        ArrayList<Alignment.HorizontalAlignment> allHorizontalAlignments = EnumHelpers.getAllEntries(Alignment.HorizontalAlignment.LEFT);
        ArrayList<Alignment.VerticalAlignment> allVerticalAlignments = EnumHelpers.getAllEntries(Alignment.VerticalAlignment.BOTTOM);

        PredefinedGrid grid = new PredefinedGrid(3, 3, Dim.fill(), Dim.mirror());
        for(int i = 0; i < 3; i++){
            for(int j = 2; j >= 0; j--){
                String textureLoc = "dLibResources/images/ui/common/alignment/" + i + j + ".png";
                Texture alignmentTexture = TextureManager.getTexture(textureLoc);
                int finalI = i;
                int finalJ = j;
                alignmentButtons[i][j] = new Toggle(Tex.stat(alignmentTexture), Dim.fill(), Dim.fill()){
                    @Override
                    public void toggle() {
                        if(isToggled()){
                            return;
                        }

                        super.toggle();

                        for(int k = 0; k < 3; k++){
                            for(int l = 0; l < 3; l++){
                                if(alignmentButtons[k][l] != this){
                                    alignmentButtons[k][l].setToggled(false);
                                }
                            }
                        }

                        Alignment.HorizontalAlignment halign = allHorizontalAlignments.get(finalI);
                        Alignment.VerticalAlignment valign = allVerticalAlignments.get(finalJ);

                        boundProperty.setValue(new Alignment(halign, valign));
                    }
                };

                int currentHAlign = property.getHorizontalAlignment().ordinal();
                int currentVAlign = property.getVerticalAlignment().ordinal();

                if(i == currentHAlign && j == currentVAlign){
                    alignmentButtons[i][j].setToggled(true);
                }

                grid.setGridSlotElement(2-j, i, alignmentButtons[i][j]);
            }
        }

        boundProperty.onValueChangedEvent.subscribe(this, (oldValue, newValue) -> {
            if(!isEditorValidForPropertyChange()) return;

            int newHAlign = newValue.horizontalAlignment.ordinal();
            int newVAlign = newValue.verticalAlignment.ordinal();

            for(int i = 0; i < 3; i++){
                for(int j = 0; j < 3; j++){
                    if(i == newHAlign && j == newVAlign){
                        alignmentButtons[i][j].setToggled(true);
                    }
                    else {
                        alignmentButtons[i][j].setToggled(false);
                    }
                }
            }
        });

        addChildNCS(grid);
    }

    //endregion
}
