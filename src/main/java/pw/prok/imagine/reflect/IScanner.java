// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine.reflect;

import java.lang.annotation.Annotation;

public interface IScanner
{
    IScanner addFilter(final IFilter p0);
    
    IScanner removeFilter(final IFilter p0);
    
     <A extends Annotation> IScanner addAnnotationFilter(final Class<A> p0, final AnnotationFilter.Filter<A> p1, final boolean p2, final boolean p3, final boolean p4);
    
     <A extends Annotation> IScanner addAnnotationFilter(final Class<A> p0, final boolean p1, final boolean p2, final boolean p3);
    
     <A extends Annotation> IScanner withClassAnnotation(final Class<A> p0, final AnnotationFilter.Filter<A> p1);
    
     <A extends Annotation> IScanner withClassAnnotation(final Class<A> p0);
    
     <A extends Annotation> IScanner withFieldAnnotation(final Class<A> p0, final AnnotationFilter.Filter<A> p1);
    
     <A extends Annotation> IScanner withFieldAnnotation(final Class<A> p0);
    
     <A extends Annotation> IScanner withMethodAnnotation(final Class<A> p0, final AnnotationFilter.Filter<A> p1);
    
     <A extends Annotation> IScanner withMethodAnnotation(final Class<A> p0);
    
    IScanner addGetSetFilter(final boolean p0, final boolean p1);
    
    IScanner withSetMethods();
    
    IScanner withGetMethods();
    
     <S> void scanClass(final Class<S> p0, final IClassScanCallback<S> p1);
    
     <S> void scanFields(final Class<S> p0, final IFieldScanCallback<S> p1);
    
     <S> void scanMethod(final Class<S> p0, final IMethodScanCallback<S> p1);
    
     <S> void scanMembers(final Class<S> p0, final IMemberScanCallback<S> p1);
}
