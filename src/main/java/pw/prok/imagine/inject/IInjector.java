// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine.inject;

public interface IInjector<State extends Injector.InjectorState>
{
    State parseClass(final Class<?> p0);
    
    boolean inject(final State p0, final Object p1, final Object... p2);
    
     <T> IConstructorBuilder<T, ?> create(final Class<T> p0);
    
     <T> IConstructorBuilder<T, ?> create(final String p0);
}
