package me.asyc.jchat.network.encryption.impl;

import me.asyc.jchat.network.encryption.CryptoKey;
import me.asyc.jchat.network.encryption.CryptoManager;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public final class BasicCryptoManager implements CryptoManager {

	private final SecureRandom random;
	private final KeyPair rsaKeyPair;

	public BasicCryptoManager() {
		// Making sure that the Ciphers are available
		System.out.println("Preparing Encryption");
		this.getCipherAES();

		this.random = new SecureRandom();
		this.rsaKeyPair = this.generateKeyPairRSA();  //This will make sure RSA is available
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
	public byte[] encryptHandshake(byte[]... in) {
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, this.rsaKeyPair.getPublic());

			for (byte[] array : in) {
				cipher.update(array);
			}

			return cipher.doFinal();
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public byte[] decryptHandshake(byte[]... in) {
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, this.rsaKeyPair.getPrivate());

			for (byte[] array : in) {
				cipher.update(array);
			}

			return cipher.doFinal();
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public int getFrameSize() {
		return 1024;
	}

	@Override
	public CryptoKey createKey(byte[] buffer) {
		return new BasicCryptoKey(new SecretKeySpec(buffer, "AES/CFB8/NoPadding"), new IvParameterSpec(buffer));
	}

	@Override
	public CryptoKey generateKey() {
		byte[] buffer = new byte[128];
		this.random.nextBytes(buffer);
		return new BasicCryptoKey(new SecretKeySpec(buffer, "AES"), new IvParameterSpec(buffer));
	}

	@Override
	public KeyPair getHandshakeKeyPair() {
		return this.rsaKeyPair;
	}

	private Cipher getCipherAES() {
		try {
			return Cipher.getInstance("AES/CFB8/NoPadding");
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			e.printStackTrace();
			System.err.println("AES/CFB8/NoPadding is not Supported");
			System.exit(-1);
		}

		return null;
	}

	private KeyPair generateKeyPairRSA() {
		try {
			KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
			generator.initialize(1024, this.random);
			return generator.generateKeyPair();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			System.err.println("RSA is not Supported");
		}

		return null;
	}
}
