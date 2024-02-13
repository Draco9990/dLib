package dLib.ui.elements.prefabs;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import dLib.modcompat.ModManager;
import dLib.ui.Alignment;
import dLib.ui.data.prefabs.TextBoxData;
import dLib.ui.elements.implementations.Hoverable;
import dLib.ui.themes.UIThemeManager;
import dLib.util.FontManager;
import sayTheSpire.Output;

import java.util.function.Consumer;

public class TextBox extends Hoverable {
    /** Variables */
    private String text;

    private Color textRenderColor;
    private BitmapFont font;
    private boolean wrap;

    private float fontScale;

    private Alignment.HorizontalAlignment horizontalAlignment;
    private Alignment.VerticalAlignment verticalAlignment;

    private String onTextChangedLine;

    private float marginPercX = 0.0f;
    private float marginPercY = 0.0f;

    private Consumer<String> onTextChangedConsumer;

    private int paddingLeft = 0;
    private int paddingTop = 0;
    private int paddingRight = 0;
    private int paddingBottom = 0;

    /** Constructors */
    public TextBox(String text, int xPos, int yPos, int width, int height){
        this(text, xPos, yPos, width, height, 0.07f, 0.33f);
    }
    public TextBox(String text, int xPos, int yPos, int width, int height, float xMarginPerc, float yMarginPerc){
        super(null, xPos, yPos, width, height);

        this.marginPercX = xMarginPerc;
        this.marginPercY = yMarginPerc;

        horizontalAlignment = Alignment.HorizontalAlignment.CENTER;
        verticalAlignment = Alignment.VerticalAlignment.CENTER;
        wrap = false;

        this.text = text;

        setFont(FontManager.genericFont);

        textRenderColor = UIThemeManager.getDefaultTheme().textColor;
    }
    public TextBox(TextBoxData data){
        super(data);

        this.marginPercX = data.marginPercX;
        this.marginPercY = data.marginPercY;

        horizontalAlignment = Alignment.HorizontalAlignment.valueOf(data.horizontalAlignment);
        verticalAlignment = Alignment.VerticalAlignment.valueOf(data.verticalAlignment);
        wrap = data.wrap;

        this.text = data.text;
        this.textRenderColor = Color.valueOf(data.textColor);

        setFont(FontManager.genericFont);
    }

    /** Update and render */
    @Override
    public void update() {
        super.update();
    }

    @Override
    public void render(SpriteBatch sb) {
        if(!shouldRender()) return;
        super.render(sb);

        font.getData().setScale(fontScale);

        float xMargin = marginPercX * width;
        float yMargin = marginPercY * height;

        int renderX = x + (int) xMargin;
        int renderY = y + (int) yMargin;
        int renderWidth = width - (int) xMargin * 2;
        int renderHeight = height - (int) yMargin * 2;

        renderX += (int)(paddingLeft * Settings.xScale);
        renderY += (int)(paddingBottom * Settings.yScale);
        renderWidth -= paddingLeft;
        renderWidth -= paddingRight;
        renderHeight -= paddingTop;
        renderHeight -= paddingBottom;

        float halfWidth = (float) renderWidth / 2;
        float halfHeight = (float) renderHeight / 2;

        if(!wrap){
            FontHelper.layout.setText(font, "lL");
            if(horizontalAlignment == Alignment.HorizontalAlignment.LEFT){
                if(verticalAlignment == Alignment.VerticalAlignment.TOP){
                    FontHelper.renderFontLeftTopAligned(
                            sb,
                            font,
                            text,
                            renderX * Settings.xScale,
                            (renderY + renderHeight) * Settings.yScale,
                            textRenderColor);
                }
                if(verticalAlignment == Alignment.VerticalAlignment.CENTER){
                    FontHelper.renderFontLeft(
                            sb,
                            font,
                            text,
                            renderX * Settings.xScale,
                            (renderY + halfHeight) * Settings.yScale,
                            textRenderColor);
                }
                if(verticalAlignment == Alignment.VerticalAlignment.BOTTOM){
                    FontHelper.renderFontLeftDownAligned(
                            sb,
                            font,
                            text,
                            renderX * Settings.xScale,
                            (renderY) * Settings.yScale,
                            textRenderColor);
                }
            }
            if(horizontalAlignment == Alignment.HorizontalAlignment.CENTER){
                if(verticalAlignment == Alignment.VerticalAlignment.TOP){
                    FontHelper.renderFontCenteredTopAligned(
                            sb,
                            font,
                            text,
                            (renderX + halfWidth) * Settings.xScale,
                            (renderY + renderHeight) * Settings.yScale - FontHelper.layout.height / 2,
                            textRenderColor);
                }
                if(verticalAlignment == Alignment.VerticalAlignment.CENTER){
                    FontHelper.renderFontCentered(
                            sb,
                            font,
                            text,
                            (renderX + halfWidth) * Settings.xScale,
                            (renderY + halfHeight) * Settings.yScale,
                            textRenderColor);
                }
                if(verticalAlignment == Alignment.VerticalAlignment.BOTTOM){
                    FontHelper.renderFontCentered(
                            sb,
                            font,
                            text,
                            (renderX + halfWidth) * Settings.xScale,
                            (renderY) * Settings.yScale + FontHelper.layout.height / 2,
                            textRenderColor);
                }
            }
            if(horizontalAlignment == Alignment.HorizontalAlignment.RIGHT){
                if(verticalAlignment == Alignment.VerticalAlignment.TOP){
                    FontHelper.renderFontRightTopAligned(
                            sb,
                            font,
                            text,
                            (renderX + renderWidth) * Settings.xScale,
                            (renderY + renderHeight) * Settings.yScale,
                            textRenderColor);
                }
                if(verticalAlignment == Alignment.VerticalAlignment.CENTER){
                    FontHelper.renderFontRightAligned(
                            sb,
                            font,
                            text,
                            (renderX + renderWidth) * Settings.xScale,
                            (renderY + halfHeight) * Settings.yScale,
                            textRenderColor);
                }
                if(verticalAlignment == Alignment.VerticalAlignment.BOTTOM){
                    FontHelper.renderFontRightAligned(
                            sb,
                            font,
                            text,
                            (renderX + renderWidth) * Settings.xScale,
                            (renderY) * Settings.yScale + FontHelper.layout.height / 2,
                            textRenderColor);
                }
            }
        }
        else{
            font.getData().setScale(fontScale);
            font.setColor(textRenderColor);

            int align = 0;
            if(horizontalAlignment == Alignment.HorizontalAlignment.LEFT) align = Align.left;
            else if(horizontalAlignment == Alignment.HorizontalAlignment.CENTER) align = Align.center;
            else if(horizontalAlignment == Alignment.HorizontalAlignment.RIGHT) align = Align.right;

            if(verticalAlignment == Alignment.VerticalAlignment.TOP) renderY += renderHeight;
            else if(verticalAlignment == Alignment.VerticalAlignment.CENTER) renderY += (int) halfHeight;

            FontHelper.layout.setText(font, text, Color.WHITE, renderWidth * Settings.xScale, align, true);
            font.draw(sb, text, renderX * Settings.xScale, (renderY + FontHelper.layout.height / 2f) * Settings.yScale, renderWidth * Settings.xScale, align, true);
            font.getData().setScale(1.0F);
        }

        font.getData().setScale(1.f);

        if(hb != null){
            hb.render(sb);
        }
    }

