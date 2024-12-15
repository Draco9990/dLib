package dLib.mousestates;

import com.badlogic.gdx.utils.Disposable;

public class MouseState implements Disposable {
    private String id;

    public MouseState(String id) {
        this.id = id;
    }

    //region Methods

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

    //endregion
}
