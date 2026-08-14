package dao;

import java.util.HashMap;
import java.util.Map;

public abstract class CacheDAO<Obj> {

    private Map<String, Obj> cache;
    // the String will be a unique key that identifies the cached object (e.g., email for User, id for announcement)

    protected CacheDAO(){
        this.cache = new HashMap<>();
    }

    public boolean isCached(String id){
        return this.cache.containsKey(id);
    }

    // Overload
    public boolean isCached(Obj obj){
        return this.cache.containsValue(obj);
    }

    public abstract String getKey(Obj o);

    public Obj getFromCache(String id){
        return this.cache.get(id);
    }

    public void addToCache(Obj o){
        if (o != null && !this.isCached(o)){
            this.cache.put(this.getKey(o), o);
        }

    }

    public void deleteFromCache(Obj o){
        if (this.isCached(o)){
            String key = this.getKey(o);
            this.cache.remove(key);
        }
    }

    public void clearCache(){
        this.cache.clear();
    }

    public abstract void flush();
}
