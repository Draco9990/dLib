package dLib.properties.ui.elements;

import com.badlogic.gdx.graphics.Texture;
import dLib.ui.Alignment;
import dLib.ui.elements.implementations.Toggle;
import dLib.ui.elements.prefabs.*;
import dLib.util.EnumHelpers;
import dLib.util.TextureManager;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;

import java.util.ArrayList;

public class AlignmentValueEditor extends AbstractValueEditor<Alignment> {
    //region Variables

    private Toggle[][] alignmentButtons;

    //endregion

    //region Constructors

    public AlignmentValueEditor(Alignment value, AbstractDimension width, AbstractDimension height) {
        super(width, Dim.width());

        alignmentButtons = new Toggle[3][3];

        ArrayList<Enum<Alignment.HorizontalAlignment>> allHorizontalAlignments = EnumHelpers.getAllEntries(Alignment.HorizontalAlignment.LEFT);
        ArrayList<Enum<Alignment.VerticalAlignment>> allVerticalAlignments = EnumHelpers.getAllEntries(Alignment.VerticalAlignment.BOTTOM);

        PredefinedGrid grid = new PredefinedGrid(3, 3, width, Dim.width());
        for(int i = 0; i < 3; i++){
            for(int j = 2; j >= 0; j--){
                String textureLoc = "dLibResources/images/ui/themes/basic/alignment/align" + i + j + ".png";
                Texture alignmentTexture = TextureManager.getTexture(textureLoc);
                int finalI = i;
                int finalJ = j;
                alignmentButtons[i][j] = new Toggle(alignmentTexture, Dim.fill(), Dim.fill()){
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

                        Alignment.HorizontalAlignment halign = (Alignment.HorizontalAlignment) allHorizontalAlignments.get(finalI);
                        Alignment.VerticalAlignment valign = (Alignment.VerticalAlignment) allVerticalAlignments.get(finalJ);

                        setValueEvent.invoke(objectConsumer -> objectConsumer.accept(new Alignment(halign, valign)));
                    }
                };

                int currentHAlign = value.horizontalAlignment.ordinal();
                int currentVAlign = value.verticalAlignment.ordinal();

                if(i == currentHAlign && j == currentVAlign){
                    alignmentButtons[i][j].setToggled(true);
                }

                grid.setGridSlotElement(2-j, i, alignmentButtons[i][j]);
            }
        }

        onValueChangedEvent.subscribe(this, (newAlignment) -> {
            int newHAlign = newAlignment.horizontalAlignment.ordinal();
            int newVAlign = newAlignment.verticalAlignment.ordinal();

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
