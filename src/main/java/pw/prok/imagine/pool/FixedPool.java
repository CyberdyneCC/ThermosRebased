// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine.pool;

import java.util.Iterator;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;
import java.util.BitSet;

public class FixedPool<T> implements Pool<T>
{
    private final T[] mPool;
    private final PoolFactory<T> mFactory;
    private final BitSet mStateBits;
    private final int mMaxCount;
    private int mCount;
    private int mLastPos;
    private volatile Lock mLock;
    
    public FixedPool(final PoolFactory<T> factory, final int count) {
        this(factory, count, count);
    }
    
    public FixedPool(final PoolFactory<T> factory, final int initialCount, final int maxCount) {
        this.mStateBits = new BitSet();
        this.mCount = 0;
        this.mLastPos = 0;
        this.mLock = new ReentrantLock(true);
        this.mFactory = factory;
        this.mMaxCount = maxCount;
        this.mPool = new Object[maxCount];
        this.mStateBits.clear(0, maxCount);
        this.grow(initialCount);
    }
    
    private void grow(int count) {
        if (count > this.mMaxCount) {
            count = this.mMaxCount;
        }
        for (int i = this.mCount; i < count; ++i) {
            final T object = this.mFactory.create();
            assert object != null;
            this.mPool[i] = object;
        }
        this.mCount = count;
    }
    
    @Override
    public T obtain() {
        this.mLock.lock();
        try {
            for (int i = this.mLastPos; i < this.mMaxCount; ++i) {
                if (!this.mStateBits.get(i)) {
                    this.mStateBits.set(this.mLastPos = i);
                    return (T)this.mPool[i];
                }
            }
            for (int i = 0; i < this.mLastPos; ++i) {
                if (!this.mStateBits.get(i)) {
                    this.mStateBits.set(this.mLastPos = i);
                    return (T)this.mPool[i];
                }
            }
            if (this.mCount < this.mMaxCount) {
                final int index = this.mCount;
                this.grow(this.mCount + 1);
                this.mStateBits.set(index);
                return (T)this.mPool[index];
            }
            throw new IllegalStateException("No available objects in pool!");
        }
        finally {
            this.mLock.unlock();
        }
    }
    
    @Override
    public void release(final T object) {
        assert object != null;
        this.mLock.lock();
        try {
            for (int i = this.mLastPos; i < this.mMaxCount; ++i) {
                if (this.mPool[i] == object) {
                    this.mStateBits.clear(i);
                    return;
                }
            }
            for (int i = this.mLastPos; i >= 0; --i) {
                if (this.mPool[i] == object) {
                    this.mStateBits.clear(i);
                    return;
                }
            }
            throw new IllegalStateException("Object not assigned to this pool: " + object);
        }
        finally {
            this.mLock.unlock();
        }
    }
    
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            int index = 0;
            
            @Override
            public boolean hasNext() {
                return this.index < FixedPool.this.mMaxCount;
            }
            
            @Override
            public T next() {
                return (T)FixedPool.this.mPool[this.index++];
            }
            
            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
