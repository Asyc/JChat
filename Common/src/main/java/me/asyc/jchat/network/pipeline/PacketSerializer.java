package me.asyc.jchat.network.pipeline;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import me.asyc.jchat.network.buffer.MutableByteArrayOutputStream;
import me.asyc.jchat.network.packet.OutboundPacket;

import java.io.ByteArrayOutputStream;
import java.util.List;

@ChannelHandler.Sharable
public final class PacketSerializer extends MessageToMessageEncoder<OutboundPacket> {

    @Override
    protected void encode(ChannelHandlerContext ctx, OutboundPacket msg, List<Object> out) throws Exception {
        MutableByteArrayOutputStream buffer = new MutableByteArrayOutputStream(msg.getEstimatedSize() + 4); // Packet Size + Packet ID Size(4)
        buffer.write((msg.getPacketID() >>> 8) & 0xFF);
        buffer.write((msg.getPacketID()) & 0xFF);

        msg.write(ctx.channel(), buffer);
        out.add(buffer.getByteArray());
    }
}
