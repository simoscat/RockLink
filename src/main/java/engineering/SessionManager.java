package engineering;

import model.Musician;
import model.Promoter;

import java.util.HashMap;
import java.util.Map;

public class SessionManager {

    private static SessionManager instance = null;
    private Map<Integer, Session> sessions;
    private int lastId = 0;

    private SessionManager() {
        this.sessions = new HashMap<>();
    }

    public static synchronized SessionManager getInstance() {
        if (instance == null){
            instance = new SessionManager();
        }
        return instance;
    }

    public synchronized Session getNewSession(Musician musician) {
        this.lastId++;

        Session current = new Session(this.lastId, musician);
        this.sessions.put(this.lastId, current);
        return current;
    }

    public synchronized Session getNewSession(Promoter promoter) {
        this.lastId++;

        Session current = new Session(this.lastId, promoter);
        this.sessions.put(this.lastId, current);
        return current;
    }

    public synchronized Session getSession(int id) {
        return this.sessions.get(id);
    }

    public synchronized void deleteSession(int id){
        this.sessions.remove(id);
    }

}
