// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine.object.nbt;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagShort;

@RegisterHandler(from = short.class, to = NBTTagShort.class)
public class ShortHandler implements INBTHandler<Short, NBTTagShort>
{
    @Override
    public Short read(final NBTTagShort nbt) {
        return nbt.func_150289_e();
    }
    
    @Override
    public NBTTagShort write(final Short value) {
        return new NBTTagShort((short)value);
    }
}
