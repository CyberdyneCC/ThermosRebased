// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine.object.nbt;

import net.minecraft.nbt.NBTBase;

public interface INBTHandler<T, B extends NBTBase>
{
    T read(final B p0);
    
    B write(final T p0);
}
