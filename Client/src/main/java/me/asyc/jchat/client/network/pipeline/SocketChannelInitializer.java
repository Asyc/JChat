package me.asyc.jchat.client.network.pipeline;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import me.asyc.jchat.network.encryption.CryptoManager;
import me.asyc.jchat.network.packet.factory.GlobalPacketFactory;
import me.asyc.jchat.network.pipeline.PacketDeserializer;
import me.asyc.jchat.network.pipeline.PacketEncryptFramer;
import me.asyc.jchat.network.pipeline.PacketFrameDecryptor;
import me.asyc.jchat.network.pipeline.PacketSerializer;

public final class SocketChannelInitializer extends ChannelInitializer<SocketChannel> {

	private static final PacketSerializer PACKET_SERIALIZER = new PacketSerializer();
	private static final MessageHandler PACKET_HANDLER = new MessageHandler();

	private final PacketDeserializer packetDeserializer;
	private final CryptoManager cryptoManager;

	public SocketChannelInitializer(CryptoManager cryptoManager, GlobalPacketFactory factory) {
		this.cryptoManager = cryptoManager;
		this.packetDeserializer = new PacketDeserializer(factory);
	}

	@Override
	protected void initChannel(SocketChannel channel) {
		channel.pipeline()
			.addLast("framer/encryptor", new PacketEncryptFramer(this.cryptoManager, this.cryptoManager.generateKey()))
			.addLast("framer/decryptor", new PacketFrameDecryptor(this.cryptoManager, this.cryptoManager.generateKey()))
			.addLast("serializer", SocketChannelInitializer.PACKET_SERIALIZER)
			.addLast("deserializer", this.packetDeserializer)
			.addLast("handler", SocketChannelInitializer.PACKET_HANDLER);
	}

}
