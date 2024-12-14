package dLib.properties.ui.elements;

import com.badlogic.gdx.graphics.Texture;
import dLib.ui.Alignment;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.implementations.Toggle;
import dLib.ui.elements.prefabs.*;
import dLib.util.EnumHelpers;
import dLib.properties.objects.templates.TAlignmentProperty;
import dLib.util.TextureManager;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.AbstractPosition;
import org.apache.logging.log4j.util.BiConsumer;

import java.util.ArrayList;

public class AlignmentPropertyEditor extends AbstractPropertyEditor<TAlignmentProperty<? extends TAlignmentProperty>> {
    //region Variables

    TextButton leftButton;
    TextButton rightButton;

    private Toggle[][] alignmentButtons;

    //endregion

    //region Constructors

    public AlignmentPropertyEditor(TAlignmentProperty<? extends TAlignmentProperty> setting, AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, boolean multiline) {
        super(setting, xPos, yPos, width, multiline);
    }

    //endregion

    //region Methods

    @Override
    protected UIElement buildContent(TAlignmentProperty<? extends TAlignmentProperty> property, AbstractDimension width, AbstractDimension height) {
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

                        property.setValue(new Alignment(halign, valign));
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

        property.onValueChangedEvent.subscribe(this, (alignment, alignment2) -> {
            int newHAlign = property.getHorizontalAlignment().ordinal();
            int newVAlign = property.getVerticalAlignment().ordinal();

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

        return grid;
    }

    @Override
    public boolean onLeftInteraction() {
        leftButton.getButton().trigger();
        return true;
    }

    @Override
    public boolean onRightInteraction() {
        rightButton.getButton().trigger();
        return true;
    }

    //endregion
}
