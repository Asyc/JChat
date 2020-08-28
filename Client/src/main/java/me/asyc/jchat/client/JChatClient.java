package me.asyc.jchat.client;

import me.asyc.jchat.network.encryption.CryptoManager;

public class JChatClient {

	public static final JChatClient INSTANCE = new JChatClient();

	private CryptoManager cryptoManager;

	public JChatClient() {

		

	}

	public static void main(String[] args) {

	}

	public CryptoManager getCryptoManager() {
		return cryptoManager;
	}
}
