// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine.pool;

public class Pools
{
    public static <T> Pool<T> create(final int initialCount, final int maxCount, final PoolFactory<T> factory) {
        return new FixedPool<T>(factory, initialCount, maxCount);
    }
    
    public static <T> Pool<T> create(final int initialCount, final int maxCount, final Class<T> clazz, final Object... args) {
        return new FixedPool<T>(new ReflectionFactory<T>(clazz, args), initialCount, maxCount);
    }
    
    public static <T> Pool<T> create(final int count, final PoolFactory<T> factory) {
        return new FixedPool<T>(factory, count);
    }
    
    public static <T> Pool<T> create(final int count, final Class<T> clazz, final Object... args) {
        return new FixedPool<T>(new ReflectionFactory<T>(clazz, args), count);
    }
    
    public static <T> Pool<T> create(final PoolFactory<T> factory) {
        return new DynamicPool<T>(factory);
    }
    
    public static <T> Pool<T> create(final Class<T> clazz, final Object... args) {
        return new DynamicPool<T>(new ReflectionFactory<T>(clazz, args));
    }
}
