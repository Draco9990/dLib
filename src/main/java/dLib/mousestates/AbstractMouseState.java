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
import dLib.util.events.localevents.ConsumerEvent;
import dLib.util.events.localevents.RunnableEvent;

public class AbstractMouseState implements Disposable {

    //region Variables

    private String id;

    protected Texture textureOverride;

    public RunnableEvent preStateEnterEvent = new RunnableEvent();                                                      public static ConsumerEvent<AbstractMouseState> preStateEnterGlobalEvent = new ConsumerEvent<>();
    public RunnableEvent postStateEnterEvent = new RunnableEvent();                                                     public static ConsumerEvent<AbstractMouseState> postStateEnterGlobalEvent = new ConsumerEvent<>();
    public RunnableEvent preStateExitEvent = new RunnableEvent();                                                       public static ConsumerEvent<AbstractMouseState> preStateExitGlobalEvent = new ConsumerEvent<>();
    public RunnableEvent postStateExitEvent = new RunnableEvent();                                                      public static ConsumerEvent<AbstractMouseState> postStateExitGlobalEvent = new ConsumerEvent<>();

    //endreigon Variables

    //region Constructors

    public AbstractMouseState(String id) {
        this.id = id;
    }

    //endregion Constructors

    //region Methods

    //region Update

    public void update() {}

    //endregion

    //region State Enter & Exit

    public void onStateEnter() {}
    public void onStateExit() {}

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
                    if(MouseStateManager.isInExternalState() && MouseStateManager.getCurrentState().textureOverride != null){
                        __instance.changeType(Enums.CUSTOM);
                        sb.setColor(___SHADOW_COLOR);
                        sb.draw(MouseStateManager.getCurrentState().textureOverride, (float) InputHelper.mX - 32.0F - ___SHADOW_OFFSET_X + ___OFFSET_X, (float)InputHelper.mY - 32.0F - ___SHADOW_OFFSET_Y + ___OFFSET_Y, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale, Settings.scale, ___rotation, 0, 0, 64, 64, false, false);
                        sb.setColor(Color.WHITE);
                        sb.draw(MouseStateManager.getCurrentState().textureOverride, (float)InputHelper.mX - 32.0F + ___OFFSET_X, (float)InputHelper.mY - 32.0F + ___OFFSET_Y, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale, Settings.scale, ___rotation, 0, 0, 64, 64, false, false);
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
