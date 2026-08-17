package dao.promoter;

import engineering.persistency.DAOWithCache;
import model.Promoter;

public abstract class PromoterDAO extends DAOWithCache<Promoter> {

    @Override
    public String getKey(Promoter obj) {
        return obj.getEmail();
    }

    public Promoter getPromoterByEmail(String email){
        if (isCached(email)){
            return getFromCache(email);
        } else {
            Promoter promoter = retrievePromoterByEmail(email);
            addToCache(promoter);
            return promoter;
        }
    }

    protected abstract Promoter retrievePromoterByEmail(String email);

    protected abstract void saveToPersistency(Promoter promoter);
}
