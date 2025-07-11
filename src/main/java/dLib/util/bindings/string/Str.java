package dLib.util.bindings.string;

import dLib.util.bindings.string.interfaces.ITextProvider;

import java.util.function.Supplier;

public class Str {
    public static StringStaticBinding stat(String text) {
        return new StringStaticBinding(text);
    }

    public static StringSourceBinding src(ITextProvider provider) {
        return new StringSourceBinding(provider);
    }

    public static StringLambdaBinding lambda(Supplier<String> provider) {
        return new StringLambdaBinding(provider);
    }
}
