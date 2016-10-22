// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine.fastdiscover;

import pw.prok.imagine.fan.Fan;
import java.util.Set;
import cpw.mods.fml.common.FMLLog;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.net.URL;
import java.util.LinkedList;
import net.minecraft.launchwrapper.LaunchClassLoader;
import java.io.File;
import java.io.FileFilter;
import pw.prok.imagine.fastdiscover.dd.DiscoverData;
import pw.prok.imagine.fastdiscover.dd.DataScanner;

public class FastDiscoverer implements DataScanner.DataScannerCallback
{
    public static final DiscoverData DISCOVER_DATA;
    public static final DataScanner DATA_SCANNER;
    private static final FileFilter JAR_FILTER;
    private static final FileFilter DIR_FILTER;
    private static boolean sNeedInjection;
    
    private static boolean isDirValid(final File file) {
        if (!file.isDirectory()) {
            return false;
        }
        final File metaInf = new File(file, "META-INF");
        return metaInf.exists() && metaInf.isDirectory();
    }
    
    private static boolean isFileValid(final File file) {
        return file.isFile() && (file.getName().endsWith(".jar") || file.getName().endsWith(".zip"));
    }
    
    public static void discoverClassloader(final LaunchClassLoader loader, final DataScanner scanner) {
        final List<DiscoverCandidate> candidates = new LinkedList<DiscoverCandidate>();
        for (final URL url : loader.getSources()) {
            try {
                final File file = new File(url.toURI());
                if (!file.exists()) {
                    continue;
                }
                if (file.isDirectory() && isDirValid(file)) {
                    candidates.add(new DirCandidate(file, false));
                }
                else {
                    if (!file.isFile() || !isFileValid(file)) {
                        continue;
                    }
                    candidates.add(new JarCandidate(file, false));
                }
            }
            catch (Exception ex) {}
        }
        scan(loader, candidates, scanner);
    }
    
    public static void discover(final LaunchClassLoader loader, final File mcHome, final String mcVersion, final DataScanner scanner) {
        final List<DiscoverCandidate> candidates = new LinkedList<DiscoverCandidate>();
        final File modsDir = new File(mcHome, "mods");
        if (modsDir.exists() && modsDir.isDirectory()) {
            scanDir(modsDir, candidates);
        }
        final File versionModsDir = new File(modsDir, mcVersion);
        if (versionModsDir.exists() && versionModsDir.isDirectory()) {
            scanDir(versionModsDir, candidates);
        }
        scan(loader, candidates, scanner);
    }
    
    private static void scanDir(final File dir, final List<DiscoverCandidate> candidates) {
        File[] files = dir.listFiles(FastDiscoverer.JAR_FILTER);
        if (files != null && files.length > 0) {
            for (final File file : files) {
                candidates.add(new JarCandidate(file, true));
            }
        }
        files = dir.listFiles(FastDiscoverer.DIR_FILTER);
        if (files != null && files.length > 0) {
            for (final File file : files) {
                candidates.add(new DirCandidate(file, true));
            }
        }
    }
    
    private static void scan(final LaunchClassLoader loader, final List<DiscoverCandidate> candidates, final DataScanner scanner) {
        for (final DiscoverCandidate candidate : candidates) {
            FastDiscoverer.sNeedInjection = false;
            for (final InputStream is : candidate) {
                scanner.scanClass(is);
            }
            if (FastDiscoverer.sNeedInjection) {
                FMLLog.info("Found Fan in " + candidate + ", injecting into classloader...", new Object[0]);
                candidate.injectClassLoader(loader);
            }
        }
    }
    
    @Override
    public void annotationResult(final String className, final Set<String> annotations) {
        FastDiscoverer.sNeedInjection = annotations.contains(Fan.class.getName());
    }
    
    static {
        DISCOVER_DATA = new DiscoverData();
        DATA_SCANNER = new DataScanner(FastDiscoverer.DISCOVER_DATA, new FastDiscoverer());
        JAR_FILTER = new FileFilter() {
            @Override
            public boolean accept(final File file) {
                return isFileValid(file);
            }
        };
        DIR_FILTER = new FileFilter() {
            @Override
            public boolean accept(final File file) {
                return isDirValid(file);
            }
        };
    }
}
