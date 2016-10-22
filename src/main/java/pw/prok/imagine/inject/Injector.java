// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine.inject;

import java.util.EnumMap;
import java.util.Iterator;
import pw.prok.imagine.util.Array;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;

public abstract class Injector<State extends InjectorState> implements IInjector<State>
{
    private static final InjectorState[] NULL_STATES;
    private static Map<Phase, Set<IInjector<?>>> sInjectors;
    private static Map<Phase, Map<Class<?>, InjectorState[]>> sState;
    
    public static void registerInjector(final Class<? extends IInjector<?>> clazz) {
        final IInjector<?> injector = (IInjector<?>)Creator.creator(clazz).build();
        final Phase[] phases = clazz.getAnnotation(RegisterInjector.class).value();
        registerInjector(injector, phases);
    }
    
    public static void registerInjector(final IInjector<?> injector, final Phase... phases) {
        for (final Phase phase : phases) {
            Set<IInjector<?>> injectors = Injector.sInjectors.get(phase);
            if (injectors == null) {
                Injector.sInjectors.put(phase, injectors = new HashSet<IInjector<?>>());
            }
            injectors.add(injector);
            Map<Class<?>, InjectorState[]> states = Injector.sState.get(phase);
            if (states == null) {
                states = new HashMap<Class<?>, InjectorState[]>();
                Injector.sState.put(phase, states);
            }
            for (final Map.Entry<Class<?>, InjectorState[]> entry : states.entrySet()) {
                final InjectorState state = (InjectorState)injector.parseClass(entry.getKey());
                if (state != null) {
                    entry.setValue(Array.mergeArrays(new InjectorState[][] { entry.getValue(), { state } }));
                }
            }
        }
    }
    
    public static <Type> InjectorState[] queryStates(final Class<Type> clazz, final Phase phase) {
        Map<Class<?>, InjectorState[]> mapStates = Injector.sState.get(phase);
        if (mapStates == null) {
            mapStates = new HashMap<Class<?>, InjectorState[]>();
            Injector.sState.put(phase, mapStates);
        }
        InjectorState[] states = mapStates.get(clazz);
        if (states != null) {
            return states;
        }
        final Set<IInjector<?>> injectors = Injector.sInjectors.get(phase);
        if (injectors != null) {
            final Set<InjectorState> statesSet = new HashSet<InjectorState>();
            for (final IInjector<?> injector : injectors) {
                final InjectorState state = (InjectorState)injector.parseClass(clazz);
                if (state != null) {
                    state.mInjector = injector;
                    statesSet.add(state);
                }
            }
            mapStates.put(clazz, states = statesSet.toArray(new InjectorState[statesSet.size()]));
        }
        else {
            mapStates.put(clazz, states = Injector.NULL_STATES);
        }
        return (InjectorState[])((states == Injector.NULL_STATES) ? null : states);
    }
    
    public static <Type> void inject(final Type t, final Phase phase, final Object... args) {
        if (t == null) {
            return;
        }
        injectInternal(t, phase, t.getClass(), args);
    }
    
    private static <Type> void injectInternal(final Type t, final Phase phase, final Class<?> clazz, final Object... args) {
        final Class<?> superclass = clazz.getSuperclass();
        if (superclass != Object.class) {
            injectInternal((Object)t, phase, superclass, args);
        }
        final InjectorState[] states = queryStates(t.getClass(), phase);
        if (states == null) {
            return;
        }
        for (final InjectorState state : states) {
            final IInjector injector = state.mInjector;
            if (!injector.inject(state, t, args)) {
                throw new RuntimeException("Failed to inject into object " + t + " for class state " + clazz);
            }
        }
    }
    
    @Override
    public <T> IConstructorBuilder<T, ?> create(final Class<T> clazz) {
        return Creator.creator(clazz);
    }
    
    @Override
    public <T> IConstructorBuilder<T, ?> create(final String className) {
        return (IConstructorBuilder<T, ?>)Creator.creator(className);
    }
    
    static {
        NULL_STATES = new InjectorState[0];
        Injector.sInjectors = new EnumMap<Phase, Set<IInjector<?>>>(Phase.class);
        Injector.sState = new EnumMap<Phase, Map<Class<?>, InjectorState[]>>(Phase.class);
    }
    
    public enum Phase
    {
        Construct, 
        PreInit, 
        Init, 
        PostInit;
    }
    
    public static class InjectorState
    {
        private IInjector mInjector;
    }
}
