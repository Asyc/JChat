package me.asyc.jchat.network.encryption;

import javax.crypto.SecretKey;
import java.security.spec.AlgorithmParameterSpec;

public interface CryptoKey {
	SecretKey getKey();
	AlgorithmParameterSpec getParameterSpec();
}
