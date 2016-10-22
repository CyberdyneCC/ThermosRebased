// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine.reflect;

import java.lang.reflect.Method;

public interface IMethodScanCallback<S>
{
    void scanMethod(final Class<S> p0, final Class<? super S> p1, final Method p2);
}
