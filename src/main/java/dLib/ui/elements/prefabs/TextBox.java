package dLib.ui.elements.prefabs;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import dLib.modcompat.ModManager;
import dLib.ui.HorizontalAlignment;
import dLib.ui.VerticalAlignment;
import dLib.ui.elements.implementations.Hoverable;
import dLib.ui.themes.UIThemeManager;
import dLib.util.FontManager;
import sayTheSpire.Output;

public class TextBox extends Hoverable {
    /** Variables */
    private String text;

    private BitmapFont font;
    private boolean wrap;

    private float fontScale;

    private HorizontalAlignment horizontalAlignment;
    private VerticalAlignment verticalAlignment;

    private String onTextChangedLine;

    /** Constructors */
    public TextBox(String text, int xPos, int yPos, int width, int height){
        this(text, xPos, yPos, width, height, 0.07f, 0.33f);
    }
    public TextBox(String text, int xPos, int yPos, int width, int height, float xMarginPerc, float yMarginPerc){
        super(null, xPos, yPos, width, height);

        float xMargin = xMarginPerc * width;
        float yMargin = yMarginPerc * height;
        x += (int) xMargin;
        y += (int) yMargin;

        this.width -= (int) xMargin * 2;
        this.height -= (int) yMargin * 2;

        horizontalAlignment = HorizontalAlignment.CENTER;
        verticalAlignment = VerticalAlignment.CENTER;
        wrap = false;

        this.text = text;

        setFont(FontManager.genericFont);

        this.renderColor = UIThemeManager.getDefaultTheme().textColor;
    }

    /** Update and render */
    @Override
    public void update() {
        super.update();
    }

    @Override
    public void render(SpriteBatch sb) {
        if(!shouldRender()) return;

        font.getData().setScale(fontScale);

        float halfWidth = (float) width / 2;
        float halfHeight = (float) height / 2;

        FontHelper.layout.setText(font, "lL");
        if(horizontalAlignment == HorizontalAlignment.LEFT){
            if(verticalAlignment == VerticalAlignment.TOP){
                FontHelper.renderFontLeftTopAligned(
                        sb,
                        font,
                        text,
                        x * Settings.xScale,
                        (y + height) * Settings.yScale,
                        renderColor);
            }
            if(verticalAlignment == VerticalAlignment.CENTER){
                FontHelper.renderFontLeft(
                        sb,
                        font,
                        text,
                        x * Settings.xScale,
                        (y + halfHeight) * Settings.yScale,
                        renderColor);
            }
            if(verticalAlignment == VerticalAlignment.BOTTOM){
                FontHelper.renderFontLeftDownAligned(
                        sb,
                        font,
                        text,
                        x * Settings.xScale,
                        (y) * Settings.yScale,
                        renderColor);
            }
        }
        if(horizontalAlignment == HorizontalAlignment.CENTER){
            if(verticalAlignment == VerticalAlignment.TOP){
                FontHelper.renderFontCenteredTopAligned(
                        sb,
                        font,
                        text,
                        (x + halfWidth) * Settings.xScale,
                        (y + height) * Settings.yScale - FontHelper.layout.height / 2,
                        renderColor);
            }
            if(verticalAlignment == VerticalAlignment.CENTER){
                FontHelper.renderFontCentered(
                        sb,
                        font,
                        text,
                        (x + halfWidth) * Settings.xScale,
                        (y + halfHeight) * Settings.yScale,
                        renderColor);
            }
            if(verticalAlignment == VerticalAlignment.BOTTOM){
                FontHelper.renderFontCentered(
                        sb,
                        font,
                        text,
                        (x + halfWidth) * Settings.xScale,
                        (y) * Settings.yScale + FontHelper.layout.height / 2,
                        renderColor);
            }
        }
        if(horizontalAlignment == HorizontalAlignment.RIGHT){
            if(verticalAlignment == VerticalAlignment.TOP){
                FontHelper.renderFontRightTopAligned(
                        sb,
                        font,
                        text,
                        (x + width) * Settings.xScale,
                        (y + height) * Settings.yScale,
                        renderColor);
            }
            if(verticalAlignment == VerticalAlignment.CENTER){
                FontHelper.renderFontRightAligned(
                        sb,
                        font,
                        text,
                        (x + width) * Settings.xScale,
                        (y + halfHeight) * Settings.yScale,
                        renderColor);
            }
            if(verticalAlignment == VerticalAlignment.BOTTOM){
                FontHelper.renderFontRightAligned(
                        sb,
                        font,
                        text,
                        (x + width) * Settings.xScale,
                        (y) * Settings.yScale + FontHelper.layout.height / 2,
                        renderColor);
            }
        }

        font.getData().setScale(1.f);

        if(hb != null){
            hb.render(sb);
        }
    }

    /** Getters and Setters */
    public TextBox setHorizontalAlignment(HorizontalAlignment alignment){
        this.horizontalAlignment = alignment;
        return this;
    }

    public TextBox setVerticalAlignment(VerticalAlignment alignment){
        this.verticalAlignment = alignment;
        return this;
    }

    public TextBox setAlignment(HorizontalAlignment horizontalAlignment, VerticalAlignment verticalAlignment){
        setHorizontalAlignment(horizontalAlignment);
        setVerticalAlignment(verticalAlignment);
        return this;
    }

    public TextBox setWrap(boolean wrap){
        this.wrap = wrap;
        return this;
    }

    public TextBox setFont(BitmapFont font){
        this.font = font;
        recalculateFontScale();
        return this;
    }

    @Override
    public TextBox setWidth(int newWidth) {
        super.setWidth(newWidth);
        recalculateFontScale();
        return this;
    }
    @Override
    public TextBox setHeight(int newHeight) {
        super.setHeight(newHeight);
        recalculateFontScale();
        return this;
    }

    public void setText(String text){
        if(!this.text.equals(text)){
            this.text = text;

            if(IsNonASCII()){
                setFont(FontManager.nonASCIIFont);
            }

            recalculateFontScale();

            if(ModManager.SayTheSpire.isActive()){
                if(getOnTextChangedLine(text) != null){
                    Output.text(getOnTextChangedLine(text), true);
                }
            }
        }
    }
    public String getText(){
        return text;
    }

    /** Say the Spire - Getters and Setters */
    public TextBox setOnTextChangedLine(String newLine) {
        this.onTextChangedLine = newLine;
        return this;
    }
    public String getOnTextChangedLine(String newText){ return this.onTextChangedLine; }

    /** Misc methods */
    protected void recalculateFontScale(){
        float fontScale = 0.1F;
        while(true){
            font.getData().setScale(fontScale);
            FontHelper.layout.setText(font, text);
            if(FontHelper.layout.height > height * Settings.yScale || (!wrap && FontHelper.layout.width > width * Settings.xScale)) {
                font.getData().setScale(1);
                this.fontScale =  fontScale - 0.1F;
                return;
            }
            fontScale+=0.1F;
        }
    }
    public boolean IsNonASCII(){
        return this.text != null && !this.text.isEmpty() && !this.text.matches("\\A\\p{ASCII}*\\z");
    }
}