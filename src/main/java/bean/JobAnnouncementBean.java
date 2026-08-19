package bean;

import model.Artist;
import model.MoneyValue;

import java.util.List;

public class JobAnnouncementBean extends BaseAnnouncementBean {

    private PromoterBean promoter;
    private MoneyValueBean moneyValue;
    private String address;
    private Artist hiredArtist;
    private String jobAnnouncementStatus;
    private List<String> tags;

    public JobAnnouncementBean() {
        super();
    }

    public JobAnnouncementBean(PromoterBean promoter, MoneyValueBean moneyValue, String address,
                               Artist hiredArtist, String jobAnnouncementStatus) {
        super();
        this.promoter = promoter;
        this.moneyValue = moneyValue;
        this.address = address;
        this.hiredArtist = hiredArtist;
        this.jobAnnouncementStatus = jobAnnouncementStatus;
    }

    public PromoterBean getPromoter() {
        return promoter;
    }

    public void setPromoter(PromoterBean promoter) {
        this.promoter = promoter;
    }

    public MoneyValueBean getMoneyValue() {
        return moneyValue;
    }

    public void setMoneyValue(MoneyValueBean moneyValue) {
        this.moneyValue = moneyValue;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Artist getHiredArtist() {
        return hiredArtist;
    }

    public void setHiredArtist(Artist hiredArtist) {
        this.hiredArtist = hiredArtist;
    }

    public String getJobAnnouncementStatus() {
        return jobAnnouncementStatus;
    }

    public void setJobAnnouncementStatus(String jobAnnouncementStatus) {
        this.jobAnnouncementStatus = jobAnnouncementStatus;
    }

    public void setTags(List<String> tags){
        this.tags = tags;
    }

    public List<String> getTags() {
        return tags;
    }

}
