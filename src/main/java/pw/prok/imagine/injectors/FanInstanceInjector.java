// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine.injectors;

import java.lang.annotation.Annotation;
import pw.prok.imagine.fan.FanLoader;
import pw.prok.imagine.inject.Injector;
import pw.prok.imagine.inject.RegisterInjector;
import pw.prok.imagine.fan.Fan;
import pw.prok.imagine.inject.AnnotationFieldInjector;

@RegisterInjector({ Phase.Construct })
public class FanInstanceInjector extends AnnotationFieldInjector<Fan.Instance, Object>
{
    public FanInstanceInjector() {
        super(Fan.Instance.class, Object.class);
    }
    
    @Override
    public <V> V inject(final Fan.Instance annotation, final Class<V> type, final Object o, final Object... args) throws Exception {
        return FanLoader.getFan(type);
    }
}
