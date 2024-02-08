package service;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Properties;

public class HashService {
    private String hashAlgorithm;

    public HashService(InputStream propertiesStream) {
        try {
            // Load the properties file
            Properties prop = new Properties();
            prop.load(propertiesStream);

            // Retrieve the hash algorithm from the properties file
            this.hashAlgorithm = prop.getProperty("hashAlgorithm");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance(hashAlgorithm);
            byte[] hashedPassword = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}