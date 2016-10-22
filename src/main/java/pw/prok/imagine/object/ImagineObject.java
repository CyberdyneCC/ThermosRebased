// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine.object;

import java.util.Iterator;
import java.util.SortedSet;
import java.util.Collection;
import com.google.common.collect.Sets;
import java.util.Comparator;
import java.lang.reflect.Field;
import pw.prok.imagine.reflect.GetSetMethodFilter;
import java.lang.reflect.Method;
import pw.prok.imagine.reflect.IMemberScanCallback;
import pw.prok.imagine.api.Member;
import pw.prok.imagine.reflect.ImagineReflect;
import pw.prok.imagine.api.Pair;
import java.util.HashMap;
import pw.prok.imagine.writer.WritableBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTBase;
import pw.prok.imagine.inject.Creator;
import pw.prok.imagine.object.nbt.INBTHandler;
import java.util.Map;

public class ImagineObject
{
    private static final ObjectState EMPTY_STATE;
    private static Map<Class<?>, ObjectState> sStates;
    private static Map<Class<?>, INBTHandler<?, ?>> sNBTHandlers;
    private static final MethodHandler METHOD_HANDLER;
    private static final FieldHandler FIELD_HANDLER;
    
    private static <T> ObjectState getState(final Class<T> clazz) {
        ObjectState state = ImagineObject.sStates.get(clazz);
        if (state == null) {
            ImagineObject.sStates.put(clazz, state = parseState((Class<Object>)clazz));
        }
        if (state == ImagineObject.EMPTY_STATE) {
            return null;
        }
        return state;
    }
    
    public static <T> T copy(final T object) {
        if (object == null) {
            return null;
        }
        final Class<T> clazz = (Class<T>)object.getClass();
        final ObjectState state = getState(clazz);
        final T newObject = Creator.creator(clazz).build();
        if (state == null) {
            return newObject;
        }
        try {
            for (int i = 0; i < state.mSize; ++i) {
                final Object value = state.get(i, object);
                state.set(i, newObject, value);
            }
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to copy object", e);
        }
        return newObject;
    }
    
    public static <T> NBTBase nbt(final T object) {
        if (object == null) {
            return null;
        }
        if (object instanceof NBTBase) {
            return (NBTBase)object;
        }
        final Class<T> clazz = (Class<T>)object.getClass();
        final ObjectState state = getState(clazz);
        if (state == null) {
            return null;
        }
        final NBTTagCompound nbt = state.newNBT();
        try {
            for (int i = 0; i < state.mSize; ++i) {
                state.get(i, object);
            }
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to create nbt", e);
        }
        return (NBTBase)nbt;
    }
    
    public static <T> void write(final T object, final WritableBuf buf) {
        buf.writeClass(object.getClass());
        buf.writeNBT(nbt(object));
    }
    
