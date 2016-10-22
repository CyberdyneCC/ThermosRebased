// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine.fan;

import cpw.mods.fml.common.network.NetworkCheckHandler;
import cpw.mods.fml.relauncher.Side;
import pw.prok.imagine.inject.Creator;
import cpw.mods.fml.common.ModContainer;
import java.util.Map;
import cpw.mods.fml.common.discovery.ModCandidate;
import pw.prok.imagine.util.ModContainerWrapper;

public class FanModContainer extends ModContainerWrapper<FanDefaultModContainer>
{
    public FanModContainer(final String className, final ModCandidate container, final Map<String, Object> modDescriptor) {
        try {
            final Class<?> clazz = Class.forName(className);
            final Fan fan = clazz.getAnnotation(Fan.class);
            final Class<? extends FanDefaultModContainer> containerClass = (Class<? extends FanDefaultModContainer>)fan.container();
            if (containerClass == FanDefaultModContainer.class) {
                this.mContainer = (T)new FanDefaultModContainer(this, clazz, fan, container, modDescriptor);
            }
            else {
                this.mContainer = (T)Creator.creator(containerClass).arg(FanModContainer.class, this).arg(Class.class, clazz).arg(Fan.class, fan).arg(ModCandidate.class, container).arg(Map.class, modDescriptor).build();
            }
            ((FanDefaultModContainer)this.mContainer).registerFan();
        }
        catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
    
    @NetworkCheckHandler
    public boolean checkModLists(final Map<String, String> modList, final Side side) {
        return ((FanDefaultModContainer)this.mContainer).checkModLists(modList, side);
    }
}
