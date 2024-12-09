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
        boolean overlapsHorizontally = (this.right <= other.right && this.right >= other.left) || (this.left >= other.left && this.left <= other.right);
        boolean overlapsVertically = (this.top <= other.top && this.top >= other.bottom) || (this.bottom >= other.bottom && this.bottom <= other.top);

        return overlapsHorizontally && overlapsVertically;
    }

    public boolean within(Bounds other){
        return this.left >= other.left && this.right <= other.right && this.top <= other.top && this.bottom >= other.bottom;
    }
}
