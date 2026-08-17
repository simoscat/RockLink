package engineering.persistency;

import java.util.HashMap;
import java.util.Map;

public abstract class DAOWithCache<T> {

    private Map<String, T> cache;
    // the String will be a unique key that identifies the cached object (e.g., email for User, id for announcement)

    protected DAOWithCache(){
        this.cache = new HashMap<>();
    }

    public boolean isCached(String id){
        return this.cache.containsKey(id);
    }

    // Overload
    public boolean isCached(T obj){
        return this.cache.containsValue(obj);
    }

    public abstract String getKey(T obj);

    public T getFromCache(String id){
        return this.cache.get(id);
    }

    public void addToCache(T obj){
        if (obj != null && !this.isCached(obj)){
            this.cache.put(this.getKey(obj), obj);
        }

    }

    public void deleteFromCache(T obj){
        if (this.isCached(obj)){
            String key = this.getKey(obj);
            this.cache.remove(key);
        }
    }

    public void clearCache(){
        this.cache.clear();
    }

    public void save(T obj){
        saveToPersistency(obj);
        addToCache(obj);
    }

    protected abstract void saveToPersistency(T obj);

}
