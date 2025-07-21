package dLib.mousestates;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.core.GameCursor;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.input.InputHelper;

public class AbstractMouseState implements Disposable {
    private String id;

    protected Texture textureOverride;

    public AbstractMouseState(String id) {
        this.id = id;
    }


    //region Methods

    //region Update

    public void update() {}

    //endregion

    //region State Enter & Exit

    public void onStateEnter() {}
    public void onStateExit() {}

    public void exitMouseState(){
        MouseStateManager.get().exitMouseState();
    }

    //endregion

    public String getId() {
        return id;
    }

    @Override
    public void dispose() {

    }

    @SpirePatch2(clz = GameCursor.class, method = "render")
    public static class RenderPatcher{
        public static void Postfix(GameCursor __instance, SpriteBatch sb, Color ___SHADOW_COLOR, float ___SHADOW_OFFSET_X, float ___OFFSET_X, float ___SHADOW_OFFSET_Y, float ___OFFSET_Y, float ___rotation){
            if(!GameCursor.hidden && !Settings.isControllerMode){
                if(!(Settings.isTouchScreen && !Settings.isDev)){
                    if(MouseStateManager.get().isInExternalState() && MouseStateManager.get().getCurrentState().textureOverride != null){
                        __instance.changeType(Enums.CUSTOM);
                        sb.setColor(___SHADOW_COLOR);
                        sb.draw(MouseStateManager.get().getCurrentState().textureOverride, (float) InputHelper.mX - 32.0F - ___SHADOW_OFFSET_X + ___OFFSET_X, (float)InputHelper.mY - 32.0F - ___SHADOW_OFFSET_Y + ___OFFSET_Y, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale, Settings.scale, ___rotation, 0, 0, 64, 64, false, false);
                        sb.setColor(Color.WHITE);
                        sb.draw(MouseStateManager.get().getCurrentState().textureOverride, (float)InputHelper.mX - 32.0F + ___OFFSET_X, (float)InputHelper.mY - 32.0F + ___OFFSET_Y, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale, Settings.scale, ___rotation, 0, 0, 64, 64, false, false);
                    }
                }
            }
        }
    }

    public static class Enums {
        @SpireEnum
        public static GameCursor.CursorType CUSTOM;
    }

    //endregion
}
