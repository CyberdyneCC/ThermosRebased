// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine.object;

import pw.prok.imagine.api.ICopyable;
import pw.prok.imagine.writer.IWritable;

public interface IObject<T extends IObject<T>> extends IWritable<T>, ICopyable<T>
{
}
