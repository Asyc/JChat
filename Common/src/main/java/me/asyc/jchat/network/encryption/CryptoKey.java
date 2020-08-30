package me.asyc.jchat.network.encryption;

import javax.crypto.SecretKey;
import java.security.spec.AlgorithmParameterSpec;

/**
 * Interface where a client's cryptography keys are stored.
 * Used in the {@link CryptoManager}.
 */
public interface CryptoKey {
	SecretKey getKey();
	AlgorithmParameterSpec getParameterSpec();
}
