package dLib.util.consumers;

public interface QuadConsumer <Type1, Type2, Type3, Type4> {
    void accept(Type1 arg1, Type2 arg2, Type3 arg3, Type4 arg4);
}
