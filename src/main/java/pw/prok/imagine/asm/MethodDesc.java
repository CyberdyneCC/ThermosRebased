// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine.asm;

import pw.prok.imagine.api.Triple;

public class MethodDesc extends Triple<String, String, String>
{
    public MethodDesc() {
    }
    
    public MethodDesc(final String first, final String second, final String third) {
        super(first, second, third);
    }
    
    public MethodDesc(final String desc) {
        this(ImagineRemapper.cutMethodClass(desc), ImagineRemapper.cutMethod(desc), ImagineRemapper.cutMethodDesc(desc));
    }
    
    @Override
    public String toString() {
        return (String)this.mFirst + '/' + (String)this.mSecond + (String)this.mThird;
    }
    
    public boolean equals(final String first, final String second, final String third) {
        return ((String)this.mFirst).equals(first) && ((String)this.mSecond).equals(second) && ((String)this.mThird).equals(third);
    }
}
