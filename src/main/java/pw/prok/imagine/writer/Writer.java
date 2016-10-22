// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine.writer;

import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;
import java.util.ListIterator;
import com.google.common.collect.Lists;
import java.util.List;
import pw.prok.imagine.api.ICopyable;

public class Writer
{
    public static <T extends ICopyable<T>> T copy(final T t) {
        return (T)((t != null) ? t.copy() : null);
    }
    
    public static <T extends ICopyable<T>> List<T> copyList(final List<T> list) {
        if (list == null) {
            return null;
        }
        final List<T> result = (List<T>)Lists.newLinkedList((Iterable)list);
        copyContent(result);
        return result;
    }
    
    public static <T extends ICopyable<T>> void copyContent(final List<T> list) {
        final ListIterator<T> iterator = list.listIterator();
        while (iterator.hasNext()) {
            final T t = iterator.next();
            iterator.set(copy(t));
        }
    }
    
    public static <K extends ICopyable<K>, V extends ICopyable<V>> Map<K, V> copyMap(final Map<K, V> map) {
        return copyMap(map, true, true);
    }
    
    public static <K extends ICopyable<K>, V extends ICopyable<V>> Map<K, V> copyMap(final Map<K, V> map, final boolean copyKey, final boolean copyValue) {
        if (map == null) {
            return null;
        }
        final Map<K, V> newMap = new HashMap<K, V>((Map<? extends K, ? extends V>)map);
        copyContent(newMap, copyKey, copyValue);
        return newMap;
    }
    
    public static <K extends ICopyable<K>, V extends ICopyable<V>> void copyContent(final Map<K, V> map, final boolean copyKey, final boolean copyValue) {
        if (!copyKey && !copyValue) {
            return;
        }
        for (final Map.Entry<K, V> entry : map.entrySet()) {
            final K key = entry.getKey();
            final V value = entry.getValue();
            map.remove(key);
            map.put(copyKey ? copy(key) : key, copyValue ? copy(value) : value);
        }
    }
}
