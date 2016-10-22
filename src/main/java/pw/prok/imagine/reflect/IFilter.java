// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine.reflect;

import java.lang.reflect.Method;
import java.lang.reflect.Field;

public interface IFilter
{
     <S> FilterResult filterClass(final IScanner p0, final Class<S> p1);
    
     <S> FilterResult filterField(final IScanner p0, final Class<S> p1, final Class<? super S> p2, final Field p3);
    
     <S> FilterResult filterMethod(final IScanner p0, final Class<S> p1, final Class<? super S> p2, final Method p3);
    
    public enum FilterResult
    {
        Default, 
        Accept, 
        Reject;
        
        public static FilterResult present(final boolean b) {
            return b ? FilterResult.Default : FilterResult.Reject;
        }
    }
}
