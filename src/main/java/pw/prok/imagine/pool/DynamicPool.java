// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine.pool;

import java.util.List;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Lock;
import java.util.Queue;

public class DynamicPool<T> implements Pool<T>
{
    private final PoolFactory<T> mFactory;
    private final Queue<T> mLocked;
    private final Queue<T> mUnlocked;
    private final Queue<T> mObjects;
    private final Lock mLock;
    
    public DynamicPool(final PoolFactory<T> factory) {
        this.mObjects = new ConcurrentLinkedQueue<T>();
        this.mLock = new ReentrantLock(true);
        this.mFactory = factory;
        this.mLocked = new LinkedList<T>();
        this.mUnlocked = new LinkedList<T>();
    }
    
    @Override
    public T obtain() {
        this.mLock.lock();
        try {
            T object = this.mUnlocked.poll();
            if (object == null) {
                object = this.mFactory.create();
                this.mObjects.add(object);
            }
            this.mLocked.add(object);
            return object;
        }
        finally {
            this.mLock.unlock();
        }
    }
    
    @Override
    public void release(final T object) {
        this.mLock.lock();
        try {
            if (!this.mLocked.remove(object)) {
                throw new IllegalStateException("Object not assigned to this pool!");
            }
            this.mUnlocked.add(object);
        }
        finally {
            this.mLock.unlock();
        }
    }
    
    @Override
    public Iterator<T> iterator() {
        this.mLock.lock();
        List<T> objects;
        try {
            objects = new ArrayList<T>((Collection<? extends T>)this.mObjects);
        }
        finally {
            this.mLock.unlock();
        }
        return new Iterator<T>() {
            private final Iterator<T> iterator = objects.iterator();
            
            @Override
            public boolean hasNext() {
                return this.iterator.hasNext();
            }
            
            @Override
            public T next() {
                return this.iterator.next();
            }
            
            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
