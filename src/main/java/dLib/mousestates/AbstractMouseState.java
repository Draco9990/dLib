package dLib.mousestates;

import com.badlogic.gdx.utils.Disposable;

public class AbstractMouseState implements Disposable {
    private String id;

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

    //endregion
}
