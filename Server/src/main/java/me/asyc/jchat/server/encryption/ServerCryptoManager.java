package me.asyc.jchat.server.encryption;

import me.asyc.jchat.network.encryption.impl.CryptoManagerAES;

import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;

public final class ServerCryptoManager extends CryptoManagerAES {

    private final KeyPair rsaKeyPair;

    public ServerCryptoManager() {
        super();

        KeyPair keyPair = null; // Have to defer with a temp variable
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(1024, super.random);
            keyPair = generator.generateKeyPair();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
            System.err.println("RSA Encryption Key Generation is Not Supported");
            System.exit(-1);
        }

        this.rsaKeyPair = keyPair;
    }

    public byte[] encryptRSA(byte[]... in) throws GeneralSecurityException {
        return super.encryptRSA(this.rsaKeyPair.getPublic(), in);
    }

    public byte[] decryptRSA(byte[]... in) throws GeneralSecurityException {
        return super.decryptRSA(this.rsaKeyPair.getPrivate(), in);
    }

    public KeyPair getKeyPairRSA() {
        return this.rsaKeyPair;
    }
}
