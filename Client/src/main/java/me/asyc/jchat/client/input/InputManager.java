package me.asyc.jchat.client.input;

import me.asyc.jchat.client.JChatClient;

import java.util.Scanner;
import java.util.function.Consumer;

public class InputManager {

    private final Thread inputThread;

    private final Consumer<String> inputCallback;

    private String ip;
    private int port;

    public InputManager(Consumer<String> callback) {
        this.inputCallback = callback;

        this.inputThread = new Thread(() -> {
            Scanner scanner = new Scanner(System.in);

            while (!Thread.currentThread().isInterrupted()) {
                String input = scanner.nextLine();
                InputManager.this.inputCallback.accept(input);
            }
        });

        this.inputThread.start();
    }

    private void processInput(String in) {
        if (JChatClient.INSTANCE.getConnection() == null) {
            this.inputCallback.accept(in);
            return;
        }

        if (this.ip == null) {
            System.out.println("Enter Server IP: ");
        }

    }

    public void shutdown() {
        this.inputThread.interrupt();
        try {
            this.inputThread.join();
        } catch (InterruptedException ignore) {}
    }
}
