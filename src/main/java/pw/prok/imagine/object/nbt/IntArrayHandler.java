// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine.object.nbt;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagIntArray;

@RegisterHandler(from = int.class, to = NBTTagIntArray.class)
public class IntArrayHandler implements INBTHandler<int[], NBTTagIntArray>
{
    @Override
    public int[] read(final NBTTagIntArray nbt) {
        return nbt.func_150302_c();
    }
    
    @Override
    public NBTTagIntArray write(final int[] value) {
        return new NBTTagIntArray(value);
    }
}
