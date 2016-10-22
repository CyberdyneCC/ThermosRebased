// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine.object.nbt;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagInt;

@RegisterHandler(from = int.class, to = NBTTagInt.class)
public class IntHandler implements INBTHandler<Integer, NBTTagInt>
{
    @Override
    public Integer read(final NBTTagInt nbt) {
        return nbt.func_150287_d();
    }
    
    @Override
    public NBTTagInt write(final Integer value) {
        return new NBTTagInt((int)value);
    }
}
