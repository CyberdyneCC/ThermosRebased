// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine.object.nbt;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagFloat;

@RegisterHandler(from = float.class, to = NBTTagFloat.class)
public class FloatHandler implements INBTHandler<Float, NBTTagFloat>
{
    @Override
    public Float read(final NBTTagFloat nbt) {
        return nbt.func_150288_h();
    }
    
    @Override
    public NBTTagFloat write(final Float value) {
        return new NBTTagFloat((float)value);
    }
}
