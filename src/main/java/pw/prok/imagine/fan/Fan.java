// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine.fan;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface Fan {
    String name() default "";
    
    String id() default "";
    
    String version() default "";
    
    boolean serverRequired() default false;
    
    boolean clientRequired() default false;
    
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.FIELD })
    public @interface Instance {
        boolean required() default true;
    }
}
