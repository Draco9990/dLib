package dLib.util;

import java.util.Objects;

public class IntegerVector2 {
    public Integer x;
    public Integer y;

    public IntegerVector2(int xIn, int yIn){
        this.x = xIn;
        this.y = yIn;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof IntegerVector2)) return false;

        IntegerVector2 other = (IntegerVector2) obj;

        return Objects.equals(other.x, x) && Objects.equals(other.y, y);
    }

    public IntegerVector2 copy(){
        return new IntegerVector2(x, y);
    }
}
