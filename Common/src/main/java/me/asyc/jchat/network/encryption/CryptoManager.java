package me.asyc.jchat.network.encryption;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;

public interface CryptoManager {
	byte[] encrypt(CryptoKey key, byte[]... in) throws BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException, InvalidKeyException;
	byte[] decrypt(CryptoKey key, byte[]... in) throws BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException, InvalidKeyException;

	byte[] encryptHandshake(byte[]... in);
	byte[] decryptHandshake(byte[]... in);

	int getFrameSize();

	CryptoKey createKey(byte[] buffer);
	CryptoKey generateKey();

	KeyPair getHandshakeKeyPair();
}
