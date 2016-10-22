// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine.pool;

import pw.prok.imagine.inject.Creator;
import pw.prok.imagine.inject.IConstructorBuilder;

public class ReflectionFactory<T> implements PoolFactory<T>
{
    private final IConstructorBuilder<T, ?> mCreator;
    
    public ReflectionFactory(final Class<T> clazz, final Object... args) {
        this.mCreator = Creator.creator(clazz).args(args);
    }
    
    @Override
    public T create() {
        return this.mCreator.build();
    }
}
