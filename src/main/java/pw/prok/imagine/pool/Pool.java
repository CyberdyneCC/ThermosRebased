// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine.pool;

public interface Pool<T> extends Iterable<T>
{
    T obtain();
    
    void release(final T p0);
}
