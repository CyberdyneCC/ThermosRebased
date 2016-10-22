// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine.collections;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Collection;
import pw.prok.imagine.util.Array;
import java.util.Iterator;
import java.util.List;

public abstract class AbstractIndirectList<T> implements List<T>, Indirect
{
    private T[] mObjects;
    private int mActualEnd;
    private int mInitialSize;
    private int mLastAvailable;
    
    public AbstractIndirectList() {
        this(100);
    }
    
    public AbstractIndirectList(final int initialSize) {
        this.mInitialSize = initialSize;
        this.clear();
    }
    
    @Override
    public int size() {
        return this.mActualEnd;
    }
    
    @Override
    public boolean isEmpty() {
        return this.mActualEnd == 0;
    }
    
    @Override
    public boolean contains(final Object o) {
        return false;
    }
    
    @Override
    public Iterator<T> iterator() {
        return this.listIterator();
    }
    
    @Override
    public Object[] toArray() {
        return this.mObjects;
    }
    
    @Override
    public <T1> T1[] toArray(T1[] a) {
        if (a.length < this.mActualEnd) {
            a = Array.newArray(a.getClass().getComponentType(), this.mActualEnd);
        }
        System.arraycopy(this.mObjects, 0, a, 0, this.mActualEnd);
        return a;
    }
    
    @Override
    public boolean add(final T t) {
        if (t == null) {
            return false;
        }
        for (int i = this.mLastAvailable; i < this.mActualEnd; ++i) {
            if (this.mObjects[i] == null) {
                this.set(this.mLastAvailable = i, t);
                this.checkEnd(i + 1);
                return true;
            }
        }
        final int index = this.mActualEnd;
        if (index < this.mObjects.length) {
            this.set(this.mLastAvailable = index, t);
            this.checkEnd(index + 1);
            return true;
        }
        for (int j = 0; j < this.mLastAvailable && j < this.mActualEnd; ++j) {
            if (this.mObjects[j] == null) {
                this.set(j, t);
                this.checkEnd(j + 1);
                return true;
            }
        }
        this.grow(t);
        return true;
    }
    
    @Override
    public boolean remove(final Object o) {
        final int index = this.indexOf(o);
        return index >= 0 && this.remove(index) == o;
    }
    
