package me.asyc.jchat.network.encryption.impl;

import me.asyc.jchat.network.encryption.CryptoKey;

import javax.crypto.SecretKey;
import java.security.spec.AlgorithmParameterSpec;

/**
 * Simple container class for an <b>AES Key</b>.
 */
public final class CryptoKeyAES implements CryptoKey {

    private final SecretKey key;
    private final AlgorithmParameterSpec algorithmParameterSpec;

    public CryptoKeyAES(SecretKey key, AlgorithmParameterSpec algorithmParameterSpec) {
        this.key = key;
        this.algorithmParameterSpec = algorithmParameterSpec;
    }

    @Override
    public SecretKey getKey() {
        return this.key;
    }

    @Override
    public AlgorithmParameterSpec getParameterSpec() {
        return this.algorithmParameterSpec;
    }
}
