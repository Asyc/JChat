package me.asyc.jchat.network.pipeline;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import me.asyc.jchat.network.buffer.MutableByteArrayOutputStream;
import me.asyc.jchat.network.packet.OutboundPacket;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Used to serialize an {@link OutboundPacket} to a byte array, then pass it to the {@link PacketFrameEncryptor}
 */
@ChannelHandler.Sharable
public final class PacketSerializer extends MessageToMessageEncoder<OutboundPacket> {

    /**
     * Calls {@link OutboundPacket#write(Channel, OutputStream)} to populate a growing byte buffer.
     * This also appends a two byte(short) header of the packet ID.
     *
     * @param ctx The channel context
     * @param msg The message to serialize
     * @param out The out objects list
     * @throws Exception Thrown if the packet could not be serialized
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, OutboundPacket msg, List<Object> out) throws Exception {
        MutableByteArrayOutputStream buffer = new MutableByteArrayOutputStream(msg.getEstimatedSize() + 4); // Packet Size + Packet ID Size(4)
        buffer.write((msg.getPacketID() >>> 8) & 0xFF);
        buffer.write((msg.getPacketID()) & 0xFF);

        msg.write(ctx.channel(), buffer);
        out.add(buffer.getByteArray());
    }
}
