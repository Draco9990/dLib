package dLib.ui.elements.prefabs;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import dLib.modcompat.ModManager;
import dLib.ui.Alignment;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.implementations.Hoverable;
import dLib.ui.screens.ScreenManager;
import dLib.ui.themes.UIThemeManager;
import dLib.util.FontManager;
import dLib.util.bindings.method.MethodBinding;
import dLib.util.bindings.method.NoneMethodBinding;
import dLib.util.settings.Property;
import sayTheSpire.Output;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.function.Consumer;

public class TextBox extends Hoverable {
    //region Variables

    private String text;

    private Color textRenderColor;
    private BitmapFont font;
    private boolean wrap;

    private Alignment.HorizontalAlignment horizontalAlignment;
    private Alignment.VerticalAlignment verticalAlignment;

    private String onTextChangedLine;

    private float marginPercX = 0.0f;
    private float marginPercY = 0.0f;

    private ArrayList<Consumer<String>> onTextChangedConsumers = new ArrayList<>();

    private int paddingLeft = 0;
    private int paddingTop = 0;
    private int paddingRight = 0;
    private int paddingBottom = 0;

    private Hitbox textRenderHitbox;

    //endregion

    //region Constructors

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

        this.text = data.text;

        this.textRenderColor = Color.valueOf(data.textRenderColor);
        //TODO FONT
        this.wrap = data.wrap;

        horizontalAlignment = Alignment.HorizontalAlignment.valueOf(data.horizontalAlignment);
        verticalAlignment = Alignment.VerticalAlignment.valueOf(data.verticalAlignment);

        this.marginPercX = data.marginPercX;
        this.marginPercY = data.marginPercY;

        onTextChangedConsumers.add(s -> data.onTextChanged.executeBinding(ScreenManager.getCurrentScreen()));

        this.paddingRight = data.paddingRight;
        this.paddingTop = data.paddingTop;
        this.paddingLeft = data.paddingLeft;
        this.paddingBottom = data.paddingBottom;

        setFont(FontManager.genericFont);
    }

    //endregion

    //region Methods

    //region Update & Render

    @Override
    public void renderSelf(SpriteBatch sb) {
        super.renderSelf(sb);

        float fontScale = calculateFontScale();
        font.getData().setScale(fontScale);

        float xMargin = marginPercX * getWidth();
        float yMargin = marginPercY * getHeight();

        int renderX = getWorldPositionX() + (int) xMargin + (int)(paddingLeft * Settings.xScale);
        int renderY = getWorldPositionY() + (int) yMargin + (int)(paddingBottom * Settings.yScale);
        int renderWidth = getWidth() - (int) xMargin * 2 - paddingLeft - paddingRight;
        int renderHeight = getHeight() - (int) yMargin * 2 - paddingTop - paddingBottom;

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

    //endregion

    //region Text

    public void setText(String text){
        if(!this.text.equals(text)){
            this.text = text;

            if(containsNonASCIICharacters()){
                setFont(FontManager.nonASCIIFont);
            }

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

        for(Consumer<String> consumer : onTextChangedConsumers) consumer.accept(newText);
    }
    public TextBox addOnTextChangedConsumer(Consumer<String> consumer){
        onTextChangedConsumers.add(consumer);
        return this;
    }

    public TextBox setOnTextChangedLine(String newLine) {
        this.onTextChangedLine = newLine;
        return this;
    }
    public String getOnTextChangedLine(String newText){ return this.onTextChangedLine; }

    //endregion

    //region Text Render Color

    public TextBox setTextRenderColor(Color renderColor){
        textRenderColor = renderColor;
        return this;
    }

    public Color getTextRenderColor(){
        return textRenderColor;
    }

    //endregion

    //region Text Margin

    public TextBox setMarginPercX(float value){
        marginPercX = value;
        return this;
    }
    public TextBox setMarginPercY(float value){
        marginPercY = value;
        return this;
    }

    //endregion

    //region Text Padding

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

    //endregion

    //region Text Alignment

    public TextBox setHorizontalAlignment(Alignment.HorizontalAlignment alignment){
        this.horizontalAlignment = alignment;
        return this;
    }
    public Alignment.HorizontalAlignment getHorizontalAlignment(){
        return horizontalAlignment;
    }

    public TextBox setVerticalAlignment(Alignment.VerticalAlignment alignment){
        this.verticalAlignment = alignment;
        return this;
    }
    public Alignment.VerticalAlignment getVerticalAlignment(){
        return verticalAlignment;
    }

    public TextBox setAlignment(Alignment.HorizontalAlignment horizontalAlignment, Alignment.VerticalAlignment verticalAlignment){
        setHorizontalAlignment(horizontalAlignment);
        setVerticalAlignment(verticalAlignment);
        return this;
    }

    //endregion

    //region Text Wrap

    public TextBox setWrap(boolean wrap){
        this.wrap = wrap;
        return this;
    }

    public boolean getWrap(){
        return wrap;
    }

    //endregion

    //region Text Font

    public TextBox setFont(BitmapFont font){
        this.font = font;
        return this;
    }

    //endregion

    protected float calculateFontScale(){
        float fontScale = 0.1F;

        float xMargin = marginPercX * getWidth();
        float yMargin = marginPercY * getHeight();

        int renderWidth = getWidth() - (int) xMargin * 2;
        int renderHeight = getHeight() - (int) yMargin * 2;

        renderWidth -= paddingLeft;
        renderWidth -= paddingRight;
        renderHeight -= paddingTop;
        renderHeight -= paddingBottom;

        while(true){
            font.getData().setScale(fontScale);
            FontHelper.layout.setText(font, text, Color.BLACK, renderWidth * Settings.xScale, 0, wrap);
            if(FontHelper.layout.height > renderHeight * Settings.yScale || (!wrap && FontHelper.layout.width > renderWidth * Settings.xScale)) {
                font.getData().setScale(1);
                return Math.max(fontScale - 0.1F, 0.1f);
            }
            fontScale+=0.1F;
        }
    }
    public boolean containsNonASCIICharacters(){
        return this.text != null && !this.text.isEmpty() && !this.text.matches("\\A\\p{ASCII}*\\z");
    }

    //endregion

    public static class TextBoxData extends Hoverable.HoverableData implements Serializable {
        private static final long serialVersionUID = 1L;

        public String text = "";

        public String textRenderColor = Color.WHITE.toString();
        //TODO FONT
        public boolean wrap;

        public String horizontalAlignment = Alignment.HorizontalAlignment.CENTER.name();
        public String verticalAlignment = Alignment.VerticalAlignment.CENTER.name();

        public float marginPercX = 0.07f;
        public float marginPercY = 0.33f;

        public MethodBinding onTextChanged = new NoneMethodBinding();

        public int paddingRight = 0;
        public int paddingTop = 0;
        public int paddingLeft = 0;
        public int paddingBottom = 0;

        @Override
        public TextBox makeUIElement() {
            return new TextBox(this);
        }
    }
}