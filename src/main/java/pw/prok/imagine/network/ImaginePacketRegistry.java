// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine.network;

import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.map.TIntObjectMap;

public class ImaginePacketRegistry
{
    private TIntObjectMap<Class<? extends ImaginePacket>> mClassMap;
    
    public ImaginePacketRegistry() {
        this.mClassMap = (TIntObjectMap<Class<? extends ImaginePacket>>)new TIntObjectHashMap();
    }
    
    public boolean missing(final Class<? extends ImaginePacket> clazz) {
        return !this.mClassMap.containsKey(this.id(clazz));
    }
    
    public int id(final Class<? extends ImaginePacket> clazz) {
        return clazz.getName().hashCode();
    }
    
    public void register(final int id, final Class<? extends ImaginePacket> clazz) {
        this.mClassMap.put(id, (Object)clazz);
    }
    
    public Class<? extends ImaginePacket> get(final int id) {
        return (Class<? extends ImaginePacket>)this.mClassMap.get(id);
    }
}
