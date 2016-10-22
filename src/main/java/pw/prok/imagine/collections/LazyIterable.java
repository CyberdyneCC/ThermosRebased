// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine.collections;

import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;

public class LazyIterable<T> implements Iterable<T>
{
    private final LazyAction<T> mAction;
    private final boolean mOneTime;
    private Iterator<T> mIterator;
    private List<T> mCacheList;
    
    public LazyIterable(final LazyAction<T> action) {
        this(action, true);
    }
    
    public LazyIterable(final LazyAction<T> action, final boolean oneTime) {
        this.mAction = action;
        this.mOneTime = oneTime;
    }
    
    @Override
    public Iterator<T> iterator() {
        if (this.mOneTime) {
            if (this.mIterator == null) {
                this.mIterator = new OneTimeIterator<T>(this.mAction);
            }
            return this.mIterator;
        }
        if (this.mIterator == null) {
            return this.mIterator = new CacheIterator<T>(this.mAction, this.mCacheList = new LinkedList<T>());
        }
        return this.mCacheList.iterator();
    }
    
    private static class OneTimeIterator<T> implements Iterator<T>
    {
        private final LazyAction<T> mAction;
        private T mNext;
        private boolean mEnd;
        
        public OneTimeIterator(final LazyAction<T> action) {
            this.mEnd = false;
            this.mAction = action;
        }
        
        @Override
        public boolean hasNext() {
            if (this.mEnd) {
                return false;
            }
            if (this.mNext != null) {
                return true;
            }
            this.mNext = this.mAction.acquire();
            if (this.mNext == null) {
                this.mEnd = true;
                return false;
            }
            return true;
        }
        
        @Override
        public T next() {
            final T object = this.hasNext() ? this.mNext : null;
            this.mNext = null;
            return object;
        }
        
        @Override
        public void remove() {
        }
    }
    
    private static class CacheIterator<T> extends OneTimeIterator<T>
    {
        private final List<T> mCache;
        private T mPrev;
        
        public CacheIterator(final LazyAction<T> action, final List<T> cache) {
            super(action);
            this.mCache = cache;
        }
        
        @Override
        public T next() {
            final T next = super.next();
            this.mPrev = next;
            final T object = next;
            if (object != null) {
                this.mCache.add(object);
            }
            return object;
        }
        
        @Override
        public void remove() {
            if (this.mPrev == null) {
                throw new IllegalStateException();
            }
            if (this.mCache.remove(this.mCache.size() - 1) != this.mPrev) {
                throw new IllegalStateException();
            }
            this.mPrev = null;
        }
    }
    
    public interface LazyAction<T>
    {
        T acquire();
    }
}
