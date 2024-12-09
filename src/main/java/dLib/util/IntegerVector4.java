package dLib.util;

import java.io.Serializable;
import java.util.Objects;

public class IntegerVector4 implements Serializable {
    static final long serialVersionUID = 1L;

    public Integer x;
    public Integer y;

    public Integer w;
    public Integer h;

    public IntegerVector4(Integer xIn, Integer yIn, Integer wIn, Integer hIn){
        this.x = xIn;
        this.y = yIn;
        this.w = wIn;
        this.h = hIn;
    }

    public IntegerVector4(IntegerVector4 copy){
        this.x = copy.x;
        this.y = copy.y;
        this.w = copy.w;
        this.h = copy.h;
    }

    public IntegerVector4 overlap(IntegerVector4 other){
        int overlapX = Math.max(x, other.x);
        int overlapY = Math.max(y, other.y);

        int overlapRight = Math.min(x + w, other.x + other.w);
        int overlapBottom = Math.min(y + h, other.y + other.h);

        int overlapW = overlapRight - overlapX;
        int overlapH = overlapBottom - overlapY;

        if (overlapW > 0 && overlapH > 0) {
            return new IntegerVector4(overlapX, overlapY, overlapW, overlapH);
        } else {
            return null; // No overlap
        }
    }
    
    public boolean within(IntegerVector4 other){
        if (x < other.x || y < other.y) {
            return false;
        }

        if (x + w > other.x + other.w || y + h > other.y + other.h) {
            return false;
        }

        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof IntegerVector4)) return false;

        IntegerVector4 other = (IntegerVector4) obj;

        return Objects.equals(other.x, x) && Objects.equals(other.y, y) && Objects.equals(other.w, w) && Objects.equals(other.h, h);
    }

    public IntegerVector4 copy(){
        return new IntegerVector4(x, y, w, h);
    }
}