    private static <T> ObjectState parseState(final Class<T> clazz) {
        final Map<String, Pair<Pair<IHandler<?>, Object>, Pair<IHandler<?>, Object>>> members = new HashMap<String, Pair<Pair<IHandler<?>, Object>, Pair<IHandler<?>, Object>>>();
        ImagineReflect.create().addGetSetFilter(true, true).addAnnotationFilter(Member.class, false, true, true).scanMembers(clazz, new IMemberScanCallback<T>() {
            @Override
            public void scanMethod(final Class<T> mainClass, final Class<? super T> childClass, final Method method) {
                final Member member = method.getAnnotation(Member.class);
                String name = member.name();
                if (name.length() == 0) {
                    name = GetSetMethodFilter.getVarName(method);
                }
                final boolean get = GetSetMethodFilter.isGetMethod(method);
                Pair<Pair<IHandler<?>, Object>, Pair<IHandler<?>, Object>> memberPair = members.get(name);
                if (memberPair == null) {
                    members.put(name, memberPair = new Pair<Pair<IHandler<?>, Object>, Pair<IHandler<?>, Object>>());
                }
                final Pair<IHandler<?>, Object> handlerPair = new Pair<IHandler<?>, Object>(ImagineObject.METHOD_HANDLER, method);
                if (get) {
                    memberPair.first(handlerPair);
                }
                else {
                    memberPair.second(handlerPair);
                }
            }
            
            @Override
            public void scanField(final Class<T> mainClass, final Class<? super T> childClass, final Field field) {
                final Member member = field.getAnnotation(Member.class);
                String name = member.name();
                if (name.length() == 0) {
                    name = field.getName();
                }
                Pair<Pair<IHandler<?>, Object>, Pair<IHandler<?>, Object>> memberPair = members.get(name);
                if (memberPair == null) {
                    members.put(name, memberPair = new Pair<Pair<IHandler<?>, Object>, Pair<IHandler<?>, Object>>());
                }
                final Pair<IHandler<?>, Object> handlerPair = new Pair<IHandler<?>, Object>(ImagineObject.FIELD_HANDLER, field);
                if (member.load()) {
                    memberPair.first(handlerPair);
                }
                if (member.save()) {
                    memberPair.second(handlerPair);
                }
            }
        });
        final int size = members.size();
        if (size == 0) {
            return ImagineObject.EMPTY_STATE;
        }
        final String[] names = new String[size];
        final Pair<IHandler<?>, Object>[] getHandlers = (Pair<IHandler<?>, Object>[])new Pair[size];
        final Pair<IHandler<?>, Object>[] setHandlers = (Pair<IHandler<?>, Object>[])new Pair[size];
        int i = 0;
        final SortedSet<Map.Entry<String, Pair<Pair<IHandler<?>, Object>, Pair<IHandler<?>, Object>>>> set = (SortedSet<Map.Entry<String, Pair<Pair<IHandler<?>, Object>, Pair<IHandler<?>, Object>>>>)Sets.newTreeSet((Comparator)new Comparator<Map.Entry<String, Pair<Pair<IHandler<?>, Object>, Pair<IHandler<?>, Object>>>>() {
            @Override
            public int compare(final Map.Entry<String, Pair<Pair<IHandler<?>, Object>, Pair<IHandler<?>, Object>>> o1, final Map.Entry<String, Pair<Pair<IHandler<?>, Object>, Pair<IHandler<?>, Object>>> o2) {
                return o1.getKey().compareTo((String)o2.getKey());
            }
        });
        set.addAll((Collection<?>)members.entrySet());
        for (final Map.Entry<String, Pair<Pair<IHandler<?>, Object>, Pair<IHandler<?>, Object>>> p : set) {
            names[i] = p.getKey();
            getHandlers[i] = p.getValue().first();
            setHandlers[i] = p.getValue().second();
            ++i;
        }
        return new ObjectState(clazz, size, names, getHandlers, setHandlers);
    }
    
    static {
        EMPTY_STATE = new ObjectState(null, 0, null, null, null);
        ImagineObject.sStates = new HashMap<Class<?>, ObjectState>();
        ImagineObject.sNBTHandlers = new HashMap<Class<?>, INBTHandler<?, ?>>();
        System.out.println("Loaded!");
        METHOD_HANDLER = new MethodHandler();
        FIELD_HANDLER = new FieldHandler();
    }
    
    private static final class ObjectState
    {
        private final Class<?> mClass;
        private final int mSize;
        private final String[] mNames;
        private final Pair<IHandler<?>, Object>[] mGetHandlers;
        private final Pair<IHandler<?>, Object>[] mSetHandlers;
        
        public ObjectState(final Class<?> clazz, final int size, final String[] names, final Pair<IHandler<?>, Object>[] getHandlers, final Pair<IHandler<?>, Object>[] setHandlers) {
            this.mClass = clazz;
            this.mSize = size;
            this.mNames = names;
            this.mGetHandlers = getHandlers;
            this.mSetHandlers = getHandlers;
        }
        
        public Object get(final int i, final Object o) throws Exception {
            final Pair<IHandler<?>, Object> p = this.mGetHandlers[i];
            final IHandler handler = p.first();
            return handler.getData(o, p.second());
        }
        
        public void set(final int i, final Object o, final Object value) throws Exception {
            final Pair<IHandler<?>, Object> p = this.mSetHandlers[i];
            final IHandler handler = p.first();
            handler.putData(o, p.second(), value);
        }
        
        public NBTTagCompound newNBT() {
            final NBTTagCompound nbt = new NBTTagCompound();
            nbt.func_74778_a("imagine:class", this.mClass.getName());
            return nbt;
        }
    }
}
