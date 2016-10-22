// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine.writer;

import java.io.DataInput;
import net.minecraft.nbt.NBTSizeTracker;
import java.io.IOException;
import java.io.DataOutput;
import net.minecraft.nbt.NBTBase;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.Collection;
import java.lang.reflect.Array;
import pw.prok.imagine.inject.Creator;
import java.io.OutputStream;
import io.netty.buffer.ByteBufOutputStream;
import java.io.InputStream;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.nio.charset.Charset;
import net.minecraft.network.PacketBuffer;

public class WritableBuf extends PacketBuffer
{
    private static final Charset UTF_8;
    public static final int FLAG_NULL = 2;
    public static final int FLAG_LENGTH_SUPPLIED = 4;
    public static final int FLAG_GENERIC_KEY_SUPPLIED = 8;
    public static final int FLAG_GENERIC_VALUE_SUPPLIED = 16;
    private DataOutputStream mDataOutputStream;
    private DataInputStream mDataInputStream;
    
    public static boolean flag(final byte flags, final int flag) {
        return (flags & flag) == flag;
    }
    
    public WritableBuf(final byte[] bytes) {
        this(Unpooled.wrappedBuffer(bytes));
    }
    
    public WritableBuf(final ByteBuf buf) {
        super(buf);
    }
    
    public DataInputStream getDataInputStream() {
        if (this.mDataInputStream == null) {
            this.mDataInputStream = new DataInputStream((InputStream)new ByteBufInputStream((ByteBuf)this));
        }
        return this.mDataInputStream;
    }
    
    public DataOutputStream getDataOutputStream() {
        if (this.mDataOutputStream == null) {
            this.mDataOutputStream = new DataOutputStream((OutputStream)new ByteBufOutputStream((ByteBuf)this));
        }
        return this.mDataOutputStream;
    }
    
