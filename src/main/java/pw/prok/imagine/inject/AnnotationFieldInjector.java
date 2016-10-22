// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine.inject;

import java.util.Collection;
import pw.prok.imagine.util.Array;
import java.util.List;
import java.lang.reflect.Modifier;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.lang.annotation.Annotation;

public abstract class AnnotationFieldInjector<A extends Annotation, T> extends Injector<AnnotationFieldState<A, T>>
{
    private final Class<A> mAnnotationClass;
    private final Class<T> mObjectClass;
    
    public AnnotationFieldInjector(final Class<A> annotationClass, final Class<T> objectClass) {
        this.mAnnotationClass = annotationClass;
        this.mObjectClass = objectClass;
    }
    
    @Override
    public AnnotationFieldState<A, T> parseClass(final Class<?> clazz) {
        final List<A> annotations = new LinkedList<A>();
        final List<Field> fields = new LinkedList<Field>();
        final List<Class<? extends T>> classes = new LinkedList<Class<? extends T>>();
        for (final Field field : clazz.getDeclaredFields()) {
            if (!Modifier.isStatic(field.getModifiers())) {
                final A annotation = field.getAnnotation(this.mAnnotationClass);
                if (annotation != null) {
                    final Class<?> type = field.getType();
                    if (this.mObjectClass == null || this.mObjectClass.isAssignableFrom(type)) {
                        field.setAccessible(true);
                        annotations.add(annotation);
                        fields.add(field);
                        classes.add((Class<? extends T>)type);
                    }
                }
            }
        }
        if (annotations.size() == 0) {
            return null;
        }
        return new AnnotationFieldState<A, T>(annotations, fields, classes);
    }
    
    @Override
    public boolean inject(final AnnotationFieldState<A, T> state, final Object o, final Object... args) {
        for (int i = 0; i < state.length; ++i) {
            try {
                state.mFields[i].set(o, this.inject(((Annotation[])state.mAnnotations)[i], (Class<Object>)state.mTypes[i], o, args));
            }
            catch (Exception e) {
                throw new RuntimeException("Failed to inject annotation " + ((Annotation[])state.mAnnotations)[i], e);
            }
        }
        return true;
    }
    
    @Override
    public <Z> IConstructorBuilder<Z, ?> create(final Class<Z> clazz) {
        return (IConstructorBuilder<Z, ?>)super.create(clazz).atLeast(this.mObjectClass);
    }
    
    public abstract <V extends T> V inject(final A p0, final Class<V> p1, final Object p2, final Object... p3) throws Exception;
    
    public static class AnnotationFieldState<A, T> extends InjectorState
    {
        final A[] mAnnotations;
        final Class<? extends T>[] mTypes;
        final Field[] mFields;
        final int length;
        
        public AnnotationFieldState(final List<A> annotations, final List<Field> fields, final List<Class<? extends T>> types) {
            this.mAnnotations = Array.asArray(annotations);
            this.mFields = Array.asArray(fields);
            this.mTypes = Array.asArray(types);
            if (this.mAnnotations.length != this.mFields.length || this.mAnnotations.length != this.mTypes.length) {
                throw new RuntimeException("Annotation/field/types desync!");
            }
            this.length = this.mAnnotations.length;
        }
    }
}
