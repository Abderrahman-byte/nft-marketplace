package com.stibits.rnft.common.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AESUtils {
    private static final String DEFAULT_ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final byte[] DEFAULT_IV_BYTES = new byte[16];
    private static final IvParameterSpec DEFAULT_PARAMETER_SPEC;

    static {
        new SecureRandom().nextBytes(DEFAULT_IV_BYTES);
        DEFAULT_PARAMETER_SPEC = new IvParameterSpec(DEFAULT_IV_BYTES);
    }

    public static SecretKey generateSecretKey (String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] passwordHash = digest.digest(password.getBytes());
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(password.toCharArray(), passwordHash, 65536, 256);
            SecretKey secretKey = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");

            return secretKey;
        } catch (NoSuchAlgorithmException | UnsupportedOperationException | InvalidKeySpecException ex ) {
            log.error("[ERROR-generateSecretKey] " + ex.getMessage(), ex);
            return null;
        }
    }

    public static IvParameterSpec generateIv () {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    public static IvParameterSpec generateIv (byte iv[]) {
        return new IvParameterSpec(iv);
    }

    public static String encrypt (String password, String input) {
        return encrypt(password, input, DEFAULT_PARAMETER_SPEC);
    }

    public static byte[] decrypt (String password, String cipherText) {
        return decrypt(password, cipherText, DEFAULT_PARAMETER_SPEC);
    }

    public static String decryptToString (String password, String cipherText) {
        return decryptToString(password, cipherText, DEFAULT_PARAMETER_SPEC);
    } 

    public static String encrypt (String password, String input, IvParameterSpec iv) {
        try {
            Cipher cipher = Cipher.getInstance(DEFAULT_ALGORITHM);
            SecretKey secretKey = generateSecretKey(password);

            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);

            byte[] cipherText = cipher.doFinal(input.getBytes());

            return Base64.getEncoder().encodeToString(cipherText);
        } catch (Exception ex) {
            log.error("["+ ex.getClass()+"] " + ex.getMessage(), ex);
            return null;
        }
    }

    public static String decryptToString (String password, String cipherText, IvParameterSpec iv) {
        byte[] decrypted = decrypt(password, cipherText, iv);

        if (decrypted == null) return null;

        return new String(decrypted);
    }

    public static byte[] decrypt (String password, String cipherText, IvParameterSpec iv) {
        try {
            Cipher cipher = Cipher.getInstance(DEFAULT_ALGORITHM);
            SecretKey secretKey = generateSecretKey(password);

            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);

            byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(cipherText));

            return decrypted;
        } catch (Exception ex) {
            log.error("["+ ex.getClass()+"] " + ex.getMessage(), ex);
            return null;
        }
    }
}
