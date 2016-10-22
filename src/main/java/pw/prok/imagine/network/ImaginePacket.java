// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine.network;

import cpw.mods.fml.relauncher.SideOnly;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import pw.prok.imagine.writer.WritableBuf;
import io.netty.channel.ChannelHandlerContext;

public abstract class ImaginePacket
{
    public void writePacket(final ChannelHandlerContext ctx, final WritableBuf buf) {
    }
    
    public void readPacket(final ChannelHandlerContext ctx, final WritableBuf buf) {
    }
    
    public void process(final ChannelHandlerContext ctx, final EntityPlayer player) {
        final Side side = (Side)ctx.channel().attr(NetworkRegistry.CHANNEL_SOURCE).get();
        if (side.isClient()) {
            this.processClient(ctx, player);
        }
        else {
            this.processServer(ctx, player);
        }
    }
    
    @SideOnly(Side.CLIENT)
    public void processClient(final ChannelHandlerContext ctx, final EntityPlayer player) {
        new UnsupportedOperationException("Packet not support client execution").printStackTrace();
    }
    
    public void processServer(final ChannelHandlerContext ctx, final EntityPlayer player) {
        new UnsupportedOperationException("Packet not support server execution").printStackTrace();
    }
}
