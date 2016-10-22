// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine.asm;

import org.objectweb.asm.tree.MethodNode;
import java.util.Iterator;

public class MethodFilter implements Iterable<ImagineMethod>
{
    private final ImagineASM mAsm;
    private final Filter mFilter;
    
    public MethodFilter(final ImagineASM asm, final Filter filter) {
        this.mAsm = asm;
        this.mFilter = filter;
    }
    
    @Override
    public Iterator<ImagineMethod> iterator() {
        this.mAsm.readClass();
        return new MethodIterator(this, this.mAsm.mClassNode.methods.iterator());
    }
    
    private static class MethodIterator implements Iterator<ImagineMethod>
    {
        private final MethodFilter mFilter;
        private final Iterator<MethodNode> mIterator;
        private ImagineMethod mMethod;
        
        public MethodIterator(final MethodFilter filter, final Iterator<MethodNode> iterator) {
            this.mFilter = filter;
            this.mIterator = iterator;
        }
        
        @Override
        public boolean hasNext() {
            this.mMethod = null;
            while (this.mIterator.hasNext()) {
                final MethodNode node = this.mIterator.next();
                final ImagineMethod method = new ImagineMethod(this.mFilter.mAsm, node);
                if (this.mFilter.mFilter.matching(method)) {
                    this.mMethod = method;
                }
            }
            return this.mIterator.hasNext();
        }
        
        @Override
        public ImagineMethod next() {
            if (this.mMethod == null) {
                this.hasNext();
            }
            final ImagineMethod method = this.mMethod;
            this.mMethod = null;
            return method;
        }
        
        @Override
        public void remove() {
            this.mIterator.remove();
        }
    }
}
