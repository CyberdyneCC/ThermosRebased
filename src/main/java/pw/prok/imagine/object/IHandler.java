// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine.object;

public interface IHandler<T>
{
    void putData(final Object p0, final T p1, final Object p2) throws Exception;
    
    Object getData(final Object p0, final T p1) throws Exception;
}
