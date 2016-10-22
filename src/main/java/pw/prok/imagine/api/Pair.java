// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine.api;

public class Pair<X, Y>
{
    protected X mFirst;
    protected Y mSecond;
    
    public Pair() {
    }
    
    public Pair(final X first, final Y second) {
        this.mFirst = first;
        this.mSecond = second;
    }
    
    public X first() {
        return this.mFirst;
    }
    
    public X first(final X first) {
        final X old = this.mFirst;
        this.mFirst = first;
        return old;
    }
    
    public Y second() {
        return this.mSecond;
    }
    
    public Y second(final Y second) {
        final Y old = this.mSecond;
        this.mSecond = second;
        return old;
    }
    
    public static <X, Y> Pair<X, Y> create(final X first, final Y second) {
        return new Pair<X, Y>(first, second);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || o.getClass() != Pair.class) {
            return false;
        }
        final Pair pair = (Pair)o;
        Label_0060: {
            if (this.mFirst != null) {
                if (this.mFirst.equals(pair.mFirst)) {
                    break Label_0060;
                }
            }
            else if (pair.mFirst == null) {
                break Label_0060;
            }
            return false;
        }
        if (this.mSecond != null) {
            if (!this.mSecond.equals(pair.mSecond)) {
                return false;
            }
        }
        else if (pair.mSecond != null) {
            return false;
        }
        return true;
        b = false;
        return b;
    }
    
    @Override
    public int hashCode() {
        int result = (this.mFirst != null) ? this.mFirst.hashCode() : 0;
        result = 31 * result + ((this.mSecond != null) ? this.mSecond.hashCode() : 0);
        return result;
    }
}
