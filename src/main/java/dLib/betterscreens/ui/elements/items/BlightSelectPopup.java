package dLib.betterscreens.ui.elements.items;

import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.helpers.BlightHelper;
import dLib.util.bindings.texture.AbstractTextureBinding;
import dLib.util.bindings.texture.Tex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class BlightSelectPopup extends GameItemSelectPopup<AbstractBlight> {
    public BlightSelectPopup() {
        super("Blights");

        BlightHelper.initialize();

        ArrayList<AbstractBlight> allBlights = new ArrayList<>();
        for (String b : BlightHelper.blights) allBlights.add(BlightHelper.getBlight(b));
        allBlights.sort(Comparator.comparing(a -> a.name));
        setItemSelection(allBlights);
    }

    @Override
    public AbstractTextureBinding getItemTexture(AbstractBlight item) {
        return Tex.stat(item.img);
    }

    @Override
    public String getItemName(AbstractBlight item) {
        return item.name;
    }

    @Override
    public String getItemDescription(AbstractBlight item) {
        return item.description;
    }
}
