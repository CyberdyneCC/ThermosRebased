// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine.reflect;

import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicBoolean;
import java.lang.annotation.Annotation;

public class AnnotationFilter<A extends Annotation> implements IFilter
{
    public static final int FILTER_CLASS = 1;
    public static final int FILTER_FIELDS = 2;
    public static final int FILTER_METHODS = 4;
    private final Class<A> mAnnotationClass;
    private final boolean mFilterClass;
    private final boolean mFilterFields;
    private final boolean mFilterMethods;
    private final Filter<A> mAnnotationFilter;
    
    public AnnotationFilter(final Class<A> clazz, final Filter<A> filter, final int flags) {
        this.mAnnotationClass = clazz;
        this.mAnnotationFilter = filter;
        this.mFilterClass = ((flags & 0x1) != 0x0);
        this.mFilterFields = ((flags & 0x2) != 0x0);
        this.mFilterMethods = ((flags & 0x4) != 0x0);
    }
    
    @Override
    public <S> FilterResult filterClass(final IScanner scanner, final Class<S> mainClass) {
        final AtomicBoolean result = new AtomicBoolean(false);
        if (this.mFilterClass) {
            ImagineReflect.create().scanClass(mainClass, new IClassScanCallback<S>() {
                @Override
                public void scanClass(final Class<S> mainClass, final Class<? super S> childClass) {
                    final A annotation = childClass.getAnnotation(AnnotationFilter.this.mAnnotationClass);
                    if (annotation != null && (AnnotationFilter.this.mAnnotationFilter == null || AnnotationFilter.this.mAnnotationFilter.classCheckAnnotation(childClass, annotation))) {
                        result.set(true);
                    }
                }
            });
            return FilterResult.present(result.get());
        }
        return FilterResult.Default;
    }
    
    @Override
    public <S> FilterResult filterField(final IScanner scanner, final Class<S> mainClass, final Class<? super S> childClass, final Field field) {
        if (this.mFilterFields) {
            final A annotation = field.getAnnotation(this.mAnnotationClass);
            return FilterResult.present(annotation != null && (this.mAnnotationFilter == null || this.mAnnotationFilter.fieldCheckAnnotation(field, annotation)));
        }
        return FilterResult.Default;
    }
    
    @Override
    public <S> FilterResult filterMethod(final IScanner scanner, final Class<S> mainClass, final Class<? super S> childClass, final Method method) {
        if (this.mFilterMethods) {
            final A annotation = method.getAnnotation(this.mAnnotationClass);
            return FilterResult.present(annotation != null && (this.mAnnotationFilter == null || this.mAnnotationFilter.methodCheckAnnotation(method, annotation)));
        }
        return FilterResult.Default;
    }
    
    public abstract static class Filter<A extends Annotation>
    {
        public boolean fieldCheckAnnotation(final Field field, final A annotation) {
            return true;
        }
        
        public boolean methodCheckAnnotation(final Method method, final A annotation) {
            return true;
        }
        
        public boolean classCheckAnnotation(final Class<?> clazz, final A annotation) {
            return true;
        }
    }
}
