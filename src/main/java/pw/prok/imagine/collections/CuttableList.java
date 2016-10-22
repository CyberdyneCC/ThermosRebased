// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine.collections;

import pw.prok.imagine.util.Array;
import java.util.ListIterator;
import java.util.Iterator;
import java.util.Collection;
import java.util.List;

public class CuttableList<T> implements List<T>
{
    private final List<T> mList;
    private int mStartOffset;
    private int mEndOffset;
    
    public CuttableList(final List<T> list) {
        this.mList = list;
        this.mStartOffset = 0;
        this.mEndOffset = 0;
    }
    
    public CuttableList(final List<T> list, final int startOffset, final int endOffset) {
        assert startOffset >= 0 && endOffset >= 0;
        this.mList = list;
        this.mStartOffset = startOffset;
        this.mEndOffset = endOffset;
    }
    
    @Override
    public int size() {
        return Math.max(this.mList.size() - this.mStartOffset - this.mEndOffset, 0);
    }
    
    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }
    
    public int startOffset() {
        return this.mStartOffset;
    }
    
    public int startOffset(final int startOffset) {
        assert startOffset >= 0;
        final int oldStartOffset = this.mStartOffset;
        this.mStartOffset = startOffset;
        return oldStartOffset;
    }
    
    public int endOffset() {
        return this.mEndOffset;
    }
    
    public int endOffset(final int endOffset) {
        assert endOffset >= 0;
        final int oldEndOffset = this.mEndOffset;
        this.mEndOffset = endOffset;
        return oldEndOffset;
    }
    
    @Override
    public T get(final int index) {
        if (this.valid(index)) {
            return this.mList.get(this.privateIndex(index));
        }
        throw new IndexOutOfBoundsException("Index out of bounds: " + index + ", size: " + this.size());
    }
    
    @Override
    public T set(final int index, final T element) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void add(final int index, final T element) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean remove(final Object o) {
        throw new UnsupportedOperationException();
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
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean addAll(final int index, final Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean removeAll(final Collection<?> c) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean retainAll(final Collection<?> c) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public T remove(final int index) {
        throw new UnsupportedOperationException();
    }
    
    private boolean valid(final int index) {
        return index >= 0 && index < this.size();
    }
    
    private int privateIndex(final int index) {
        return index + this.mStartOffset;
    }
    
    private int publicIndex(final int index) {
        return index - this.mStartOffset;
    }
    
    @Override
    public boolean contains(final Object o) {
        return this.valid(this.indexOf(o));
    }
    
    @Override
    public int indexOf(final Object o) {
        int index = 0;
        final ListIterator<T> iterator = this.listIterator();
        while (iterator.hasNext()) {
            if (iterator.next() == o) {
                return index;
            }
            ++index;
        }
        return -1;
    }
    
    @Override
    public int lastIndexOf(final Object o) {
        int index = 0;
        final ListIterator<T> iterator = this.listIterator();
        while (iterator.hasPrevious()) {
            if (iterator.previous() == o) {
                return this.size() - index - 1;
            }
            ++index;
        }
        return -1;
    }
    
    @Override
    public Iterator<T> iterator() {
        return this.listIterator();
    }
    
    @Override
    public Object[] toArray() {
        return this.toArray(new Object[this.size()]);
    }
    
    @Override
    public <T1> T1[] toArray(T1[] a) {
        final int size = this.size();
        if (size > a.length) {
            a = Array.newArray(a.getClass().getComponentType(), size);
        }
        int i = 0;
        for (final Object o : this) {
            a[i++] = (T1)o;
        }
        return a;
    }
    
    @Override
    public boolean add(final T t) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public ListIterator<T> listIterator() {
        return this.listIterator(0);
    }
    
    @Override
    public ListIterator<T> listIterator(final int index) {
        return new ListIterator<T>() {
            private final ListIterator<T> iterator = CuttableList.this.mList.listIterator(index);
            private final int size = CuttableList.this.size();
            private int nextIndex = 0;
            private int previousIndex = this.size - 1;
            
            @Override
            public boolean hasNext() {
                return this.nextIndex < this.size - 1 && this.iterator.hasNext();
            }
            
            @Override
            public T next() {
                if (!this.hasNext()) {
                    return null;
                }
                ++this.nextIndex;
                return this.iterator.next();
            }
            
            @Override
            public boolean hasPrevious() {
                return this.previousIndex > 0 && this.iterator.hasPrevious();
            }
            
            @Override
            public T previous() {
                if (!this.hasPrevious()) {
                    return null;
                }
                --this.previousIndex;
                return this.iterator.previous();
            }
            
            @Override
            public int nextIndex() {
                return this.nextIndex;
            }
            
            @Override
            public int previousIndex() {
                return this.previousIndex;
            }
            
            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
            
            @Override
            public void set(final T t) {
                throw new UnsupportedOperationException();
            }
            
            @Override
            public void add(final T t) {
                throw new UnsupportedOperationException();
            }
        };
    }
    
    @Override
    public List<T> subList(final int fromIndex, final int toIndex) {
        return new CuttableList((List<Object>)this.mList, this.privateIndex(fromIndex), this.privateIndex(this.size() - toIndex));
    }
}
