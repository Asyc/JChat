package me.asyc.jchat.network.encryption.impl;

import me.asyc.jchat.network.encryption.CryptoKey;
import me.asyc.jchat.network.encryption.CryptoManager;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

/**
 * Implementation of {@link CryptoManager} utilizing <b>AES/CFB8/NoPadding</b> encryption
 */
public class CryptoManagerAES implements CryptoManager {

	protected final SecureRandom random;

	public CryptoManagerAES() {
		// Making sure that the Ciphers are available
		this.getCipherAES();
		this.getCipherRSA();

		this.random = new SecureRandom();
	}

	@Override
	public byte[] encrypt(CryptoKey key, byte[]... in) throws BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException, InvalidKeyException {
		Cipher cipher = this.getCipherAES();
		cipher.init(Cipher.ENCRYPT_MODE, key.getKey(), key.getParameterSpec());

		for (byte[] array : in) {
			cipher.update(array);
		}

		return cipher.doFinal();
	}

	@Override
	public byte[] decrypt(CryptoKey key, byte[]... in) throws BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException, InvalidKeyException {
		Cipher cipher = this.getCipherAES();
		cipher.init(Cipher.DECRYPT_MODE, key.getKey(), key.getParameterSpec());

		for (byte[] array : in) {
			cipher.update(array);
		}

		return cipher.doFinal();
	}

	@Override
	public byte[] encryptRSA(PublicKey key, byte[]... in) throws GeneralSecurityException {
		Cipher cipher = this.getCipherRSA();
		cipher.init(Cipher.ENCRYPT_MODE, key);

		for (byte[] array : in) {
			cipher.update(array);
		}

		return cipher.doFinal();
	}

	@Override
	public byte[] decryptRSA(PrivateKey key, byte[]... in) throws GeneralSecurityException {
		Cipher cipher = this.getCipherRSA();
		cipher.init(Cipher.DECRYPT_MODE, key);

		for (byte[] array : in) {
			cipher.update(array);
		}

		return cipher.doFinal();
	}

	@Override
	public CryptoKey generateKey() {
		byte[] buffer = new byte[128];
		this.random.nextBytes(buffer);
		return new CryptoKeyAES(new SecretKeySpec(buffer, "AES"), new IvParameterSpec(buffer));
	}

	@Override
	public CryptoKey createKeyFromBuffer(byte[] buffer) {
		return new CryptoKeyAES(new SecretKeySpec(buffer, "AES/CFB8/NoPadding"), new IvParameterSpec(buffer));
	}

	private Cipher getCipherAES() {
		try {
			return Cipher.getInstance("AES/CFB8/NoPadding");
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			e.printStackTrace();
			System.err.println("AES/CFB8/NoPadding Encryption is not Supported");
			System.exit(-1);
		}

		return null;
	}

	private Cipher getCipherRSA() {
		try {
			return Cipher.getInstance("RSA");
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			e.printStackTrace();
			System.err.println("RSA Encryption is not Supported");
			System.exit(-1);
		}

		return null;
	}
}
