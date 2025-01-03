package dLib.ui.elements.items.text;

import com.badlogic.gdx.graphics.Color;
import dLib.ui.animations.exit.UIAnimation_FadeOut;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.Pos;

public class TextNotification extends TextBox{
    public TextNotification(String text, VerboseLevel verbosity) {
        this(text, verbosity, Pos.px(1080-170));
    }

    public TextNotification(String text, VerboseLevel verbosity, AbstractPosition yPos) {
        super(text, Pos.px(0), yPos, Dim.fill(), Dim.px(30));

        switch (verbosity) {
            case INFO:
                setTextRenderColor(Color.BLUE);
                break;
            case WARNING:
                setTextRenderColor(Color.YELLOW);
                break;
            case ERROR:
                setTextRenderColor(Color.RED);
                break;
        }

        setLifespan(3f);
    }

    public enum VerboseLevel {
        INFO, WARNING, ERROR
    }
}
