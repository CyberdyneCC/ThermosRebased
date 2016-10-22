// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine.reflect;

import java.util.Iterator;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

public class ImagineReflect implements IScanner
{
    private List<IFilter> mFilters;
    
    public ImagineReflect() {
        this.mFilters = new LinkedList<IFilter>();
    }
    
    public static IScanner create() {
        return new ImagineReflect();
    }
    
    @Override
    public <S> void scanClass(final Class<S> clazz, final IClassScanCallback<S> callback) {
        if (clazz == null || !this.filterClass(clazz)) {
            return;
        }
        for (Class<? super S> c = clazz; c != Object.class; c = c.getSuperclass()) {
            callback.scanClass(clazz, c);
        }
    }
    
    @Override
    public <S> void scanFields(final Class<S> clazz, final IFieldScanCallback<S> callback) {
        this.scanMembersInternal(clazz, callback, null);
    }
    
    @Override
    public <S> void scanMethod(final Class<S> clazz, final IMethodScanCallback<S> callback) {
        this.scanMembersInternal(clazz, null, callback);
    }
    
    @Override
    public <S> void scanMembers(final Class<S> clazz, final IMemberScanCallback<S> callback) {
        this.scanMembersInternal(clazz, callback, callback);
    }
    
    private <S> void scanMembersInternal(final Class<S> clazz, final IFieldScanCallback<S> fieldCallback, final IMethodScanCallback<S> methodCallback) {
        this.scanClass(clazz, new IClassScanCallback<S>() {
            @Override
            public void scanClass(final Class<S> mainClass, final Class<? super S> superClass) {
                if (fieldCallback != null) {
                    for (final Field field : superClass.getDeclaredFields()) {
                        if (ImagineReflect.this.filterField((Class<Object>)mainClass, superClass, field)) {
                            fieldCallback.scanField(mainClass, superClass, field);
                        }
                    }
                }
                if (methodCallback != null) {
                    for (final Method method : superClass.getDeclaredMethods()) {
                        if (ImagineReflect.this.filterMethod((Class<Object>)mainClass, superClass, method)) {
                            methodCallback.scanMethod(mainClass, superClass, method);
                        }
                    }
                }
            }
        });
    }
    
    @Override
    public IScanner addFilter(final IFilter filter) {
        this.mFilters.add(filter);
        return this;
    }
    
    @Override
    public IScanner removeFilter(final IFilter filter) {
        this.mFilters.remove(filter);
        return this;
    }
    
    @Override
    public <A extends Annotation> IScanner addAnnotationFilter(final Class<A> annotationClass, final AnnotationFilter.Filter<A> filter, final boolean filterClass, final boolean filterFields, final boolean filterMethod) {
        return this.addFilter(new AnnotationFilter<Object>(annotationClass, filter, (filterClass ? 1 : 0) | (filterFields ? 2 : 0) | (filterMethod ? 4 : 0)));
    }
    
    @Override
    public <A extends Annotation> IScanner addAnnotationFilter(final Class<A> annotationClass, final boolean filterClass, final boolean filterFields, final boolean filterMethod) {
        return this.addAnnotationFilter(annotationClass, null, filterClass, filterFields, filterMethod);
    }
    
    @Override
    public <A extends Annotation> IScanner withClassAnnotation(final Class<A> annotationClass, final AnnotationFilter.Filter<A> filter) {
        return this.addFilter(new AnnotationFilter<Object>(annotationClass, filter, 1));
    }
    
    @Override
    public <A extends Annotation> IScanner withClassAnnotation(final Class<A> annotationClass) {
        return this.withClassAnnotation(annotationClass, null);
    }
    
    @Override
    public <A extends Annotation> IScanner withFieldAnnotation(final Class<A> annotationClass, final AnnotationFilter.Filter<A> filter) {
        return this.addFilter(new AnnotationFilter<Object>(annotationClass, filter, 2));
    }
    
    @Override
    public <A extends Annotation> IScanner withFieldAnnotation(final Class<A> annotationClass) {
        return this.withFieldAnnotation(annotationClass, null);
    }
    
    @Override
    public <A extends Annotation> IScanner withMethodAnnotation(final Class<A> annotationClass, final AnnotationFilter.Filter<A> filter) {
        return this.addFilter(new AnnotationFilter<Object>(annotationClass, filter, 4));
    }
    
    @Override
    public <A extends Annotation> IScanner withMethodAnnotation(final Class<A> annotationClass) {
        return this.withMethodAnnotation(annotationClass, null);
    }
    
    @Override
    public IScanner addGetSetFilter(final boolean get, final boolean set) {
        return this.addFilter(new GetSetMethodFilter(get, set));
    }
    
    @Override
    public IScanner withGetMethods() {
        return this.addGetSetFilter(true, false);
    }
    
    @Override
    public IScanner withSetMethods() {
        return this.addGetSetFilter(false, true);
    }
    
    private <S> boolean filterClass(final Class<S> mainClass) {
        for (final IFilter filter : this.mFilters) {
            switch (filter.filterClass(this, mainClass)) {
                case Accept: {
                    return true;
                }
                case Reject: {
                    return false;
                }
                default: {
                    continue;
                }
            }
        }
        return true;
    }
    
    private <S> boolean filterField(final Class<S> mainClass, final Class<? super S> superClass, final Field field) {
        for (final IFilter filter : this.mFilters) {
            switch (filter.filterField(this, mainClass, superClass, field)) {
                case Accept: {
                    return true;
                }
                case Reject: {
                    return false;
                }
                default: {
                    continue;
                }
            }
        }
        return true;
    }
    
    private <S> boolean filterMethod(final Class<S> mainClass, final Class<? super S> superClass, final Method method) {
        for (final IFilter filter : this.mFilters) {
            switch (filter.filterMethod(this, mainClass, superClass, method)) {
                case Accept: {
                    return true;
                }
                case Reject: {
                    return false;
                }
                default: {
                    continue;
                }
            }
        }
        return true;
    }
}
