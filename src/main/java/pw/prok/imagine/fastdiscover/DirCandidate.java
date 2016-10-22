// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine.fastdiscover;

import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.util.LinkedHashSet;
import java.util.Set;
import java.net.MalformedURLException;
import net.minecraft.launchwrapper.LaunchClassLoader;
import java.io.InputStream;
import java.util.Iterator;
import java.io.File;

public class DirCandidate extends DiscoverCandidate
{
    private final File mDir;
    private final boolean mNeedInjection;
    
    public DirCandidate(final File dir, final boolean needInjection) {
        this.mDir = dir;
        this.mNeedInjection = needInjection;
    }
    
    @Override
    public Iterator<InputStream> iterator() {
        return new Iter(this.mDir);
    }
    
    @Override
    public void injectClassLoader(final LaunchClassLoader classLoader) {
        if (this.mNeedInjection) {
            try {
                classLoader.addURL(this.mDir.toURI().toURL());
            }
            catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }
    
    @Override
    public String toString() {
        return String.valueOf(this.mDir);
    }
    
    private static final class Iter implements Iterator<InputStream>
    {
        private final Set<File> mClasses;
        private final Iterator<File> mIterator;
        
        public Iter(final File dir) {
            this.mClasses = new LinkedHashSet<File>();
            this.scanDir(dir);
            this.mIterator = this.mClasses.iterator();
        }
        
        private void scanDir(final File dir) {
            final File[] files = dir.listFiles();
            if (files == null || files.length == 0) {
                return;
            }
            for (final File file : files) {
                if (file.isDirectory()) {
                    this.scanDir(file);
                }
                else if (file.isFile() && file.getName().endsWith(".class")) {
                    this.mClasses.add(file);
                }
            }
        }
        
        @Override
        public boolean hasNext() {
            return this.mIterator.hasNext();
        }
        
        @Override
        public InputStream next() {
            try {
                return new FileInputStream(this.mIterator.next());
            }
            catch (FileNotFoundException ignored) {
                return null;
            }
        }
        
        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
