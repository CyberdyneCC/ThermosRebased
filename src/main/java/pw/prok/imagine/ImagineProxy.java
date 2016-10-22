// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine;

import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;

public class ImagineProxy
{
    public EntityPlayer obtainPlayer(final INetHandler handler) {
        if (handler instanceof NetHandlerPlayServer) {
            return (EntityPlayer)((NetHandlerPlayServer)handler).field_147369_b;
        }
        return null;
    }
}
