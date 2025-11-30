package kg.attractor.java.lesson44.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SessionManager {
    private static SessionManager instance;
    private final Map<String, Integer> sessions;
    private final Map<Integer, String> employeeSessions;


    private SessionManager() {
        this.sessions = new HashMap<>();
        this.employeeSessions = new HashMap<>();
    }

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }

        return instance;
    }

    public String createSession(int employeeId) {

        String sessionId = employeeId + "_" + new Random().nextInt(10000) + "_" + System.currentTimeMillis();

        sessions.put(sessionId, employeeId);
        return sessionId;
    }

    public Integer getEmployeeId(String sessionId) {
        return sessions.get(sessionId);
    }

    public void removeSession(String sessionId) {
        sessions.remove(sessionId);
    }

    public void removeSessionByEmployeeId(int employeeId) {
        sessions.entrySet().removeIf(entry -> entry.getValue() == employeeId);
    }
}
