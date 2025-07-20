package dLib.betterscreens.ui.elements.items;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.helpers.PotionHelper;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import dLib.util.Reflection;
import dLib.util.bindings.texture.AbstractTextureBinding;
import dLib.util.bindings.texture.Tex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class PotionSelectPopup extends GameItemSelectPopup<AbstractPotion> {
    public PotionSelectPopup() {
        super();

        ArrayList<AbstractPotion> allRelics = new ArrayList<>();
        allRelics.addAll(PotionHelper.getPotionsByRarity(AbstractPotion.PotionRarity.COMMON));
        allRelics.addAll(PotionHelper.getPotionsByRarity(AbstractPotion.PotionRarity.UNCOMMON));
        allRelics.addAll(PotionHelper.getPotionsByRarity(AbstractPotion.PotionRarity.RARE));
        allRelics.sort(Comparator.comparing(a -> a.name));
        setItemSelection(allRelics);
    }

    @Override
    public AbstractTextureBinding getItemTexture(AbstractPotion item) {
        return Tex.stat(((Texture) Reflection.getFieldValue("containerImg", item)));
    }

    @Override
    public String getItemName(AbstractPotion item) {
        return item.name;
    }

    @Override
    public Enum<?> getItemRarity(AbstractPotion item) {
        return item.rarity;
    }

    @Override
    public String getItemDescription(AbstractPotion item) {
        return item.description;
    }

    @Override
    public boolean hasItemFlavorText() {
        return false;
    }

    @Override
    public ArrayList<AbstractPotion.PotionRarity> getItemRaritiesForFilter() {
        return new ArrayList<>(Arrays.asList(
                AbstractPotion.PotionRarity.COMMON,
                AbstractPotion.PotionRarity.UNCOMMON,
                AbstractPotion.PotionRarity.RARE
        ));
    }
}
