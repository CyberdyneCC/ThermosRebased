// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine.injectors;

import pw.prok.imagine.inject.IConstructorBuilder;
import java.lang.annotation.Annotation;
import pw.prok.imagine.inject.Injector;
import pw.prok.imagine.inject.RegisterInjector;
import pw.prok.imagine.network.ImagineNetwork;
import pw.prok.imagine.network.Network;
import pw.prok.imagine.inject.AnnotationFieldInjector;

@RegisterInjector({ Phase.Init })
public class ImagineNetworkInjector extends AnnotationFieldInjector<Network, ImagineNetwork>
{
    public ImagineNetworkInjector() {
        super(Network.class, ImagineNetwork.class);
    }
    
    @Override
    public <V extends ImagineNetwork> V inject(final Network annotation, final Class<V> type, final Object o, final Object... args) throws Exception {
        return ((IConstructorBuilder<V, I>)this.create(type).arg(annotation.value())).build();
    }
}
