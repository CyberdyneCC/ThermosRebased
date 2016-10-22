// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine.fastdiscover.dd;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassReader;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import org.objectweb.asm.ClassVisitor;

public class DataScanner extends ClassVisitor
{
    private final DiscoverData mData;
    private String mClassName;
    private final Set<String> mAnnotations;
    private final DataScannerCallback mCallback;
    
    public DataScanner(final DiscoverData data, final DataScannerCallback callback) {
        super(327680);
        this.mAnnotations = new HashSet<String>();
        this.mData = data;
        this.mCallback = callback;
    }
    
    public void scanClass(final InputStream is) {
        try {
            final ClassReader reader = new ClassReader(is);
            reader.accept((ClassVisitor)this, 7);
            is.close();
        }
        catch (Exception ex) {}
    }
    
    public void visit(final int version, final int access, final String name, final String signature, final String superName, final String[] interfaces) {
        this.mClassName = name.replace('/', '.');
    }
    
    public AnnotationVisitor visitAnnotation(final String desc, final boolean visible) {
        this.mAnnotations.add(desc.substring(1, desc.length() - 1).replace('/', '.'));
        return super.visitAnnotation(desc, visible);
    }
    
    public void visitEnd() {
        if (this.mAnnotations.size() > 0) {
            this.mData.putAnnotations(this.mClassName, this.mAnnotations);
        }
        if (this.mCallback != null) {
            this.mCallback.annotationResult(this.mClassName, this.mAnnotations);
        }
        this.mClassName = null;
        this.mAnnotations.clear();
    }
    
    public interface DataScannerCallback
    {
        void annotationResult(final String p0, final Set<String> p1);
    }
}
