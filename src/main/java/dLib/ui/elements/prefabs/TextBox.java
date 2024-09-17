package dLib.ui.elements.prefabs;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import dLib.modcompat.ModManager;
import dLib.properties.objects.*;
import dLib.properties.ui.elements.OnValueChangedStringPropertyEditor;
import dLib.ui.Alignment;
import dLib.ui.elements.implementations.Hoverable;
import dLib.ui.screens.ScreenManager;
import dLib.ui.themes.UIThemeManager;
import dLib.util.FontManager;
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

    private Alignment alignment;

    private String onTextChangedLine;

    private float marginPercX = 0.0f;
    private float marginPercY = 0.0f;

    private ArrayList<Consumer<String>> onTextChangedConsumers = new ArrayList<>();

    private int paddingLeft = 0;
    private int paddingTop = 0;
    private int paddingRight = 0;
    private int paddingBottom = 0;

    private Hitbox textRenderHitbox;

    private float minFontScale = 0.0f;
    private float fontScaleOverride = 0.0f;
    private float maxFontScale = 0.0f;

    //endregion

    //region Constructors

    public TextBox(String text, int xPos, int yPos, int width, int height){
        this(text, xPos, yPos, width, height, 0.07f, 0.33f);
    }
    public TextBox(String text, int xPos, int yPos, int width, int height, float xMarginPerc, float yMarginPerc){
        super(null, xPos, yPos, width, height);

        this.marginPercX = xMarginPerc;
        this.marginPercY = yMarginPerc;

        alignment = new Alignment(Alignment.HorizontalAlignment.CENTER, Alignment.VerticalAlignment.CENTER);
        wrap = false;

        this.text = text;

        setFont(FontManager.genericFont);

        textRenderColor = UIThemeManager.getDefaultTheme().textColor;
    }

    public TextBox(TextBoxData data){
        super(data);

        this.text = data.text.getValue();

        this.textRenderColor = Color.valueOf(data.textRenderColor.getValue());
        //TODO FONT
        this.wrap = data.wrap.getValue();

        alignment = new Alignment(data.alignment.getValue());

        this.marginPercX = data.marginPercX;
        this.marginPercY = data.marginPercY;

        onTextChangedConsumers.add(s -> data.onTextChanged.getValue().executeBinding(ScreenManager.getCurrentScreen()));

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

        if(text == null || text.isEmpty()) return;

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
            FontHelper.layout.setText(getFontForRender(), "lL");
            if(alignment.horizontalAlignment == Alignment.HorizontalAlignment.LEFT){
                if(alignment.verticalAlignment == Alignment.VerticalAlignment.TOP){
                    FontHelper.renderFontLeftTopAligned(
                            sb,
                            getFontForRender(),
                            text,
                            renderX * Settings.xScale,
                            (renderY + renderHeight) * Settings.yScale,
                            textRenderColor);
                }
                if(alignment.verticalAlignment == Alignment.VerticalAlignment.CENTER){
                    FontHelper.renderFontLeft(
                            sb,
                            getFontForRender(),
                            text,
                            renderX * Settings.xScale,
                            (renderY + halfHeight) * Settings.yScale,
                            textRenderColor);
                }
                if(alignment.verticalAlignment == Alignment.VerticalAlignment.BOTTOM){
                    FontHelper.renderFontLeftDownAligned(
                            sb,
                            getFontForRender(),
                            text,
                            renderX * Settings.xScale,
                            (renderY) * Settings.yScale,
                            textRenderColor);
                }
            }
            if(alignment.horizontalAlignment == Alignment.HorizontalAlignment.CENTER){
                if(alignment.verticalAlignment == Alignment.VerticalAlignment.TOP){
                    FontHelper.renderFontCenteredTopAligned(
                            sb,
                            getFontForRender(),
                            text,
                            (renderX + halfWidth) * Settings.xScale,
                            (renderY + renderHeight) * Settings.yScale - FontHelper.layout.height / 2,
                            textRenderColor);
                }
                if(alignment.verticalAlignment == Alignment.VerticalAlignment.CENTER){
                    FontHelper.renderFontCentered(
                            sb,
                            getFontForRender(),
                            text,
                            (renderX + halfWidth) * Settings.xScale,
                            (renderY + halfHeight) * Settings.yScale,
                            textRenderColor);
                }
                if(alignment.verticalAlignment == Alignment.VerticalAlignment.BOTTOM){
                    FontHelper.renderFontCentered(
                            sb,
                            getFontForRender(),
                            text,
                            (renderX + halfWidth) * Settings.xScale,
                            (renderY) * Settings.yScale + FontHelper.layout.height / 2,
                            textRenderColor);
                }
            }
            if(alignment.horizontalAlignment == Alignment.HorizontalAlignment.RIGHT){
                if(alignment.verticalAlignment == Alignment.VerticalAlignment.TOP){
                    FontHelper.renderFontRightTopAligned(
                            sb,
                            getFontForRender(),
                            text,
                            (renderX + renderWidth) * Settings.xScale,
                            (renderY + renderHeight) * Settings.yScale,
                            textRenderColor);
                }
                if(alignment.verticalAlignment == Alignment.VerticalAlignment.CENTER){
                    FontHelper.renderFontRightAligned(
                            sb,
                            getFontForRender(),
                            text,
                            (renderX + renderWidth) * Settings.xScale,
                            (renderY + halfHeight) * Settings.yScale,
                            textRenderColor);
                }
                if(alignment.verticalAlignment == Alignment.VerticalAlignment.BOTTOM){
                    FontHelper.renderFontRightAligned(
                            sb,
                            getFontForRender(),
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
            if(alignment.horizontalAlignment == Alignment.HorizontalAlignment.LEFT) align = Align.left;
            else if(alignment.horizontalAlignment == Alignment.HorizontalAlignment.CENTER) align = Align.center;
            else if(alignment.horizontalAlignment == Alignment.HorizontalAlignment.RIGHT) align = Align.right;

            if(alignment.verticalAlignment == Alignment.VerticalAlignment.TOP) renderY += (int) halfHeight;
            else if(alignment.verticalAlignment == Alignment.VerticalAlignment.BOTTOM) renderY -= (int) halfHeight;

            FontHelper.layout.setText(getFontForRender(), text, Color.WHITE, renderWidth * Settings.xScale, align, true);
            font.draw(sb, text, renderX * Settings.xScale, (renderY + FontHelper.layout.height / 2f) * Settings.yScale, renderWidth * Settings.xScale, align, true);
            font.getData().setScale(1.0F);
        }

        sb.flush(); //* We have to flush after drawing because ScissorStack only applies to the last drawn elements for some reason

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

    public BitmapFont getFontForRender(){
        if(containsNonASCIICharacters()){
            return FontManager.nonASCIIFont;
        }
        else{
            return font;
        }
    }

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
        this.alignment.horizontalAlignment = alignment;
        return this;
    }
    public Alignment.HorizontalAlignment getHorizontalAlignment(){
        return this.alignment.horizontalAlignment;
    }

    public TextBox setVerticalAlignment(Alignment.VerticalAlignment alignment){
        this.alignment.verticalAlignment = alignment;
        return this;
    }
    public Alignment.VerticalAlignment getVerticalAlignment(){
        return alignment.verticalAlignment;
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
        if(text == null || text.isEmpty()) return 0.1f;

        float fontScale = 0.1F;

        float xMargin = marginPercX * getWidth();
        float yMargin = marginPercY * getHeight();

        int renderWidth = getWidth() - (int) xMargin * 2;
        int renderHeight = getHeight() - (int) yMargin * 2;

        renderWidth -= paddingLeft;
        renderWidth -= paddingRight;
        renderHeight -= paddingTop;
        renderHeight -= paddingBottom;

        if(fontScaleOverride > 0.0f){
            return fontScaleOverride;
        }

        while(true){
            font.getData().setScale(fontScale);
            FontHelper.layout.setText(getFontForRender(), text, Color.BLACK, renderWidth * Settings.xScale, 0, wrap);
            if(FontHelper.layout.height > renderHeight * Settings.yScale || (!wrap && FontHelper.layout.width > renderWidth * Settings.xScale)) {
                font.getData().setScale(1);
                float calculatedScale = Math.max(fontScale - 0.1F, 0.1f);

                if(minFontScale > 0.0f){
                    calculatedScale = Math.max(calculatedScale, minFontScale);
                }
                if(maxFontScale > 0.0f){
                    calculatedScale = Math.min(calculatedScale, maxFontScale);
                }

                return calculatedScale;
            }
            fontScale+=0.1F;
        }
    }
    public boolean containsNonASCIICharacters(){
        return this.text != null && !this.text.isEmpty() && !this.text.matches("\\A\\p{ASCII}*\\z");
    }

    public TextBox setFontScaleOverride(float fontScaleOverride){
        this.fontScaleOverride = fontScaleOverride;
        return this;
    }

    public TextBox setMinFontScale(float minFontScale){
        this.minFontScale = minFontScale;
        return this;
    }

    public TextBox setMaxFontScale(float maxFontScale){
        this.maxFontScale = maxFontScale;
        return this;
    }

    //endregion

    public static class TextBoxData extends Hoverable.HoverableData implements Serializable {
        private static final long serialVersionUID = 1L;

        public StringProperty text = (StringProperty) new StringProperty("TEXT").setName("Text").setPropertyEditorClass(OnValueChangedStringPropertyEditor.class);

        public ColorProperty textRenderColor = (ColorProperty) new ColorProperty(Color.WHITE).setName("Render Color");
        //TODO FONT
        public BooleanProperty wrap = new BooleanProperty(false).setName("Wrap");

        public AlignmentProperty alignment = (AlignmentProperty) new AlignmentProperty(new Alignment(Alignment.HorizontalAlignment.CENTER, Alignment.VerticalAlignment.CENTER)).setName("Alignment");

        public float marginPercX = 0.07f; //TODO Propertize
        public float marginPercY = 0.33f;

        public MethodBindingProperty onTextChanged = new MethodBindingProperty().setName("On Text Changed");

        public int paddingRight = 0;
        public int paddingTop = 0;
        public int paddingLeft = 0;
        public int paddingBottom = 0;

        public float fontScaleOverride = 0.0f;

        @Override
        public TextBox makeUIElement() {
            return new TextBox(this);
        }
    }
}