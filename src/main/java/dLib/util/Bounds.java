package dLib.util;

public class Bounds {
    public int left;
    public int top;
    public int right;
    public int bottom;

    public Bounds(int left, int bottom, int right, int top) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    public boolean overlaps(Bounds other){
        if (this.right < other.left || this.left > other.right) {
            return false;
        }

        if (this.top < other.bottom || this.bottom > other.top) {
            return false;
        }

        return true;
    }

    public boolean within(Bounds other){
        return this.left >= other.left && this.right <= other.right && this.top <= other.top && this.bottom >= other.bottom;
    }

    public boolean clip(Bounds mask){
        if (this.left < mask.left) {
            this.left = mask.left;
        }
        if (this.right > mask.right) {
            this.right = mask.right;
        }
        if (this.top > mask.top) {
            this.top = mask.top;
        }
        if (this.bottom < mask.bottom) {
            this.bottom = mask.bottom;
        }
        return this.left < this.right && this.bottom < this.top;
    }
}
