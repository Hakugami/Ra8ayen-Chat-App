package token;

import utils.EncryptionService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class TokenManager {
    private static TokenManager instance;
    private static final String TOKEN_FILE = System.getProperty("user.dir") + File.separator + "token.config";
    private static final String AES_KEY = "aesEncryptionKey"; // This should be replaced with a secure key

    private TokenManager() {
    }

    public static TokenManager getInstance() {
        if (instance == null) {
            instance = new TokenManager();
        }
        return instance;
    }

    public void setToken(String token) {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(TOKEN_FILE)))) {
            String encryptedToken = EncryptionService.encrypt(token, AES_KEY);
            out.println(encryptedToken);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getToken() {
        try {
            if (!Files.exists(Paths.get(TOKEN_FILE))) {
                Files.createFile(Paths.get(TOKEN_FILE));
            }
            try (BufferedReader reader = new BufferedReader(new FileReader(TOKEN_FILE))) {
                String encryptedToken = reader.readLine();
                if (encryptedToken == null) {
                    // return default value or throw an exception
                    return null;
                }
                return Objects.requireNonNull(EncryptionService.decrypt(encryptedToken, AES_KEY)).split("\n")[0];
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String[] loadData(){
        try {
            if (!Files.exists(Paths.get(TOKEN_FILE))) {
                Files.createFile(Paths.get(TOKEN_FILE));
            }
            try (BufferedReader reader = new BufferedReader(new FileReader(TOKEN_FILE))) {
                String encryptedToken = reader.readLine();
                if (encryptedToken == null) {
                    // return default value or throw an exception
                    return new String[0];
                }
                return Objects.requireNonNull(EncryptionService.decrypt(encryptedToken, AES_KEY)).split("\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void truncateToken() {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(TOKEN_FILE)))) {
            out.print("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}