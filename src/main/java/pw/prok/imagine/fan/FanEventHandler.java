// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine.fan;

import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import com.google.common.eventbus.Subscribe;
import cpw.mods.fml.common.ModClassLoader;
import cpw.mods.fml.common.FMLCommonHandler;
import pw.prok.imagine.inject.Injector;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.event.FMLConstructionEvent;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.discovery.ModCandidate;

public class FanEventHandler
{
    private final ModCandidate mCandidate;
    private final ModContainer mContainer;
    private final Object mMod;
    private final Fan mFan;
    
    public FanEventHandler(final ModCandidate candidate, final ModContainer container, final Object mod, final Fan fan) {
        this.mCandidate = candidate;
        this.mContainer = container;
        this.mMod = mod;
        this.mFan = fan;
    }
    
    @Subscribe
    public void constructMod(final FMLConstructionEvent event) {
        try {
            final ModClassLoader modClassLoader = event.getModClassLoader();
            modClassLoader.addFile(this.mContainer.getSource());
            modClassLoader.clearNegativeCacheFor(this.mCandidate.getClassList());
            NetworkRegistry.INSTANCE.register(this.mContainer, (Class)this.mContainer.getClass(), (String)null, event.getASMHarvestedData());
            Injector.inject(this.mMod, Injector.Phase.Construct, FMLCommonHandler.instance().getSide());
        }
        catch (Throwable e) {
            throw new IllegalStateException("Cannot construct fan", e);
        }
    }
    
    @Subscribe
    public void preInit(final FMLPreInitializationEvent event) {
        Injector.inject(this.mMod, Injector.Phase.PreInit, event);
    }
    
    @Subscribe
    public void init(final FMLInitializationEvent event) {
        Injector.inject(this.mMod, Injector.Phase.Init, event);
    }
    
    @Subscribe
    public void postInit(final FMLPostInitializationEvent event) {
        Injector.inject(this.mMod, Injector.Phase.PostInit, event);
    }
}
