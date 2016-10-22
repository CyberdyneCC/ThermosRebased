// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine.fastdiscover;

import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.Enumeration;
import java.util.zip.ZipFile;
import java.net.MalformedURLException;
import net.minecraft.launchwrapper.LaunchClassLoader;
import java.io.InputStream;
import java.util.Iterator;
import java.io.File;

public class JarCandidate extends DiscoverCandidate
{
    private final File mJarFile;
    private final boolean mNeedInjection;
    
    public JarCandidate(final File jarFile, final boolean needInjection) {
        this.mJarFile = jarFile;
        this.mNeedInjection = needInjection;
    }
    
    @Override
    public Iterator<InputStream> iterator() {
        return new Iter(this.mJarFile);
    }
    
    @Override
    public void injectClassLoader(final LaunchClassLoader classLoader) {
        if (this.mNeedInjection) {
            try {
                classLoader.addURL(this.mJarFile.toURI().toURL());
            }
            catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }
    
    @Override
    public String toString() {
        return String.valueOf(this.mJarFile);
    }
    
    private static final class Iter implements Iterator<InputStream>
    {
        private ZipFile mZipFile;
        private Enumeration<? extends ZipEntry> mEntries;
        private InputStream mNextStream;
        
        public Iter(final File jarFile) {
            try {
                this.mZipFile = new ZipFile(jarFile);
                this.mEntries = this.mZipFile.entries();
            }
            catch (IOException ex) {}
        }
        
        @Override
        public boolean hasNext() {
            this.acquire();
            return this.mNextStream != null;
        }
        
        private void acquire() {
            if (this.mNextStream != null || this.mEntries == null) {
                return;
            }
            while (this.mEntries.hasMoreElements()) {
                final ZipEntry entry = (ZipEntry)this.mEntries.nextElement();
                if (!entry.getName().endsWith(".class")) {
                    continue;
                }
                try {
                    this.mNextStream = this.mZipFile.getInputStream(entry);
                }
                catch (IOException ex) {}
                break;
            }
        }
        
        @Override
        public InputStream next() {
            this.acquire();
            final InputStream is = this.mNextStream;
            this.mNextStream = null;
            return is;
        }
        
        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
