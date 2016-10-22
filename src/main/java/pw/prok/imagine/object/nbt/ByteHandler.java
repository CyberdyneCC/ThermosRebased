// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine.object.nbt;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;

@RegisterHandler(from = byte.class, to = NBTTagByte.class)
public class ByteHandler implements INBTHandler<Byte, NBTTagByte>
{
    @Override
    public Byte read(final NBTTagByte nbt) {
        return nbt.func_150290_f();
    }
    
    @Override
    public NBTTagByte write(final Byte value) {
        return new NBTTagByte((byte)value);
    }
}
