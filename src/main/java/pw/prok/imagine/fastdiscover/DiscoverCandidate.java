// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine.fastdiscover;

import net.minecraft.launchwrapper.LaunchClassLoader;
import java.io.InputStream;

public abstract class DiscoverCandidate implements Iterable<InputStream>
{
    public abstract void injectClassLoader(final LaunchClassLoader p0);
}
