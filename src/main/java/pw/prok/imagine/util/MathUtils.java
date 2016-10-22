// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine.util;

public class MathUtils
{
    public static int roundUpDivision(final int dividend, final int divider) {
        if (dividend == 0) {
            return 0;
        }
        return (dividend + divider - 1) / divider;
    }
}
