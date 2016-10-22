// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine.reflect;

import java.lang.reflect.Method;
import java.lang.reflect.Field;

public class GetSetMethodFilter implements IFilter
{
    private final boolean mGet;
    private final boolean mSet;
    
    public GetSetMethodFilter(final boolean get, final boolean set) {
        this.mGet = get;
        this.mSet = set;
    }
    
    @Override
    public <S> FilterResult filterClass(final IScanner scanner, final Class<S> mainClass) {
        return FilterResult.Default;
    }
    
    @Override
    public <S> FilterResult filterField(final IScanner scanner, final Class<S> mainClass, final Class<? super S> childClass, final Field field) {
        return FilterResult.Default;
    }
    
    @Override
    public <S> FilterResult filterMethod(final IScanner scanner, final Class<S> mainClass, final Class<? super S> childClass, final Method method) {
        final String name = method.getName();
        if (name.length() <= 4) {
            return FilterResult.Reject;
        }
        if (this.mGet && name.startsWith("get")) {
            return FilterResult.Default;
        }
        if (this.mSet && name.startsWith("set")) {
            return FilterResult.Default;
        }
        return FilterResult.Reject;
    }
    
    public static boolean isGetMethod(final Method method) {
        return method.getName().startsWith("get");
    }
    
    public static boolean isSetMethod(final Method method) {
        return method.getName().startsWith("set");
    }
    
    public static String getVarName(final Method method) {
        final char[] chars = method.getName().substring(3).toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);
        return new String(chars);
    }
}
