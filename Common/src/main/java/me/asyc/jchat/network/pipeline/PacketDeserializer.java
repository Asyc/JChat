package me.asyc.jchat.network.pipeline;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import me.asyc.jchat.network.exception.UnknownPacketException;
import me.asyc.jchat.network.packet.InboundPacket;
import me.asyc.jchat.network.packet.factory.PacketResolver;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.List;

/**
 * After the {@link PacketFrameDecryptor} passes on a byte array, this class
 * turns that byte array into a {@link InboundPacket}
 *
 * The first two bytes(short) of the payload is the packet's unique identifier.
 *
 */
@ChannelHandler.Sharable
public final class PacketDeserializer extends MessageToMessageDecoder<byte[]> {

    private final PacketResolver factory;

    public PacketDeserializer(PacketResolver factory) {
        this.factory = factory;
    }

    /**
     *
     * This method takes in a byte array, and parses it to a {@link InboundPacket} then
     * sends it down the netty pipeline.
     *
     * @param ctx The channel context
     * @param msg The packet payload
     * @param out The object output list
     * @throws Exception Throws an exception if the Packet could not be de-serialized
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, byte[] msg, List<Object> out) throws Exception {
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(msg));

        short packetID = in.readShort();

        InboundPacket packet = this.factory.createPacketFromID(packetID);

        if (packet == null) throw new UnknownPacketException();

        packet.read(ctx.channel(), msg);
        out.add(packet);
    }

}
