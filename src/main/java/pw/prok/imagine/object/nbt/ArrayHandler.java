// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine.object.nbt;

import net.minecraft.nbt.NBTBase;
import pw.prok.imagine.object.ImagineObject;
import io.netty.buffer.Unpooled;
import pw.prok.imagine.util.Array;
import pw.prok.imagine.writer.WritableBuf;
import net.minecraft.nbt.NBTTagByteArray;

@RegisterHandler(from = Object[].class, to = NBTTagByteArray.class)
public class ArrayHandler<T> implements INBTHandler<T[], NBTTagByteArray>
{
    @Override
    public T[] read(final NBTTagByteArray nbt) {
        final WritableBuf buf = new WritableBuf(nbt.func_150292_c());
        final int length = buf.readInt();
        final Class<T> componentType = buf.readClass();
        final T[] array = Array.newArray(componentType, length);
        for (int i = 0; i < length; ++i) {
            buf.readNBT();
        }
        return null;
    }
    
    @Override
    public NBTTagByteArray write(final T[] value) {
        final WritableBuf buf = new WritableBuf(Unpooled.buffer());
        buf.writeInt(value.length);
        buf.writeClass(value.getClass().getComponentType());
        for (final T t : value) {
            final NBTBase nbt = ImagineObject.nbt(t);
            if (nbt == null) {
                throw new RuntimeException("Null items not allowed");
            }
            buf.writeNBT(nbt);
        }
        return new NBTTagByteArray(buf.array());
    }
}
