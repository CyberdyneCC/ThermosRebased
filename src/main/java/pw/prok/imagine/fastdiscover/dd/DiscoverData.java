// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine.fastdiscover.dd;

import java.util.Iterator;
import com.google.common.collect.Sets;
import com.google.common.collect.ImmutableSet;
import java.util.HashMap;
import java.util.Set;
import java.util.Map;

public class DiscoverData
{
    private Map<String, Set<String>> annotationToClass;
    private Map<String, Set<String>> classToAnnotation;
    
    public DiscoverData() {
        this.annotationToClass = new HashMap<String, Set<String>>();
        this.classToAnnotation = new HashMap<String, Set<String>>();
    }
    
    public Map<String, Set<String>> getAnnotationToClass() {
        return this.annotationToClass;
    }
    
    public Map<String, Set<String>> getClassesToAnnotation() {
        return this.classToAnnotation;
    }
    
    public Set<String> getAnnotationsForClass(final String className) {
        final Set<String> annotations = this.classToAnnotation.get(className);
        return (Set<String>)((annotations != null) ? annotations : ImmutableSet.of());
    }
    
    public Set<String> getClassesForAnnotation(final String annotation) {
        final Set<String> classes = this.annotationToClass.get(annotation);
        return (Set<String>)((classes != null) ? classes : ImmutableSet.of());
    }
    
    public void putAnnotations(final String className, final Set<String> annotations) {
        this.classToAnnotation.put(className, annotations);
        for (final String annotation : annotations) {
            Set<String> classes = this.annotationToClass.get(annotation);
            if (classes == null) {
                this.annotationToClass.put(annotation, classes = (Set<String>)Sets.newHashSet((Object[])new String[] { className }));
            }
            else {
                classes.add(className);
            }
        }
    }
}
