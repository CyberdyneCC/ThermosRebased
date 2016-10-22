// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine.asm;

import pw.prok.imagine.api.Pair;

public class FieldDesc extends Pair<String, String>
{
    public FieldDesc() {
    }
    
    public FieldDesc(final String first, final String second) {
        super(first, second);
    }
    
    public FieldDesc(final String desc) {
        this(ImagineRemapper.cutFieldClass(desc), ImagineRemapper.cutField(desc));
    }
    
    @Override
    public String toString() {
        return (String)this.mFirst + '/' + (String)this.mSecond;
    }
    
    public boolean equals(final String first, final String second) {
        return ((String)this.mFirst).equals(first) && ((String)this.mSecond).equals(second);
    }
}
