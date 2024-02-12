package dLib.util;

public class IntVector2 {
    public int x;
    public int y;

    public IntVector2(int xIn, int yIn){
        this.x = xIn;
        this.y = yIn;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof IntVector2)) return false;

        IntVector2 other = (IntVector2) obj;

        return other.x == x && other.y == y;
    }
}
