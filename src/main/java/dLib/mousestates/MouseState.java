package dLib.mousestates;

import com.badlogic.gdx.utils.Disposable;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.helpers.input.InputHelper;

public class MouseState implements Disposable {
    private String id;

    public MouseState(String id) {
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

    //endregion
}
