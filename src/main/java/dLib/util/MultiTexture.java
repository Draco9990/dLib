package dLib.util;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MultiTexture extends Texture {
    /** Variables */
    private Texture topLeft;
    private Texture top;
    private Texture topRight;

    private Texture left;
    private Texture right;

    private Texture bottomLeft;
    private Texture bottom;
    private Texture bottomRight;

    /** Constructors */
    public MultiTexture(String multiTexturePath){
        super(multiTexturePath + "center.png");

        topLeft = TextureManager.getTexture("topLeft.png");
        top = TextureManager.getTexture("top.png");
        topRight = TextureManager.getTexture("topRight.png");

        left = TextureManager.getTexture("left.png");
        right = TextureManager.getTexture("right.png");

        bottomLeft = TextureManager.getTexture("bottomLeft.png");
        bottom = TextureManager.getTexture("bottom.png");
        bottomRight = TextureManager.getTexture("bottomRight.png");
    }

    @Override
    public int getWidth() {
        float totalSideWidth = (left != null ? left.getWidth() : 0) + (right != null ? right.getWidth() : 0);
        float topLeftWidth = (topLeft != null ? topLeft.getWidth() : 0);
        float topRightWidth = (topRight != null ? topRight.getWidth() : 0);
        float bottomLeftWidth = (bottomLeft != null ? bottomLeft.getWidth() : 0);
        float bottomRightWidth = (bottomRight != null ? bottomRight.getWidth() : 0);

        float width = Math.max(topLeftWidth + topRightWidth, bottomLeftWidth + bottomRightWidth);
        width = Math.max(width, totalSideWidth);

        return (int) (super.getWidth() + width);
    }

    @Override
    public int getHeight() {
        float totalSideHeight = (left != null ? left.getHeight() : 0) + (right != null ? right.getHeight() : 0);
        float topLeftHeight = (topLeft != null ? topLeft.getHeight() : 0);
        float topRightHeight = (topRight != null ? topRight.getHeight() : 0);
        float bottomLeftHeight = (bottomLeft != null ? bottomLeft.getHeight() : 0);
        float bottomRightHeight = (bottomRight != null ? bottomRight.getHeight() : 0);

        float height = Math.max(topLeftHeight + topRightHeight, bottomLeftHeight + bottomRightHeight);
        height = Math.max(height, totalSideHeight);

        return (int) (super.getHeight() + height);
    }

    /** Render */
    public void draw(SpriteBatch sb, float xPos, float yPos, float width, float height){
        // Calculate the total width and height of the sides
        float totalSideWidth = getWidth();
        float totalSideHeight = getHeight();

        // If the provided width or height is smaller than the total width or height of the sides, scale down the corners individually
        float topLeftWidth = (topLeft != null ? topLeft.getWidth() : 0);
        float topLeftHeight = (topLeft != null ? topLeft.getHeight() : 0);
        float topRightWidth = (topRight != null ? topRight.getWidth() : 0);
        float topRightHeight = (topRight != null ? topRight.getHeight() : 0);
        float bottomLeftWidth = (bottomLeft != null ? bottomLeft.getWidth() : 0);
        float bottomLeftHeight = (bottomLeft != null ? bottomLeft.getHeight() : 0);
        float bottomRightWidth = (bottomRight != null ? bottomRight.getWidth() : 0);
        float bottomRightHeight = (bottomRight != null ? bottomRight.getHeight() : 0);

        if (width < totalSideWidth) {
            float excessWidth = totalSideWidth - width;
            topLeftWidth -= excessWidth * topLeftWidth / totalSideWidth;
            topRightWidth -= excessWidth * topRightWidth / totalSideWidth;
            bottomLeftWidth -= excessWidth * bottomLeftWidth / totalSideWidth;
            bottomRightWidth -= excessWidth * bottomRightWidth / totalSideWidth;
        }
        if (height < totalSideHeight) {
            float excessHeight = totalSideHeight - height;
            topLeftHeight -= excessHeight * topLeftHeight / totalSideHeight;
            topRightHeight -= excessHeight * topRightHeight / totalSideHeight;
            bottomLeftHeight -= excessHeight * bottomLeftHeight / totalSideHeight;
            bottomRightHeight -= excessHeight * bottomRightHeight / totalSideHeight;
        }

        // Calculate the size of the center texture
        float centerWidth = width - totalSideWidth;
        float centerHeight = height - totalSideHeight;

        // Draw the center texture
        sb.draw(this, xPos + (left != null ? left.getWidth() : 0), yPos + (bottom != null ? bottom.getHeight() : 0), centerWidth, centerHeight);

        // Draw the sides
        if (left != null) sb.draw(left, xPos, yPos + bottomLeftHeight, left.getWidth(), centerHeight);
        if (right != null) sb.draw(right, xPos + width - right.getWidth(), yPos + bottomRightHeight, right.getWidth(), centerHeight);
        if (top != null) sb.draw(top, xPos + topLeftWidth, yPos + height - top.getHeight(), centerWidth, top.getHeight());
        if (bottom != null) sb.draw(bottom, xPos + bottomLeftWidth, yPos, centerWidth, bottom.getHeight());

        // Draw the corners
        if (topLeft != null) sb.draw(topLeft, xPos, yPos + height - topLeftHeight, topLeftWidth, topLeftHeight);
        else if (top != null) sb.draw(top, xPos, yPos + height - top.getHeight(), top.getWidth(), top.getHeight());

        if (topRight != null) sb.draw(topRight, xPos + width - topRightWidth, yPos + height - topRightHeight, topRightWidth, topRightHeight);
        else if (top != null) sb.draw(top, xPos + width - top.getWidth(), yPos + height - top.getHeight(), top.getWidth(), top.getHeight());

        if (bottomLeft != null) sb.draw(bottomLeft, xPos, yPos, bottomLeftWidth, bottomLeftHeight);
        else if (bottom != null) sb.draw(bottom, xPos, yPos, bottom.getWidth(), bottom.getHeight());

        if (bottomRight != null) sb.draw(bottomRight, xPos + width - bottomRightWidth, yPos, bottomRightWidth, bottomRightHeight);
        else if (bottom != null) sb.draw(bottom, xPos + width - bottom.getWidth(), yPos, bottom.getWidth(), bottom.getHeight());
    }
}
