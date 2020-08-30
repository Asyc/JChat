package me.asyc.jchat.network.encryption;

import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Class used to handle encrypting/decrypting network data
 */
public interface CryptoManager {
    /**
     * Encrypts the provided byte arrays with the provided secret key
     *
     * @param key The secret key to use for encryption
     * @param in A list of byte arrays that will be added to the {@link javax.crypto.Cipher}
     * @return Returns an encrypted byte array
     * @throws GeneralSecurityException Thrown if there was an error with encryption
     */
    byte[] encrypt(CryptoKey key, byte[]... in) throws GeneralSecurityException;

    /**
     * Decrypts the provided byte arrays with the provided secret key
     *
     * @param key The secret key to use for decryption
     * @param in A list of byte arrays that will be added to the {@link javax.crypto.Cipher}
     * @return Returns a decrypted byte array
     * @throws GeneralSecurityException Thrown if there was an error with decryption
     */
    byte[] decrypt(CryptoKey key, byte[]... in) throws GeneralSecurityException;

    /**
     * Encrypts the provided byte arrays with RSA encryption
     *
     * @param key The secret key to use for encryption
     * @param in A list of byte arrays that will be added to the {@link javax.crypto.Cipher}
     * @return Returns an encrypted byte array
     * @throws GeneralSecurityException Thrown if there was an error with encryption
     */
    byte[] encryptRSA(PublicKey key, byte[]...in) throws GeneralSecurityException;

    /**
     * Decrypts the provided byte arrays with RSA encryption
     *
     * @param key The secret key to use for decryption
     * @param in A list of byte arrays that will be added to the {@link javax.crypto.Cipher}
     * @return Returns a decrypted byte array
     * @throws GeneralSecurityException Thrown if there was an error with decryption
     */
    byte[] decryptRSA(PrivateKey key, byte[]... in) throws GeneralSecurityException;


    /**
     * Creates an existing key from a byte array
     *
     * @param buffer The data of the key
     * @return Creates a key from the provided byte array
     */
    CryptoKey createKeyFromBuffer(byte[] buffer);

    /**
     * @return Returns a newly generated {@link CryptoKey}
     */
    CryptoKey generateKey();
}
