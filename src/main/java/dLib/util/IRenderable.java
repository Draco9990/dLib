package dLib.util;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface IRenderable {
    void render(SpriteBatch sb);

    float getDrawX();
    float getDrawY();
}
