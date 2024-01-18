package server;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class EncryptionPropertiesGenerator {

    public void generatePropertiesFile(String encryptionMethod, String transformation, String filePath) {
        Properties prop = new Properties();
        prop.setProperty("encryptionMethod", encryptionMethod);
        prop.setProperty("transformation", transformation);
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            prop.store(fos, null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        EncryptionPropertiesGenerator generator = new EncryptionPropertiesGenerator();
        generator.generatePropertiesFile("AES", "AES/CBC/PKCS5Padding", "encryption.properties");
    }
}