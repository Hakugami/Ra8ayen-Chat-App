package token;

import java.io.*;

public class TokenManager {
    private static TokenManager instance;
    private TokenManager() {
    }
    public static TokenManager getInstance() {
        if (instance == null) {
            instance = new TokenManager();
        }
        return instance;
    }
    private static final String TOKEN_FILE = System.getProperty("user.dir") + File.separator + "token.txt";

    public  void setToken(String token) {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(TOKEN_FILE)))) {
            out.println(token);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public  String getToken() {
        try (BufferedReader reader = new BufferedReader(new FileReader(TOKEN_FILE))) {
            return reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}