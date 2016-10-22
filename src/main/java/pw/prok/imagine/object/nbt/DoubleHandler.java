// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine.object.nbt;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagDouble;

@RegisterHandler(from = double.class, to = NBTTagDouble.class)
public class DoubleHandler implements INBTHandler<Double, NBTTagDouble>
{
    @Override
    public Double read(final NBTTagDouble nbt) {
        return nbt.func_150286_g();
    }
    
    @Override
    public NBTTagDouble write(final Double value) {
        return new NBTTagDouble((double)value);
    }
}
