// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine.object.nbt;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByteArray;

@RegisterHandler(from = byte.class, to = NBTTagByteArray.class)
public class ByteArrayHandler implements INBTHandler<byte[], NBTTagByteArray>
{
    @Override
    public byte[] read(final NBTTagByteArray nbt) {
        return nbt.func_150292_c();
    }
    
    @Override
    public NBTTagByteArray write(final byte[] value) {
        return new NBTTagByteArray(value);
    }
}
