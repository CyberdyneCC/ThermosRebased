// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine.api;

public interface IBuilder<B extends IBuilder<B, T>, T extends ICopyable<T>> extends ICopyable<B>
{
    T build();
}
