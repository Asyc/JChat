package me.asyc.jchat.server.network.packet.impl.in;

import io.netty.channel.Channel;
import me.asyc.jchat.network.packet.InboundPacket;
import me.asyc.jchat.network.pipeline.ChannelAttributes;
import me.asyc.jchat.server.JChatServer;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Arrays;

public class ClientHandshakePacket implements InboundPacket {

	private Channel channel;
	private byte[] key;
	private byte[] validate, originalValidate;

	@Override
	public void read(Channel channel, byte[] buffer) throws IOException {
		this.channel = channel;

		DataInputStream in = new DataInputStream(new ByteArrayInputStream(buffer));
		this.key = new byte[in.readInt()];
		in.readFully(this.key);
		this.validate = new byte[in.readInt()];
		in.readFully(this.validate);

		this.originalValidate = channel.attr(ChannelAttributes.VALIDATE_BUFFER).getAndSet(null);
	}

	@Override
	public void handle() {
		if (!Arrays.equals(this.originalValidate, this.validate)) {
			System.err.println("Client(" + this.channel.remoteAddress().toString() + "): Handshake secret does not match original secret. Disconnecting.");
			this.channel.close();
		}

		this.channel.attr(ChannelAttributes.KEY).set(JChatServer.INSTANCE.getCryptoManager().createKey(this.key));
	}

	@Override
	public short getPacketID() {
		return 0x01;
	}
}
