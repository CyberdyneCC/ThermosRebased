// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine.util;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Collection;

public class Array
{
    public static <T> T[] newArray(final Class<T> clazz, final int length) {
        return (T[])java.lang.reflect.Array.newInstance(clazz, length);
    }
    
    public static <T> T[] newArrayUnchecked(final Class<?> clazz, final int length) {
        return (T[])newArray(clazz, length);
    }
    
    public static <T> T[] appendToArray(final T[] array, final T... objects) {
        if (array == null || array.length == 0) {
            return objects;
        }
        if (objects == null || objects.length == 0) {
            return array;
        }
        final int i1 = array.length;
        final int i2 = objects.length;
        final T[] newArray = newArrayUnchecked(array.getClass().getComponentType(), i1 + i2);
        System.arraycopy(array, 0, newArray, 0, i1);
        System.arraycopy(objects, 0, newArray, i1, i2);
        return newArray;
    }
    
    public static <T> T[] mergeArrays(final T[]... arrays) {
        if (arrays == null) {
            return null;
        }
        int length = 0;
        Class<T> componentClass = null;
        for (final T[] array : arrays) {
            if (array != null) {
                if (componentClass == null) {
                    componentClass = (Class<T>)array.getClass().getComponentType();
                }
                length += array.length;
            }
        }
        if (componentClass == null) {
            return null;
        }
        final T[] newArray = newArray(componentClass, length);
        int index = 0;
        for (final T[] array2 : arrays) {
            if (array2 != null) {
                System.arraycopy(array2, 0, newArray, index, array2.length);
                index += array2.length;
            }
        }
        return newArray;
    }
    
    public static <T> T[] asArray(final Collection<T> collection) {
        return asArray(collection, null);
    }
    
    public static <T> T[] asArray(final Collection<T> collection, final Class<T> clazz) {
        final int size = collection.size();
        Class<? extends T> component = (Class<? extends T>)clazz;
        if (clazz == null) {
            if (size == 0) {
                return null;
            }
            final T t = collection.iterator().next();
            component = (Class<? extends T>)t.getClass();
        }
        final T[] array = newArray((Class<T>)component, size);
        collection.toArray(array);
        return array;
    }
    
    public static <T> T[] keys(final Map<T, ?> map) {
        return keys(map, null);
    }
    
    public static <T> T[] keys(final Map<T, ?> map, final Class<T> clazz) {
        return asArray(map.keySet(), clazz);
    }
    
    public static <T> T[] values(final Map<?, T> map) {
        return values(map, null);
    }
    
    public static <T> T[] values(final Map<?, T> map, final Class<T> clazz) {
        return asArray(map.values(), clazz);
    }
    
    public static <T> List<T> asList(final T... values) {
        final List<T> list = new ArrayList<T>(values.length);
        Collections.addAll(list, values);
        return list;
    }
}
