// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine.object.nbt;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;

@RegisterHandler(from = boolean.class, to = NBTTagByte.class)
public class BooleanHandler implements INBTHandler<Boolean, NBTTagByte>
{
    private static final byte TRUE = 1;
    private static final byte FALSE = 0;
    
    @Override
    public Boolean read(final NBTTagByte nbt) {
        return nbt.func_150290_f() != 0;
    }
    
    @Override
    public NBTTagByte write(final Boolean value) {
        return new NBTTagByte((byte)(((boolean)value) ? 1 : 0));
    }
}
