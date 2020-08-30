package me.asyc.jchat.client;

import me.asyc.jchat.client.encryption.ClientCryptoManager;
import me.asyc.jchat.client.input.InputManager;
import me.asyc.jchat.client.network.Connection;

public class JChatClient {

    public static final JChatClient INSTANCE = new JChatClient();

    private final ClientCryptoManager cryptoManager;
    private final InputManager inputManager;
    private Connection connection;

    public JChatClient() {
        this.cryptoManager = new ClientCryptoManager();
        this.inputManager = new InputManager(this::onInput);
    }

    public void shutdown() {
        this.inputManager.shutdown();
    }

    private void onInput(String message) {
        System.out.println(message);
    }

    public static void main(String[] args) {

    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public ClientCryptoManager getCryptoManager() {
        return cryptoManager;
    }
}
