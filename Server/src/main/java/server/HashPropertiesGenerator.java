package server;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class HashPropertiesGenerator {

    public void generatePropertiesFile(String hashAlgorithm, String filePath) {
        Properties prop = new Properties();
        prop.setProperty("hashAlgorithm", hashAlgorithm);
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            prop.store(fos, null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        HashPropertiesGenerator generator = new HashPropertiesGenerator();
        generator.generatePropertiesFile("SHA-256", "hashing.properties");
    }
}