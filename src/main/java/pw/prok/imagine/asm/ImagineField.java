// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine.asm;

import org.objectweb.asm.tree.FieldNode;

public class ImagineField extends ImagineAccess<ImagineField>
{
    private final ImagineASM mAsm;
    private final FieldNode mField;
    
    ImagineField(final ImagineASM asm, final FieldNode field) {
        this.mAsm = asm;
        this.mField = field;
    }
    
    @Override
    public ImagineField addAccess(final int modifiers) {
        final FieldNode mField = this.mField;
        mField.access |= modifiers;
        return this;
    }
    
    @Override
    public ImagineField limitAccess(final int modifiers) {
        final FieldNode mField = this.mField;
        mField.access &= modifiers;
        return this;
    }
}
