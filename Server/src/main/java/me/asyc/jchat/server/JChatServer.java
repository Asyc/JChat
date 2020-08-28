package me.asyc.jchat.server;

import me.asyc.jchat.network.encryption.CryptoManager;
import me.asyc.jchat.network.encryption.impl.BasicCryptoManager;


public class JChatServer {

	public static final JChatServer INSTANCE = new JChatServer();

	private final CryptoManager cryptoManager;

	private JChatServer() {
		System.out.println("Starting Server");
		this.cryptoManager = new BasicCryptoManager();
	}

	public CryptoManager getCryptoManager() {
		return this.cryptoManager;
	}

	public static void main(String[] args) throws ClassNotFoundException {
		Class.forName("me.asyc.jchat.server.JChatServer");
	}

}
