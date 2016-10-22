// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine.object.nbt;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagString;

@RegisterHandler(from = String.class, to = NBTTagString.class)
public class StringHandler implements INBTHandler<String, NBTTagString>
{
    @Override
    public String read(final NBTTagString nbt) {
        return nbt.func_150285_a_();
    }
    
    @Override
    public NBTTagString write(final String value) {
        return new NBTTagString(value);
    }
}
