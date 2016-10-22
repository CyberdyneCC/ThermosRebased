// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine.fan;

import java.util.HashMap;
import java.util.Iterator;
import pw.prok.imagine.inject.Creator;
import java.util.Map;

public class FanLoader
{
    private static Map<Class<?>, FanHolder<?>> sFanMap;
    private static State sState;
    
    public static <T> void loadFan(final Class<T> fanClass) {
        if (FanLoader.sState != State.Unloaded) {
            throw new IllegalStateException("Attempt to load fan after initializing!");
        }
        final Fan fanData = fanClass.getAnnotation(Fan.class);
        if (fanData == null) {
            throw new IllegalArgumentException("Illegal fan! No fan data found");
        }
        final FanHolder<T> holder = new FanHolder<T>();
        ((FanHolder<Object>)holder).fan = Creator.creator(fanClass).build();
        ((FanHolder<Object>)holder).fanData = new FanData(fanData);
        ((FanHolder<Object>)holder).state = State.Unloaded;
        FanLoader.sFanMap.put(fanClass, holder);
    }
    
    public static void migrate(final State state) {
        switch (state) {
            case Unloaded: {
                throw new IllegalArgumentException("Could not migrate to unloaded state!");
            }
        }
        FanLoader.sState = state;
        for (final FanHolder<?> holder : FanLoader.sFanMap.values()) {
            ((FanHolder<Object>)holder).state = state;
        }
    }
    
    public static <T> T getFan(final Class<T> clazz) {
        final FanHolder<T> holder = (FanHolder<T>)FanLoader.sFanMap.get(clazz);
        return (T)((holder != null) ? ((FanHolder<Object>)holder).fan : null);
    }
    
    static {
        FanLoader.sFanMap = new HashMap<Class<?>, FanHolder<?>>();
        FanLoader.sState = State.Unloaded;
    }
    
    private static class FanHolder<T>
    {
        private FanData fanData;
        private T fan;
        private State state;
    }
    
    public enum State
    {
        Unloaded, 
        Found, 
        Loaded, 
        PreInitialized, 
        Initialized, 
        PostInitialized, 
        Error;
    }
}
