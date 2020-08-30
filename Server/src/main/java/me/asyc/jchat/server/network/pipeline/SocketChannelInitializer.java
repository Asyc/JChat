package me.asyc.jchat.server.network.pipeline;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import me.asyc.jchat.network.encryption.CryptoManager;
import me.asyc.jchat.network.packet.factory.PacketResolver;
import me.asyc.jchat.network.pipeline.PacketDeserializer;
import me.asyc.jchat.network.pipeline.PacketFrameEncryptor;
import me.asyc.jchat.network.pipeline.PacketFrameDecryptor;
import me.asyc.jchat.network.pipeline.PacketSerializer;
import me.asyc.jchat.server.JChatServer;
import me.asyc.jchat.server.network.packet.impl.outbound.ServerHandshakePacket;

import java.util.Random;

public final class SocketChannelInitializer extends ChannelInitializer<SocketChannel> {

	private static final Random RANDOM = new Random();

	private static final PacketSerializer PACKET_SERIALIZER = new PacketSerializer();
	private static final MessageHandler PACKET_HANDLER = new MessageHandler();

	private final PacketDeserializer packetDeserializer;
	private final CryptoManager cryptoManager;

	public SocketChannelInitializer(CryptoManager cryptoManager, PacketResolver factory) {
		this.cryptoManager = cryptoManager;
		this.packetDeserializer = new PacketDeserializer(factory);
	}

	@Override
	protected void initChannel(SocketChannel channel) {
		channel.pipeline()
			.addLast("framer/encryptor", new PacketFrameEncryptor(this.cryptoManager, this.cryptoManager.generateKey()))
			.addLast("framer/decryptor", new PacketFrameDecryptor(this.cryptoManager, this.cryptoManager.generateKey()))
			.addLast("serializer", SocketChannelInitializer.PACKET_SERIALIZER)
			.addLast("deserializer", this.packetDeserializer)
			.addLast("handler", SocketChannelInitializer.PACKET_HANDLER);

		byte[] validate = new byte[128];
		SocketChannelInitializer.RANDOM.nextBytes(validate);

		channel.writeAndFlush(new ServerHandshakePacket(JChatServer.INSTANCE.getCryptoManager().getKeyPairRSA().getPublic().getEncoded(), validate));
	}

}
