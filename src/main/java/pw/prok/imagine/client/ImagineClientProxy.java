// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine.client;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;
import pw.prok.imagine.ImagineProxy;

public class ImagineClientProxy extends ImagineProxy
{
    @Override
    public EntityPlayer obtainPlayer(final INetHandler handler) {
        EntityPlayer player = super.obtainPlayer(handler);
        if (player == null) {
            player = (EntityPlayer)Minecraft.func_71410_x().field_71439_g;
        }
        return player;
    }
}
