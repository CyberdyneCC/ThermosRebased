// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine.fan;

import java.util.HashMap;
import java.util.Iterator;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import pw.prok.imagine.api.Pair;
import java.util.Map;

public class FanRegistry
{
    private static final Map<Fan, Pair<FanModContainer, Object>> sFansMap;
    private static final Map<String, Fan> sIdMap;
    
    public static void registerFan(final Fan fan, final FanModContainer container, final Object object) {
        final String id = fan.id();
        FanRegistry.sFansMap.put(fan, Pair.create(container, object));
        FanRegistry.sIdMap.put(id, fan);
    }
    
    public static Fan getFanInfo(final String id) {
        return FanRegistry.sIdMap.get(id);
    }
    
    public static <T> T getFan(final String id) {
        return (T)FanRegistry.sFansMap.get(getFanInfo(id));
    }
    
    public static boolean isFanLoaded(final String id) {
        return FanRegistry.sIdMap.containsKey(id);
    }
    
    public static void finishLoading() {
        for (final Map.Entry<Fan, Pair<FanModContainer, Object>> entry : FanRegistry.sFansMap.entrySet()) {
            final Fan fan = entry.getKey();
            final ModContainer container = (ModContainer)entry.getValue().first();
            final String submod = fan.submod();
            if (submod != null && !"".equals(submod)) {
                final Fan parentFan = getFanInfo(submod);
                ModContainer parent = null;
                if (parentFan != null) {
                    parent = (ModContainer)FanRegistry.sFansMap.get(parentFan).first();
                }
                else {
                    for (final ModContainer c : Loader.instance().getActiveModList()) {
                        if (submod.equals(c.getModId())) {
                            parent = c;
                            break;
                        }
                    }
                }
                if (parent == null) {
                    throw new IllegalStateException("Cannot find submod " + submod);
                }
                parent.getMetadata().childMods.add(container);
                container.getMetadata().parentMod = parent;
                container.getMetadata().parent = parent.getModId();
            }
        }
    }
    
    static {
        sFansMap = new HashMap<Fan, Pair<FanModContainer, Object>>();
        sIdMap = new HashMap<String, Fan>();
    }
}
