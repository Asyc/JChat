package me.asyc.jchat.network.pipeline;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.io.ByteArrayOutputStream;
import java.util.List;

@ChannelHandler.Sharable
public final class PacketSerializer extends MessageToMessageEncoder<Packet> {

	@Override
	protected void encode(ChannelHandlerContext ctx, Packet msg, List<Object> out) throws Exception {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream(34);
		buffer.write((msg.getPacketID() >>> 8) & 0xFF);
		buffer.write((msg.getPacketID()) & 0xFF);

		msg.write(buffer);
		out.add(buffer.toByteArray());
	}
}
