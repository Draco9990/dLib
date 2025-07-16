package dLib.betterscreens.ui.elements.items;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.helpers.PotionHelper;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import dLib.util.Reflection;
import dLib.util.bindings.texture.AbstractTextureBinding;
import dLib.util.bindings.texture.Tex;

import java.util.ArrayList;
import java.util.Comparator;

public class PotionSelectPopup extends GameItemSelectPopup<AbstractPotion, AbstractPotion.PotionRarity> {
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
    public AbstractPotion.PotionRarity getDefaultItemRarity() {
        return AbstractPotion.PotionRarity.PLACEHOLDER;
    }

    @Override
    public AbstractPotion.PotionRarity getItemRarity(AbstractPotion item) {
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
}
