// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine;

import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkCheckHandler;
import cpw.mods.fml.relauncher.Side;
import java.util.Map;
import com.google.common.eventbus.Subscribe;
import pw.prok.imagine.fan.FanLoader;
import pw.prok.imagine.inject.Injector;
import cpw.mods.fml.common.event.FMLConstructionEvent;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.LoadController;
import com.google.common.eventbus.EventBus;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.DummyModContainer;

public class ImagineModContainer extends DummyModContainer
{
    public static final String MODID = "kimagine";
    public static ImagineModContainer INSTANCE;
    @SidedProxy(serverSide = "pw.prok.imagine.ImaginePoxy", clientSide = "pw.prok.imagine.client.ImagineClientProxy")
    private ImagineProxy proxy;
    private ModMetadata mMetadata;
    
    public static ImagineProxy proxy() {
        return ImagineModContainer.INSTANCE.proxy;
    }
    
    public ImagineModContainer() {
        super(createContainerMetadata());
        this.mMetadata = this.getMetadata();
        ImagineModContainer.INSTANCE = this;
    }
    
    private static ModMetadata createContainerMetadata() {
        final ModMetadata metadata = new ModMetadata();
        metadata.modId = "kimagine";
        metadata.name = "KImagine";
        metadata.version = "0.2";
        return metadata;
    }
    
    public boolean registerBus(final EventBus bus, final LoadController controller) {
        bus.register((Object)this);
        return true;
    }
    
    public static void registerChildMod(final ModContainer modContainer) {
        final ModMetadata metadata = modContainer.getMetadata();
        metadata.parent = "kimagine";
        metadata.parentMod = (ModContainer)ImagineModContainer.INSTANCE;
        ImagineModContainer.INSTANCE.mMetadata.childMods.add(modContainer);
    }
    
    @Subscribe
    public void constructMod(final FMLConstructionEvent event) {
        Injector.inject(this, Injector.Phase.Construct, new Object[0]);
        FanLoader.migrate(FanLoader.State.Loaded);
    }
    
    @NetworkCheckHandler
    public boolean checkModLists(final Map<String, String> modList, final Side side) {
        return true;
    }
    
    @Subscribe
    public void onPreInit(final FMLPreInitializationEvent event) {
        Injector.inject(this, Injector.Phase.PreInit, new Object[0]);
        FanLoader.migrate(FanLoader.State.PreInitialized);
    }
    
    @Subscribe
    public void onInit(final FMLInitializationEvent event) {
        Injector.inject(this, Injector.Phase.Init, new Object[0]);
        FanLoader.migrate(FanLoader.State.Initialized);
    }
    
    @Subscribe
    public void onPostInit(final FMLPostInitializationEvent event) {
        Injector.inject(this, Injector.Phase.PostInit, new Object[0]);
        FanLoader.migrate(FanLoader.State.PostInitialized);
    }
}
