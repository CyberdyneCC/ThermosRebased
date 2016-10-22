// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine.network;

import java.util.HashMap;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import net.minecraft.world.World;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.relauncher.Side;
import io.netty.channel.ChannelHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import pw.prok.imagine.pool.Pools;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import io.netty.util.AttributeKey;
import pw.prok.imagine.pool.Pool;
import java.util.Map;

public class ImagineNetwork
{
    private static final Map<Class<?>, Pool<? extends ImaginePacket>> sPacketPool;
    public static final AttributeKey<ImagineNetwork> PACKET_NETWORK;
    private final FMLEmbeddedChannel mServerChannel;
    private final FMLEmbeddedChannel mClientChannel;
    
    public static <T extends ImaginePacket> T obtainPacket(final Class<T> packetClass) {
        Pool<T> pool = (Pool<T>)ImagineNetwork.sPacketPool.get(packetClass);
        if (pool == null) {
            synchronized (ImagineNetwork.sPacketPool) {
                pool = (Pool<T>)ImagineNetwork.sPacketPool.get(packetClass);
                if (pool == null) {
                    pool = Pools.create(packetClass, new Object[0]);
                    ImagineNetwork.sPacketPool.put((Class<?>)packetClass, (Pool<?>)pool);
                }
            }
        }
        return pool.obtain();
    }
    
    public static <T extends ImaginePacket> void releasePacket(final T packet) {
        final Pool<T> pool = (Pool<T>)ImagineNetwork.sPacketPool.get(packet.getClass());
        pool.release(packet);
    }
    
    public ImagineNetwork(final String channelName) {
        final Map<Side, FMLEmbeddedChannel> channels = (Map<Side, FMLEmbeddedChannel>)NetworkRegistry.INSTANCE.newChannel(channelName, new ChannelHandler[] { new ImaginePacketCodec(this), new ImaginePacketHandler() });
        this.mServerChannel = channels.get(Side.SERVER);
        this.mClientChannel = channels.get(Side.CLIENT);
    }
    
    public void sendAllAround(final ImaginePacket packet, final TileEntity tileEntity, final double range) {
        this.sendAllAround(packet, new NetworkRegistry.TargetPoint(tileEntity.func_145831_w().field_73011_w.field_76574_g, (double)tileEntity.field_145851_c, (double)tileEntity.field_145848_d, (double)tileEntity.field_145849_e, range));
    }
    
    public void sendAllAround(final ImaginePacket packet, final World world, final double x, final double y, final double z, final double range) {
        this.sendAllAround(packet, new NetworkRegistry.TargetPoint(world.field_73011_w.field_76574_g, x, y, z, range));
    }
    
    public void sendAllAround(final ImaginePacket packet, final NetworkRegistry.TargetPoint point) {
        this.mServerChannel.attr(FMLOutboundHandler.FML_MESSAGETARGET).set((Object)FMLOutboundHandler.OutboundTarget.ALLAROUNDPOINT);
        this.mServerChannel.attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set((Object)point);
        this.mServerChannel.writeOutbound(new Object[] { packet });
    }
    
    public void sendToPlayer(final ImaginePacket packet, final EntityPlayer player) {
        this.mServerChannel.attr(FMLOutboundHandler.FML_MESSAGETARGET).set((Object)FMLOutboundHandler.OutboundTarget.PLAYER);
        this.mServerChannel.attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set((Object)player);
        this.mServerChannel.writeOutbound(new Object[] { packet });
    }
    
    public void sendToWorld(final ImaginePacket packet, final World world) {
        this.mServerChannel.attr(FMLOutboundHandler.FML_MESSAGETARGET).set((Object)FMLOutboundHandler.OutboundTarget.DIMENSION);
        this.mServerChannel.attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set((Object)world.field_73011_w.field_76574_g);
        this.mServerChannel.writeOutbound(new Object[] { packet });
    }
    
    public void sendToAll(final ImaginePacket packet) {
        this.mServerChannel.attr(FMLOutboundHandler.FML_MESSAGETARGET).set((Object)FMLOutboundHandler.OutboundTarget.ALL);
        this.mServerChannel.writeOutbound(new Object[] { packet });
    }
    
    public void send(final ImaginePacket packet) {
        this.mClientChannel.attr(FMLOutboundHandler.FML_MESSAGETARGET).set((Object)FMLOutboundHandler.OutboundTarget.TOSERVER);
        this.mClientChannel.writeOutbound(new Object[] { packet });
    }
    
    static {
        sPacketPool = new HashMap<Class<?>, Pool<? extends ImaginePacket>>();
        PACKET_NETWORK = new AttributeKey("imagine:network");
    }
}
