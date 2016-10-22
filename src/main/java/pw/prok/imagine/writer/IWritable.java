// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine.writer;

public interface IWritable<T extends IWritable<T>>
{
    void write(final WritableBuf p0);
}
