package service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.util.Base64;
import java.util.Properties;

public class EncryptionService {
    private SecretKeySpec secretKey;
    private String encryptionMethod;
    private String transformation;

    public EncryptionService(String keyStorePath, String keyStorePassword, String keyPassword, String propertiesFilePath) {
        try {
            KeyStore keyStore = KeyStore.getInstance("JCEKS");
            keyStore.load(new FileInputStream(keyStorePath), keyStorePassword.toCharArray());

            KeyStore.SecretKeyEntry secretKeyEntry = (KeyStore.SecretKeyEntry) keyStore.getEntry("secretKeyAlias", new KeyStore.PasswordProtection(keyPassword.toCharArray()));
            SecretKey secretKey = secretKeyEntry.getSecretKey();

            this.secretKey = new SecretKeySpec(secretKey.getEncoded(), "AES");

            // Load the properties file
            Properties prop = new Properties();
            prop.load(new FileInputStream(propertiesFilePath));

            // Retrieve the encryption method and transformation from the properties file
            this.encryptionMethod = prop.getProperty("encryptionMethod");
            this.transformation = prop.getProperty("transformation");
        } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException | UnrecoverableEntryException e) {
            throw new RuntimeException(e);
        }
    }

    public String encrypt(String message) {
        try {
            Cipher cipher = Cipher.getInstance(transformation);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedMessage = cipher.doFinal(message.getBytes());
            return Base64.getEncoder().encodeToString(encryptedMessage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String decrypt(String encryptedMessage) {
        try {
            byte[] bytes = Base64.getDecoder().decode(encryptedMessage);
            Cipher cipher = Cipher.getInstance(transformation);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decryptedMessage = cipher.doFinal(bytes);
            return new String(decryptedMessage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}