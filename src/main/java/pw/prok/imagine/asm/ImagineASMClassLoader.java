// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine.asm;

import net.minecraft.launchwrapper.Launch;
import java.util.Collections;
import java.util.Collection;
import net.minecraft.launchwrapper.LaunchClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.net.URL;
import java.net.URLClassLoader;

public class ImagineASMClassLoader extends URLClassLoader
{
    public static ImagineASMClassLoader ASM_CLASSLOADER;
    
    public ImagineASMClassLoader(final ClassLoader parent) {
        super(new URL[0], parent);
    }
    
    public void addURL(final URL url) {
        super.addURL(url);
    }
    
    public List<URL> getSources() {
        final List<URL> sources = new ArrayList<URL>();
        putSources(this, sources);
        return sources;
    }
    
    private static void putSources(ClassLoader classLoader, final List<URL> sources) {
        if (classLoader instanceof LaunchClassLoader) {
            sources.addAll(((LaunchClassLoader)classLoader).getSources());
        }
        else if (classLoader instanceof URLClassLoader) {
            Collections.addAll(sources, ((URLClassLoader)classLoader).getURLs());
        }
        classLoader = classLoader.getParent();
        if (classLoader != null) {
            putSources(classLoader, sources);
        }
    }
    
    static {
        ImagineASMClassLoader.ASM_CLASSLOADER = new ImagineASMClassLoader((ClassLoader)Launch.classLoader);
    }
}
