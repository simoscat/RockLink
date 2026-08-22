package bean;

import engineering.enums.JobAnnouncementTag;
import model.Artist;

import java.time.LocalDateTime;
import java.util.List;

public class JobAnnouncementBean extends BaseAnnouncementBean {

    private PromoterBean promoter;
    private MoneyValueBean moneyValue;
    private String address;
    private Artist hiredArtist;
    private String jobAnnouncementStatus;
    private List<JobAnnouncementTag> tags;

    public JobAnnouncementBean(String title, String content, LocalDateTime date, LocalDateTime publishDate,
                               PromoterBean promoter, MoneyValueBean moneyValue, String address,
                               Artist hiredArtist, String jobAnnouncementStatus, List<JobAnnouncementTag> tags) {
        super(title, content, date, publishDate);

        this.promoter = promoter;
        this.moneyValue = moneyValue;
        this.address = address;
        this.hiredArtist = hiredArtist;
        this.jobAnnouncementStatus = jobAnnouncementStatus;
        this.tags = tags;
    }

    public JobAnnouncementBean(String title, String content, LocalDateTime date,
                               PromoterBean promoter, MoneyValueBean moneyValue, String address,
                               List<JobAnnouncementTag> tags) { // for brand new announcement
        super(title, content, date);
        this.promoter = promoter;
        this.moneyValue = moneyValue;
        this.address = address;
        this.tags = tags;
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

    public void setTags(List<JobAnnouncementTag> tags){
        this.tags = tags;
    }

    public List<JobAnnouncementTag> getTags() {
        return tags;
    }

}
