package dLib.betterscreens.ui.elements.items;

import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import dLib.util.bindings.texture.AbstractTextureBinding;
import dLib.util.bindings.texture.Tex;

import java.util.ArrayList;
import java.util.Comparator;

public class RelicSelectPopup extends GameItemSelectPopup<AbstractRelic> {
    public RelicSelectPopup() {
        super();

        ArrayList<AbstractRelic> allRelics = new ArrayList<>();
        allRelics.addAll(RelicLibrary.starterList);
        allRelics.addAll(RelicLibrary.commonList);
        allRelics.addAll(RelicLibrary.uncommonList);
        allRelics.addAll(RelicLibrary.rareList);
        allRelics.addAll(RelicLibrary.bossList);
        allRelics.addAll(RelicLibrary.specialList);
        allRelics.addAll(RelicLibrary.shopList);
        allRelics.sort(Comparator.comparing(a -> a.name));
        setItemSelection(allRelics);
    }

    @Override
    public AbstractTextureBinding getItemTexture(AbstractRelic item) {
        return Tex.stat(item.img);
    }

    @Override
    public String getItemName(AbstractRelic item) {
        return item.name;
    }

    @Override
    public String getItemRarity(AbstractRelic item) {
        return item.toString();
    }

    @Override
    public String getItemFlavorText(AbstractRelic item) {
        return item.flavorText;
    }
}
