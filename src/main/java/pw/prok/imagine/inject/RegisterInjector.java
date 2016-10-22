// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine.inject;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.Annotation;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface RegisterInjector {
    Injector.Phase[] value();
}
