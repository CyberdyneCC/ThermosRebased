// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine.object.nbt;

import net.minecraft.nbt.NBTBase;
import io.netty.buffer.Unpooled;
import pw.prok.imagine.writer.WritableBuf;
import java.util.BitSet;
import net.minecraft.nbt.NBTTagByteArray;

@RegisterHandler(from = boolean[].class, to = NBTTagByteArray.class)
public class BooleanArrayHandler implements INBTHandler<boolean[], NBTTagByteArray>
{
    private static final boolean[] EMPTY;
    
    private static byte[] booleanToBytes(final boolean[] value) {
        final int length = value.length;
        final BitSet set = new BitSet();
        for (int i = 0; i < length; ++i) {
            set.set(i, value[i]);
        }
        return set.toByteArray();
    }
    
    private static boolean[] bytesToBoolean(final byte[] value, final int length) {
        final boolean[] array = new boolean[length];
        final BitSet set = BitSet.valueOf(value);
        for (int i = 0; i < length; ++i) {
            array[i] = set.get(i);
        }
        return array;
    }
    
    @Override
    public boolean[] read(final NBTTagByteArray nbt) {
        final byte[] bytes = nbt.func_150292_c();
        final WritableBuf buf = new WritableBuf(bytes);
        final int length = buf.readInt();
        if (length == 0) {
            return BooleanArrayHandler.EMPTY;
        }
        final int bytesLength = buf.readInt();
        final byte[] z = new byte[bytesLength];
        buf.readBytes(z, 0, bytesLength);
        return bytesToBoolean(z, length);
    }
    
    @Override
    public NBTTagByteArray write(final boolean[] value) {
        final int length = value.length;
        final byte[] bytes = booleanToBytes(value);
        final int byteLength = 4 + bytes.length;
        final WritableBuf buf = new WritableBuf(Unpooled.buffer(byteLength, byteLength));
        buf.writeInt(length);
        if (length != 0) {
            buf.writeInt(bytes.length);
            buf.writeBytes(bytes);
        }
        return new NBTTagByteArray(buf.array());
    }
    
    static {
        EMPTY = new boolean[0];
    }
}
