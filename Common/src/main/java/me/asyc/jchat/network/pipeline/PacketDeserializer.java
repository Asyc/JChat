package me.asyc.jchat.network.pipeline;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import me.asyc.jchat.network.exception.UnknownPacketException;
import me.asyc.jchat.network.packet.factory.GlobalPacketFactory;
import me.asyc.jchat.network.packet.Packet;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.List;

@ChannelHandler.Sharable
public final class PacketDeserializer extends MessageToMessageDecoder<byte[]> {

	private final GlobalPacketFactory factory;

	public PacketDeserializer(GlobalPacketFactory factory) {
		this.factory = factory;
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, byte[] msg, List<Object> out) throws Exception {
		DataInputStream in = new DataInputStream(new ByteArrayInputStream(msg));

		short packetID = in.readShort();

		if (packetID > this.factory.maxPacketID()) throw new UnknownPacketException();

		Packet packet = this.factory.createPacketFromID(packetID);
		packet.read(ctx.channel(), msg);
		out.add(packet);
	}

}
