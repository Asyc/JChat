package me.asyc.jchat.client.encryption;

import me.asyc.jchat.network.encryption.impl.CryptoManagerAES;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public final class ClientCryptoManager extends CryptoManagerAES {

    private final KeyFactory keyFactory;

    public ClientCryptoManager() {
        super();

        KeyFactory factory = null; // Have to defer with a temp variable
        try {
            factory = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            System.err.println("RSA Encryption is not Supported");
            System.exit(-1);
        }

        this.keyFactory = factory;
    }

    public PublicKey createKeyFromBufferRSA(byte[] in) throws InvalidKeySpecException {
        return this.keyFactory.generatePublic(new X509EncodedKeySpec(in));
    }
}
