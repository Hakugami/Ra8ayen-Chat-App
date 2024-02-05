package token;

import utils.EncryptionService;

import java.io.*;
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
        try (BufferedReader reader = new BufferedReader(new FileReader(TOKEN_FILE))) {
            String encryptedToken = reader.readLine();
            return Objects.requireNonNull(EncryptionService.decrypt(encryptedToken, AES_KEY)).split("\n")[0];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String[] loadData(){
        try (BufferedReader reader = new BufferedReader(new FileReader(TOKEN_FILE))) {
            String encryptedToken = reader.readLine();
            return Objects.requireNonNull(EncryptionService.decrypt(encryptedToken, AES_KEY)).split("\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}