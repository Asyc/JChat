package me.asyc.jchat.server.network.client;

import io.netty.channel.socket.SocketChannel;
import me.asyc.jchat.server.JChatServer;
import me.asyc.jchat.server.network.encryption.CryptoKey;

public final class User {

	private final SocketChannel socket;
	private final CryptoKey cryptoKey;

	private final Object writeLock;

	public User(SocketChannel socket) {
		this.socket = socket;
		this.cryptoKey = JChatServer.INSTANCE.getCryptoManager().generateKey();
		this.writeLock = new Object();
	}

	public void write(Packet packet) {
		synchronized (this.writeLock) {
			this.socket.writeAndFlush(packet).awaitUninterruptibly();
		}
	}

	public CryptoKey getCryptoKey() {
		return cryptoKey;
	}

	public SocketChannel getSocket() {
		return socket;
	}
}
