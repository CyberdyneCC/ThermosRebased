// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine.object.nbt;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagLong;

@RegisterHandler(from = long.class, to = NBTTagLong.class)
public class LongHandler implements INBTHandler<Long, NBTTagLong>
{
    @Override
    public Long read(final NBTTagLong nbt) {
        return nbt.func_150291_c();
    }
    
    @Override
    public NBTTagLong write(final Long value) {
        return new NBTTagLong((long)value);
    }
}
