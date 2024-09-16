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
