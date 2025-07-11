package dLib.ui.elements.items.text;

import com.badlogic.gdx.graphics.Color;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.Pos;

public class TextNotification extends TextBox{
    public TextNotification(String text, VerboseLevel verbosity) {
        this(text, verbosity, Pos.px(1080-170));
    }

    public TextNotification(String text, VerboseLevel verbosity, AbstractPosition yPos) {
        super(text, Pos.px(0), yPos, Dim.fill(), Dim.px(30));

        setFontSize(18);

        switch (verbosity) {
            case NONE:
                setTextRenderColor(Color.WHITE);
                break;
            case SUCCESS:
                setTextRenderColor(Color.GREEN);
                break;
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
        NONE, INFO, SUCCESS, WARNING, ERROR
    }
}
