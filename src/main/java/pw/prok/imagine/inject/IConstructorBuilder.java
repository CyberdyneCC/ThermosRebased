// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine.inject;

public interface IConstructorBuilder<Z, I extends IConstructorBuilder<Z, I>>
{
    I atLeast(final Class<?> p0);
    
     <T, V extends T> I arg(final Class<T> p0, final V p1);
    
     <T, V extends T> I arg(final int p0, final Class<T> p1, final V p2);
    
     <V> I arg(final V p0);
    
     <V> I arg(final int p0, final V p1);
    
    I arg(final int p0);
    
    I arg(final int p0, final int p1);
    
    Z build();
    
     <V> I args(final V... p0);
    
    Class<? extends Z> clazz();
}
