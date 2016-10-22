// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine.network;

import pw.prok.imagine.ImagineModContainer;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.network.INetHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelHandler;
import io.netty.channel.SimpleChannelInboundHandler;

@ChannelHandler.Sharable
public class ImaginePacketHandler extends SimpleChannelInboundHandler<ImaginePacket>
{
    protected void channelRead0(final ChannelHandlerContext ctx, final ImaginePacket packet) throws Exception {
        final INetHandler netHandler = (INetHandler)ctx.channel().attr(NetworkRegistry.NET_HANDLER).get();
        packet.process(ctx, ImagineModContainer.proxy().obtainPlayer(netHandler));
        ImagineNetwork.releasePacket(packet);
    }
}
