// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine.config;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.Annotation;
import java.util.Map;
import java.io.InputStream;

public class Config
{
    public static Map<String, String> load(final InputStream is) {
        return null;
    }
    
    @Target({ ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Name {
        String value();
        
        boolean autoload() default true;
    }
    
    @Target({ ElementType.FIELD })
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Property {
        boolean load() default true;
        
        boolean save() default true;
        
        String desc() default "";
    }
}
