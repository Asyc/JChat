package me.asyc.jchat.network.pipeline;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import me.asyc.jchat.network.buffer.ByteArray;
import me.asyc.jchat.network.encryption.CryptoKey;
import me.asyc.jchat.network.encryption.CryptoManager;


import java.util.List;

public final class PacketFrameDecryptor extends ByteToMessageDecoder {

	private final CryptoManager manager;
	private final CryptoKey key;

	private final ByteArray header;
	private ByteArray packetBuffer;

	public PacketFrameDecryptor(CryptoManager manager, CryptoKey key) {
		this.manager = manager;
		this.key = key;

		this.header = new ByteArray(4);
		this.packetBuffer = null;
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		while (in.readableBytes() > 0) {
			byte[] decoded = this.decode(in);
			if (decoded != null) out.add(decoded);
		}
	}

	private byte[] decode(ByteBuf in) throws Exception {
		if (!this.header.full()) {
			int toRead = Math.min(this.header.remaining(), in.readableBytes());

			in.readBytes(this.header.get(), this.header.side(), toRead);
			this.header.incrementSize(toRead);

			if (!this.header.full()) return null;

			byte[] sizeBuffer = this.manager.decrypt(this.key, this.header.get());
			this.packetBuffer = new ByteArray(((sizeBuffer[0] << 24) + (sizeBuffer[1] << 16) + (sizeBuffer[2] << 8) + (sizeBuffer[3])));
		}

		if (!this.packetBuffer.full()) {
			int toRead = Math.min(this.packetBuffer.remaining(), in.readableBytes());

			in.readBytes(this.packetBuffer.get(), this.packetBuffer.side(), toRead);
			this.packetBuffer.incrementSize(toRead);

			if (!this.packetBuffer.full()) return null;
		}

		byte[] decrypted = this.manager.decrypt(this.key, this.header.get(), this.packetBuffer.get());

		this.header.reset();
		this.packetBuffer = null;

		return decrypted;
	}

}
