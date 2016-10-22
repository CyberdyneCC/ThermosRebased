// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine.asm;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.Annotation;

public interface Transformer
{
    void transform(final ImagineASM p0);
    
    @Target({ ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    public @interface RegisterTransformer {
    }
}
