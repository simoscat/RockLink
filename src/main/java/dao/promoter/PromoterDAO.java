package dao.promoter;

import dao.DAOWithCache;
import model.Promoter;

public abstract class PromoterDAO extends DAOWithCache<Promoter> {

    @Override
    public String getKey(Promoter obj) {
        return obj.getEmail();
    }

    public abstract Promoter getPromoterByEmail(String email);
    public abstract void flush(Promoter promoter);
}
