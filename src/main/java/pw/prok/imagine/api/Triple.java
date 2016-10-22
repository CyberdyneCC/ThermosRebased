// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine.api;

public class Triple<X, Y, Z> extends Pair<X, Y>
{
    protected Z mThird;
    
    public Triple() {
    }
    
    public Triple(final X first, final Y second, final Z third) {
        super(first, second);
        this.mThird = third;
    }
    
    public Z third() {
        return this.mThird;
    }
    
    public Z third(final Z third) {
        final Z old = this.mThird;
        this.mThird = third;
        return old;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || o.getClass() != Triple.class) {
            return false;
        }
        final Triple triple = (Triple)o;
        Label_0060: {
            if (this.mFirst != null) {
                if (this.mFirst.equals(triple.mFirst)) {
                    break Label_0060;
                }
            }
            else if (triple.mFirst == null) {
                break Label_0060;
            }
            return false;
        }
        Label_0093: {
            if (this.mSecond != null) {
                if (this.mSecond.equals(triple.mSecond)) {
                    break Label_0093;
                }
            }
            else if (triple.mSecond == null) {
                break Label_0093;
            }
            return false;
        }
        if (this.mThird != null) {
            if (!this.mThird.equals(triple.mThird)) {
                return false;
            }
        }
        else if (triple.mThird != null) {
            return false;
        }
        return true;
        b = false;
        return b;
    }
    
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + ((this.mThird != null) ? this.mThird.hashCode() : 0);
        return result;
    }
    
    public static <X, Y, Z> Triple<X, Y, Z> create(final X first, final Y second, final Z third) {
        return new Triple<X, Y, Z>(first, second, third);
    }
}