    @Override
    public boolean containsAll(final Collection<?> c) {
        for (final Object o : c) {
            if (!this.contains(o)) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean addAll(final Collection<? extends T> c) {
        if (c == null || c.size() == 0) {
            return false;
        }
        this.grow(this.mObjects.length + c.size());
        boolean changed = false;
        for (final T o : c) {
            changed |= this.add(o);
        }
        return changed;
    }
    
    @Override
    public boolean addAll(final int index, final Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean removeAll(final Collection<?> c) {
        if (c == null || c.size() == 0) {
            return false;
        }
        boolean changed = false;
        for (final Object o : c) {
            changed |= this.remove(o);
        }
        return changed;
    }
    
    @Override
    public boolean retainAll(final Collection<?> c) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void clear() {
        this.mObjects = new Object[this.mInitialSize];
        this.mActualEnd = 0;
        this.mLastAvailable = 0;
    }
    
    @Override
    public T get(final int index) {
        return (T)this.mObjects[index];
    }
    
    @Override
    public T set(final int index, final T element) {
        final T old = (T)this.mObjects[index];
        this.mObjects[index] = element;
        return old;
    }
    
    @Override
    public void add(final int index, final T element) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public T remove(final int index) {
        return this.set(index, null);
    }
    
    @Override
    public int indexOf(final Object o) {
        for (int i = 0; i < this.mActualEnd; ++i) {
            if (this.mObjects[i] == o) {
                return i;
            }
        }
        return -1;
    }
    
    @Override
    public int lastIndexOf(final Object o) {
        for (int i = this.mActualEnd - 1; i >= 0; --i) {
            if (this.mObjects[i] == o) {
                return i;
            }
        }
        return -1;
    }
    
    @Override
    public ListIterator<T> listIterator() {
        return new IndirectIterator();
    }
    
    @Override
    public ListIterator<T> listIterator(final int index) {
        return new IndirectIterator(index);
    }
    
    @Override
    public List<T> subList(final int fromIndex, final int toIndex) {
        final List<T> subList = new ArrayList<T>(toIndex - fromIndex);
        for (int i = fromIndex; i < toIndex; ++i) {
            final T t = (T)this.mObjects[i];
            if (t != null) {
                subList.add(t);
            }
        }
        return subList;
    }
    
    @Override
    public void compat(final boolean trim) {
        int last = -1;
        int count = 0;
        for (int i = 0; i < this.mActualEnd; ++i) {
            final T item = (T)this.mObjects[i];
            if (item == null) {
                if (last == -1) {
                    last = i;
                }
            }
            else {
                ++count;
                if (last != -1) {
                    this.mObjects[last++] = this.mObjects[i];
                    this.mObjects[i] = null;
                }
            }
        }
        this.mActualEnd = count;
        if (trim) {
            this.trim();
        }
    }
    
    @Override
    public void trim() {
        if (this.mActualEnd <= 0) {
            return;
        }
        final T[] newObjects = (T[])new Object[this.mActualEnd];
        System.arraycopy(this.mObjects, 0, newObjects, 0, this.mActualEnd);
        this.mObjects = newObjects;
    }
    
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(this.getClass().getSimpleName());
        builder.append('[');
        for (int i = 0; i < this.mObjects.length; ++i) {
            if (i != 0) {
                builder.append(',');
            }
            builder.append(this.mObjects[i]);
        }
        builder.append(']');
        return builder.toString();
    }
    
    private void grow(final T element) {
        final int currentLength = this.mObjects.length;
        final int newSize = Math.max(currentLength * 3 / 2, 10);
        final T[] newData = (T[])new Object[newSize];
        System.arraycopy(this.mObjects, 0, newData, 0, currentLength);
        newData[currentLength] = element;
        this.mObjects = newData;
        this.checkEnd(currentLength + 1);
    }
    
    private void grow(final int newSize) {
        final int currentLength = this.mObjects.length;
        final T[] newData = (T[])new Object[newSize];
        System.arraycopy(this.mObjects, 0, newData, 0, currentLength);
        this.mObjects = newData;
        this.mLastAvailable = currentLength;
    }
    
    private void checkEnd(final int i) {
        if (i > this.mActualEnd) {
            this.mActualEnd = i;
        }
    }
    
    private final class IndirectIterator implements ListIterator<T>
    {
        int mIndex;
        boolean mIndexSetted;
        
        public IndirectIterator() {
            this.mIndex = -1;
            this.mIndexSetted = false;
        }
        
        public IndirectIterator(final int index) {
            this.mIndex = index;
            this.mIndexSetted = true;
        }
        
        @Override
        public boolean hasNext() {
            this.ensureIndex(true);
            return this.mIndex < AbstractIndirectList.this.mActualEnd;
        }
        
        private void ensureIndex(final boolean forward) {
            if (!this.mIndexSetted) {
                this.mIndex = (forward ? 0 : (AbstractIndirectList.this.mActualEnd - 1));
                this.mIndexSetted = true;
            }
        }
        
        @Override
        public T next() {
            this.ensureIndex(true);
            return (T)AbstractIndirectList.this.mObjects[this.mIndex++];
        }
        
        @Override
        public boolean hasPrevious() {
            this.ensureIndex(false);
            return this.mIndex >= 0;
        }
        
        @Override
        public T previous() {
            this.ensureIndex(false);
            return (T)AbstractIndirectList.this.mObjects[this.mIndex--];
        }
        
        @Override
        public int nextIndex() {
            if (!this.mIndexSetted) {
                return 0;
            }
            return this.mIndex + 1;
        }
        
        @Override
        public int previousIndex() {
            if (!this.mIndexSetted) {
                return AbstractIndirectList.this.mActualEnd - 1;
            }
            return this.mIndex - 1;
        }
        
        @Override
        public void remove() {
            AbstractIndirectList.this.remove(this.mIndex);
        }
        
        @Override
        public void set(final T t) {
            AbstractIndirectList.this.set(this.mIndex, t);
        }
        
        @Override
        public void add(final T t) {
            throw new UnsupportedOperationException();
        }
    }
}