    public static <T> Class<T> parseClass(final String className) {
        try {
            return (Class<T>)Class.forName(className);
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public <T extends IWritable<T>> Class<T> readWritableClass(final String className) {
        final Class<?> clazz = parseClass(className);
        if (clazz == null || !IWritable.class.isAssignableFrom(clazz)) {
            new IllegalArgumentException("Class isn't implement IWritable").printStackTrace();
            return null;
        }
        return (Class<T>)clazz;
    }
    
    public <T> Class<T> readClass() {
        return parseClass(this.readString());
    }
    
    public WritableBuf writeClass(final Class<?> clazz) {
        return this.writeString(clazz.getName());
    }
    
    public WritableBuf writeString(final String s) {
        final byte[] bytes = s.getBytes(WritableBuf.UTF_8);
        final int length = bytes.length;
        this.writeInt(length);
        this.writeBytes(bytes, 0, length);
        return this;
    }
    
    public String readString() {
        final int length = this.readInt();
        final byte[] bytes = new byte[length];
        this.readBytes(bytes, 0, length);
        return new String(bytes, WritableBuf.UTF_8);
    }
    
    public <T extends IWritable<T>> WritableBuf writeWritable(final T object) {
        this.writeObjectInfo(object);
        object.write(this);
        return this;
    }
    
    public WritableBuf writeObjectInfo(final Object object) {
        if (object == null) {
            this.writeByte(2);
            return this;
        }
        this.writeByte(0);
        this.writeString(object.getClass().getName());
        return this;
    }
    
    public <T extends IWritable<T>> T readWritable() {
        final byte flags = this.readByte();
        if (flag(flags, 2)) {
            return null;
        }
        final Class<T> objectClass = this.readClass();
        return Creator.creator(objectClass).arg(this).build();
    }
    
    public <T extends IWritable<T>> WritableBuf writeArray(final T[] objects) {
        if (objects == null) {
            this.writeByte(2);
            return this;
        }
        this.writeByte(20);
        this.writeInt(objects.length);
        this.writeClass(objects.getClass().getComponentType());
        for (final T object : objects) {
            this.writeWritable(object);
        }
        return this;
    }
    
    public <T extends IWritable<T>> T[] readArray() {
        final byte flags = this.readByte();
        if (flag(flags, 2)) {
            return null;
        }
        if (!flag(flags, 20)) {
            throw new RuntimeException("Array require to known generic type and length");
        }
        final int length = this.readInt();
        final T[] array = (T[])Array.newInstance(this.readClass(), length);
        for (int i = 0; i < length; ++i) {
            array[i] = this.readWritable();
        }
        return array;
    }
    
    public <T extends IWritable<T>> WritableBuf writeCollection(final Collection<T> collection) {
        return this.writeCollection(collection, null);
    }
    
    public <T extends IWritable<T>> WritableBuf writeCollection(final Collection<T> collection, final Class<T> objectType) {
        if (collection == null) {
            this.writeByte(2);
            return this;
        }
        final boolean hasGenType = objectType != null;
        this.writeByte(0x4 | (hasGenType ? 16 : 0));
        this.writeInt(collection.size());
        if (hasGenType) {
            this.writeClass(objectType);
        }
        for (final T object : collection) {
            this.writeWritable(object);
        }
        return this;
    }
    
    public <T extends IWritable<T>> List<T> readCollection() {
        final byte flags = this.readByte();
        if (flag(flags, 2)) {
            return null;
        }
        final int length = flag(flags, 4) ? this.readInt() : -1;
        final String genType = flag(flags, 16) ? this.readString() : null;
        final List<T> collection = (List<T>)((length > 0) ? new ArrayList<Object>(length) : new LinkedList<Object>());
        for (int i = 0; i < length; ++i) {
            collection.add(this.readWritable());
        }
        return collection;
    }
    
    public <T extends IWritable<T>, C extends Collection<T>> C readCollection(final C collection) {
        final byte flags = this.readByte();
        if (flag(flags, 2)) {
            return null;
        }
        final int length = flag(flags, 4) ? this.readInt() : -1;
        final String genType = flag(flags, 16) ? this.readString() : null;
        for (int i = 0; i < length; ++i) {
            collection.add(this.readWritable());
        }
        return collection;
    }
    
    public <K extends IWritable<K>, V extends IWritable<V>> WritableBuf writeMap(final Map<K, V> map) {
        if (map == null) {
            this.writeByte(2);
            return this;
        }
        this.writeByte(4);
        this.writeInt(map.size());
        for (final Map.Entry<K, V> entry : map.entrySet()) {
            this.writeWritable(entry.getKey());
            this.writeWritable(entry.getValue());
        }
        return this;
    }
    
    public <K extends IWritable<K>, V extends IWritable<V>> HashMap<K, V> readMap() {
        final byte flags = this.readByte();
        if (flag(flags, 2)) {
            return null;
        }
        if (!flag(flags, 4)) {
            throw new RuntimeException("Map require to specify size");
        }
        final int length = this.readInt();
        final HashMap<K, V> map = new HashMap<K, V>(length);
        for (int i = 0; i < length; ++i) {
            map.put(this.readWritable(), this.readWritable());
        }
        return map;
    }
    
    public <K extends IWritable<K>, V extends IWritable<V>, M extends Map<K, V>> M readMap(final M map) {
        final byte flags = this.readByte();
        if (flag(flags, 2)) {
            return null;
        }
        if (!flag(flags, 4)) {
            throw new RuntimeException("Map require to specify size");
        }
        for (int length = this.readInt(), i = 0; i < length; ++i) {
            map.put(this.readWritable(), this.readWritable());
        }
        return map;
    }
    
    public <E extends Enum<E>> WritableBuf writeEnum(final E enumObject) {
        if (enumObject == null) {
            this.writeByte(2);
            return this;
        }
        this.writeByte(0);
        this.writeString(enumObject.getDeclaringClass().getName());
        this.writeInt(enumObject.ordinal());
        return this;
    }
    
    public <E extends Enum<E>> E readEnum() {
        final byte flags = this.readByte();
        if (flag(flags, 2)) {
            return null;
        }
        final Class<E> enumClass = this.readClass();
        if (enumClass == null) {
            throw new IllegalArgumentException("Enum class not found");
        }
        final E[] values = enumClass.getEnumConstants();
        final int index = this.readInt();
        if (index >= 0 && index < values.length) {
            return values[index];
        }
        throw new IndexOutOfBoundsException("Illegal index: " + index + ". Size: " + values.length);
    }
    
    public <K extends Enum<K>, V extends IWritable<V>> WritableBuf writeEnumMap(final Map<K, V> map) {
        if (map == null) {
            this.writeByte(2);
            return this;
        }
        this.writeByte(4);
        this.writeInt(map.size());
        for (final Map.Entry<K, V> entry : map.entrySet()) {
            this.writeEnum(entry.getKey());
            this.writeWritable(entry.getValue());
        }
        return this;
    }
    
    public <K extends Enum<K>, V extends IWritable<V>> EnumMap<K, V> readEnumMap(final Class<K> keyClass) {
        final EnumMap<K, V> map = new EnumMap<K, V>(keyClass);
        this.readEnumMap(map);
        return map;
    }
    
    public <K extends Enum<K>, V extends IWritable<V>, M extends Map<K, V>> M readEnumMap(final M map) {
        final byte flags = this.readByte();
        if (flag(flags, 2)) {
            return null;
        }
        if (!flag(flags, 4)) {
            throw new RuntimeException("Map require to specify size");
        }
        for (int length = this.readInt(), i = 0; i < length; ++i) {
            map.put(this.readEnum(), this.readWritable());
        }
        return map;
    }
    
    public void writeNBT(final NBTBase nbt) {
        if (nbt == null) {
            this.writeByte(-1);
            return;
        }
        this.writeInt((int)nbt.func_74732_a());
        try {
            nbt.func_74734_a((DataOutput)this.getDataOutputStream());
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public <T extends NBTBase> T readNBT() {
        final byte type = this.readByte();
        if (type == -1) {
            return null;
        }
        final T nbt = (T)NBTBase.func_150284_a(type);
        try {
            nbt.func_152446_a((DataInput)this.getDataInputStream(), 0, NBTSizeTracker.field_152451_a);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        return nbt;
    }
    
    static {
        UTF_8 = Charset.forName("utf-8");
    }
}
