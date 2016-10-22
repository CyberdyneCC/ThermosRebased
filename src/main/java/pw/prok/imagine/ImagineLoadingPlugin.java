// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine;

import java.util.Iterator;
import pw.prok.imagine.fan.FanLoader;
import pw.prok.imagine.fan.Fan;
import pw.prok.imagine.asm.Transformer;
import pw.prok.imagine.inject.IInjector;
import pw.prok.imagine.inject.Injector;
import pw.prok.imagine.inject.Creator;
import pw.prok.imagine.inject.RegisterInjector;
import pw.prok.imagine.fastdiscover.FastDiscoverer;
import net.minecraft.launchwrapper.Launch;
import pw.prok.imagine.asm.ImagineRemapper;
import java.util.Map;
import pw.prok.imagine.asm.ImagineASMClassTransformer;
import java.io.File;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.MCVersion("1.7.10")
public class ImagineLoadingPlugin implements IFMLLoadingPlugin
{
    public static boolean DEV;
    public static File MC_HOME;
    public static String MC_VERSION;
    
    public String[] getASMTransformerClass() {
        return new String[] { ImagineASMClassTransformer.class.getName() };
    }
    
    public String getModContainerClass() {
        return ImagineModContainer.class.getName();
    }
    
    public String getSetupClass() {
        return null;
    }
    
    public void injectData(final Map<String, Object> map) {
        ImagineLoadingPlugin.DEV = !map.get("runtimeDeobfuscationEnabled");
        ImagineLoadingPlugin.MC_HOME = map.get("mcLocation");
        ImagineRemapper.setupDeobfuscationData("/srg_deobfuscation_1.7.10.lzma", "/method_deobfuscation_1.7.10.lzma", "/field_deobfuscation_1.7.10.lzma");
        FastDiscoverer.discoverClassloader(Launch.classLoader, FastDiscoverer.DATA_SCANNER);
        FastDiscoverer.discover(Launch.classLoader, ImagineLoadingPlugin.MC_HOME, ImagineLoadingPlugin.MC_VERSION, FastDiscoverer.DATA_SCANNER);
        for (final String className : FastDiscoverer.DISCOVER_DATA.getClassesForAnnotation(RegisterInjector.class.getName())) {
            Injector.registerInjector((Class<? extends IInjector<?>>)Creator.creator((ClassLoader)Launch.classLoader, className).clazz());
        }
        for (final String className : FastDiscoverer.DISCOVER_DATA.getClassesForAnnotation(Transformer.RegisterTransformer.class.getName())) {
            ImagineASMClassTransformer.addTransformer(Creator.creator((ClassLoader)Launch.classLoader, className).atLeast((Class<?>)Transformer.class).build());
        }
        for (final String className : FastDiscoverer.DISCOVER_DATA.getClassesForAnnotation(Fan.class.getName())) {
            FanLoader.loadFan(Creator.creator((ClassLoader)Launch.classLoader, className).clazz());
        }
        FanLoader.migrate(FanLoader.State.Found);
    }
    
    public String getAccessTransformerClass() {
        return ImagineAccessTransformer.class.getName();
    }
    
    static {
        ImagineLoadingPlugin.MC_VERSION = "1.7.10";
        System.err.println("Loading plugin initialized!");
        Launch.classLoader.addTransformerExclusion("pw.prok.imagine.");
    }
}
