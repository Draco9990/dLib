package dLib.util;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.mainMenu.MainMenuScreen;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PersistentActionManager {
    /** Instantiators */
    public static Map<String, PersistentActionManager> instances = new HashMap<>();
    public static PersistentActionManager get(String modId){
        if(instances == null){
            instances = new HashMap<>();
        }

        if(!instances.containsKey(modId)){
            instances.put(modId, new PersistentActionManager());
        }

        return instances.get(modId);
    }

    /** Variables */
    private ArrayList<AbstractGameAction> actionList = new ArrayList<>();
    private ArrayList<AbstractGameEffect> effectList = new ArrayList<>();

    private AbstractGameAction currAction = null;

    /** Update */
    public static void updateAll(){
        if(instances == null) return;
        for(PersistentActionManager instance : instances.values()){
            instance.update();
        }
    }

    public void update(){
        updateAction();
        updateEffect();
    }

    private void updateAction(){
        if(currAction == null && actionList.isEmpty()) return;

        if(currAction == null) currAction = actionList.remove(0);
        currAction.update();
        if(currAction.isDone) currAction = null;
    }
    private void updateEffect(){
        Iterator<AbstractGameEffect> effectIterator = effectList.iterator();
        while(effectIterator.hasNext()){
            AbstractGameEffect effect = effectIterator.next();

            effect.update();
            if(effect.isDone){
                effectIterator.remove();
            }
        }
    }

    /** Render */
    public static void renderAll(SpriteBatch sb){
        if(instances == null) return;
        for(PersistentActionManager instance : instances.values()){
            instance.render(sb);
        }
    }

    public void render(SpriteBatch sb){
        renderEffect(sb);
    }

    private void renderEffect(SpriteBatch sb){
        Iterator<AbstractGameEffect> effectIterator = effectList.iterator();
        while(effectIterator.hasNext()){
            AbstractGameEffect effect = effectIterator.next();
            effect.render(sb);
        }
    }

    /** Methods */
    public static void reset(String modId){
        if(instances == null) return;
        if(!instances.containsKey(modId)) return;

        instances.get(modId).reset();
    }

    public void reset(){
        actionList.clear();
        effectList.clear();
        currAction = null;
    }

    public void addToBottom(AbstractGameAction a){
        actionList.add(a);
    }
    public void addToBottom(AbstractGameEffect a){
        effectList.add(a);
    }

    public void addToTop(AbstractGameAction e){
        actionList.add(0, e);
    }
    public void addToTop(AbstractGameEffect e){
        effectList.add(0, e);
    }

    public void add(AbstractGameAction a){
        addToBottom(a);
    }
    public void add(AbstractGameEffect a){
        addToBottom(a);
    }

    /** Patches */
    public static class Patches{
        @SpirePatch2(clz = AbstractDungeon.class, method = "update")
        public static class ActionInGameInjector{
            public static void Postfix(){
                if(CardCrawlGame.isInARun()){
                    updateAll();
                }
            }
        }
        @SpirePatch2(clz = AbstractDungeon.class, method = "render")
        public static class ActionRenderInGameInjector{
            public static void Postfix(SpriteBatch sb){
                if(CardCrawlGame.isInARun()){
                    renderAll(sb);
                }
            }
        }

        @SpirePatch2(clz = MainMenuScreen.class, method = "update")
        public static class ActionUpdateOutOfGamePatch{
            public static void Postfix(){
                if(!CardCrawlGame.isInARun()){
                    updateAll();
                }
            }
        }
        @SpirePatch2(clz = MainMenuScreen.class, method = "render")
        public static class ActionRenderOutOfGamePatch{
            public static void Postfix(SpriteBatch sb){
                if(!CardCrawlGame.isInARun()){
                    renderAll(sb);
                }
            }
        }
    }
}
