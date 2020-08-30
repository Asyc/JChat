package me.asyc.jchat.server;

import me.asyc.jchat.server.encryption.ServerCryptoManager;

public class JChatServer {
    public static final JChatServer INSTANCE = new JChatServer();

    private final ServerCryptoManager cryptoManager;

    private JChatServer() {
        System.out.println("Starting Server");
        this.cryptoManager = new ServerCryptoManager();
    }

    public ServerCryptoManager getCryptoManager() {
        return this.cryptoManager;
    }

    public static void main(String[] args) throws ClassNotFoundException {
        Class.forName("me.asyc.jchat.server.JChatServer");
    }
}
