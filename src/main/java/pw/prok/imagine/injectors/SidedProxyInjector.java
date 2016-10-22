// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine.injectors;

import java.lang.annotation.Annotation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.common.FMLCommonHandler;
import pw.prok.imagine.inject.Injector;
import pw.prok.imagine.inject.RegisterInjector;
import cpw.mods.fml.common.SidedProxy;
import pw.prok.imagine.inject.AnnotationFieldInjector;

@RegisterInjector({ Phase.Construct })
public class SidedProxyInjector extends AnnotationFieldInjector<SidedProxy, Object>
{
    public SidedProxyInjector() {
        super(SidedProxy.class, Object.class);
    }
    
    @Override
    public <V> V inject(final SidedProxy annotation, final Class<V> type, final Object o, final Object... args) throws Exception {
        final Side side = FMLCommonHandler.instance().getSide();
        if (side.isClient() && !"".equals(annotation.clientSide())) {
            return this.create(annotation.clientSide()).build();
        }
        if (!"".equals(annotation.serverSide())) {
            return this.create(annotation.serverSide()).build();
        }
        return this.create(type).build();
    }
}
