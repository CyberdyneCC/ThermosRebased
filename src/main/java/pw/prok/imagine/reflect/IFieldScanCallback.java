// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine.reflect;

import java.lang.reflect.Field;

public interface IFieldScanCallback<S>
{
    void scanField(final Class<S> p0, final Class<? super S> p1, final Field p2);
}
