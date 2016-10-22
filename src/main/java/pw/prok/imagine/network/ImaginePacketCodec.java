// 
// Decompiled by Procyon v0.5.30
// 

package pw.prok.imagine.network;

import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.NetworkRegistry;
import pw.prok.imagine.writer.WritableBuf;
import io.netty.buffer.Unpooled;
import java.util.List;
import io.netty.channel.ChannelHandlerContext;
import java.nio.charset.Charset;
import java.lang.ref.WeakReference;
import io.netty.util.AttributeKey;
import io.netty.channel.ChannelHandler;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import io.netty.handler.codec.MessageToMessageCodec;

@ChannelHandler.Sharable
public class ImaginePacketCodec extends MessageToMessageCodec<FMLProxyPacket, ImaginePacket>
{
    public static final AttributeKey<ThreadLocal<WeakReference<FMLProxyPacket>>> PACKET_TRACKER;
    public static final AttributeKey<ImaginePacketRegistry> PACKET_REGISTRY;
    private static final Charset UTF_8;
    private final ImagineNetwork mNetwork;
    
    public ImaginePacketCodec(final ImagineNetwork network) {
        this.mNetwork = network;
    }
    
    protected void encode(final ChannelHandlerContext ctx, final ImaginePacket packet, final List<Object> out) throws Exception {
        final WritableBuf buffer = new WritableBuf(Unpooled.buffer());
        final ImaginePacketRegistry packetRegistry = (ImaginePacketRegistry)ctx.attr((AttributeKey)ImaginePacketCodec.PACKET_REGISTRY).get();
        final Class<? extends ImaginePacket> packetClass = packet.getClass();
        final int packetClassId = packetRegistry.id(packetClass);
        if (packetRegistry.missing(packetClass)) {
            packetRegistry.register(packetClassId, packetClass);
            buffer.writeBoolean(true);
            buffer.writeInt(packetClassId);
            final byte[] classNameBytes = packetClass.getName().getBytes(ImaginePacketCodec.UTF_8);
            final int classNameLength = classNameBytes.length;
            buffer.writeInt(classNameLength);
            buffer.writeBytes(classNameBytes, 0, classNameLength);
        }
        else {
            buffer.writeBoolean(false);
            buffer.writeInt(packetClassId);
        }
        packet.writePacket(ctx, buffer);
        final FMLProxyPacket proxy = new FMLProxyPacket((ByteBuf)buffer, (String)ctx.channel().attr(NetworkRegistry.FML_CHANNEL).get());
        final WeakReference<FMLProxyPacket> ref = ((ThreadLocal)ctx.attr((AttributeKey)ImaginePacketCodec.PACKET_TRACKER).get()).get();
        final FMLProxyPacket old = (ref == null) ? null : ref.get();
        if (old != null) {
            proxy.setDispatcher(old.getDispatcher());
        }
        out.add(proxy);
    }
    
    protected void decode(final ChannelHandlerContext ctx, final FMLProxyPacket msg, final List<Object> out) throws Exception {
        ((ThreadLocal)ctx.attr((AttributeKey)ImaginePacketCodec.PACKET_TRACKER).get()).set(new WeakReference<FMLProxyPacket>(msg));
        final ImaginePacketRegistry packetRegistry = (ImaginePacketRegistry)ctx.attr((AttributeKey)ImaginePacketCodec.PACKET_REGISTRY).get();
        final ByteBuf payload = msg.payload();
        final boolean newPacketClass = payload.readBoolean();
        final int packetClassId = payload.readInt();
        Class<? extends ImaginePacket> packetClass;
        if (newPacketClass) {
            final int classNameLength = payload.readInt();
            if (classNameLength <= 0) {
                new IllegalArgumentException("Too short class name: " + classNameLength).printStackTrace();
                ctx.close();
            }
            if (classNameLength > 1024) {
                new IllegalArgumentException("Too long class name: " + classNameLength).printStackTrace();
                ctx.close();
            }
            final byte[] classNameBytes = new byte[classNameLength];
            payload.readBytes(classNameBytes, 0, classNameLength);
            final String className = new String(classNameBytes, 0, classNameLength, ImaginePacketCodec.UTF_8);
            packetClass = (Class<? extends ImaginePacket>)Class.forName(className);
            if (!ImaginePacket.class.isAssignableFrom(packetClass)) {
                new IllegalArgumentException("Provided class isn't imagine packet: " + packetClass).printStackTrace();
                ctx.close();
            }
            packetRegistry.register(packetClassId, packetClass);
        }
        else {
            packetClass = packetRegistry.get(packetClassId);
        }
        if (packetClass == null) {
            new NullPointerException("Undefined message in channel " + msg.channel()).printStackTrace();
            ctx.close();
        }
        final ImaginePacket packet = ImagineNetwork.obtainPacket(packetClass);
        packet.readPacket(ctx, new WritableBuf(payload.slice()));
        out.add(packet);
    }
    
    public void handlerAdded(final ChannelHandlerContext ctx) throws Exception {
        super.handlerAdded(ctx);
        ctx.attr((AttributeKey)ImaginePacketCodec.PACKET_TRACKER).set((Object)new ThreadLocal());
        ctx.attr((AttributeKey)ImaginePacketCodec.PACKET_REGISTRY).set((Object)new ImaginePacketRegistry());
    }
    
    static {
        PACKET_TRACKER = new AttributeKey("imagine:inboundpacket");
        PACKET_REGISTRY = new AttributeKey("imagine:packet_registry");
        UTF_8 = Charset.forName("utf-8");
    }
}