    /** Text */
    public void setText(String text){
        if(!this.text.equals(text)){
            this.text = text;

            if(IsNonASCII()){
                setFont(FontManager.nonASCIIFont);
            }

            recalculateFontScale();

            onTextChanged(text);
        }
    }
    public String getText(){
        return text;
    }

    public void onTextChanged(String newText){
        if(ModManager.SayTheSpire.isActive()){
            if(getOnTextChangedLine(text) != null){
                Output.text(getOnTextChangedLine(text), true);
            }
        }

        if(onTextChangedConsumer != null) onTextChangedConsumer.accept(newText);
    }
    public TextBox setOnTextChangedConsumer(Consumer<String> consumer){
        onTextChangedConsumer = consumer;
        return this;
    }

    public TextBox setTextRenderColor(Color renderColor){
        textRenderColor = renderColor;
        return this;
    }

    /** Dimensions */
    @Override
    public TextBox setDimensions(Integer newWidth, Integer newHeight) {
        super.setDimensions(newWidth, newHeight);
        recalculateFontScale();
        return this;
    }

    /** Margin */
    public TextBox setMarginPercX(float value){
        marginPercX = value;
        return this;
    }
    public TextBox setMarginPercY(float value){
        marginPercY = value;
        return this;
    }

    /** Padding */
    public TextBox setPadding(int value){
        return setPadding(value, value);
    }
    public TextBox setPadding(int horizontal, int vertical){
        return setPadding(horizontal, vertical, horizontal, vertical);
    }
    public TextBox setPadding(int left, int top, int right, int bottom){
        paddingLeft = left;
        paddingTop = top;
        paddingRight = right;
        paddingBottom = bottom;

        return this;
    }

    /** Getters and Setters */
    public TextBox setHorizontalAlignment(Alignment.HorizontalAlignment alignment){
        this.horizontalAlignment = alignment;
        return this;
    }
    public TextBox setVerticalAlignment(Alignment.VerticalAlignment alignment){
        this.verticalAlignment = alignment;
        return this;
    }

    public TextBox setAlignment(Alignment.HorizontalAlignment horizontalAlignment, Alignment.VerticalAlignment verticalAlignment){
        setHorizontalAlignment(horizontalAlignment);
        setVerticalAlignment(verticalAlignment);
        return this;
    }

    public TextBox setWrap(boolean wrap){
        this.wrap = wrap;
        recalculateFontScale();
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


    /** Say the Spire - Getters and Setters */
    public TextBox setOnTextChangedLine(String newLine) {
        this.onTextChangedLine = newLine;
        return this;
    }
    public String getOnTextChangedLine(String newText){ return this.onTextChangedLine; }

    /** Misc methods */
    protected void recalculateFontScale(){
        float fontScale = 0.1F;

        float xMargin = marginPercX * width;
        float yMargin = marginPercY * height;

        int renderWidth = width - (int) xMargin * 2;
        int renderHeight = height - (int) yMargin * 2;

        renderWidth -= paddingLeft;
        renderWidth -= paddingRight;
        renderHeight -= paddingTop;
        renderHeight -= paddingBottom;

        while(true){
            font.getData().setScale(fontScale);
            FontHelper.layout.setText(font, text, Color.BLACK, renderWidth * Settings.xScale, 0, wrap);
            if(FontHelper.layout.height > renderHeight * Settings.yScale || (!wrap && FontHelper.layout.width > renderWidth * Settings.xScale)) {
                font.getData().setScale(1);
                this.fontScale = Math.max(fontScale - 0.1F, 0.1f);
                return;
            }
            fontScale+=0.1F;
        }
    }
    public boolean IsNonASCII(){
        return this.text != null && !this.text.isEmpty() && !this.text.matches("\\A\\p{ASCII}*\\z");
    }
}