package session.manager;

import session.Session;

import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {
    private static SessionManager sessionManager;
    private ConcurrentHashMap<String, Session> sessions;

    private SessionManager() {
        sessions = new ConcurrentHashMap<>();
    }

    public static SessionManager getInstance() {
        if (sessionManager == null) {
            sessionManager = new SessionManager();
        }
        return sessionManager;
    }

    public void addSession(Session session) {
        sessions.put(session.getToken(), session);
    }

    public Session getSession(String token) {
        return sessions.get(token);
    }

    public void removeSession(String token) {
        sessions.remove(token);
    }

    @Override
    public String toString() {
        return "SessionManager{" +
                "sessions=" + sessions +
                '}';
    }
}