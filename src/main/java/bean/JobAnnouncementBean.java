package bean;

import engineering.enums.JobAnnouncementTag;

import java.time.LocalDateTime;
import java.util.List;

public class JobAnnouncementBean extends BaseAnnouncementBean {

    private PromoterBean promoter;
    private MoneyValueBean moneyValue;
    private String address;
    private ArtistBean hiredArtist;
    private String jobAnnouncementStatus;
    private List<JobAnnouncementTag> tags;
    private String baseTitle;
    private String baseContent;
    private String id;

    public JobAnnouncementBean(String title, String content, LocalDateTime date,
                               PromoterBean promoter, MoneyValueBean moneyValue, String address,
                               List<JobAnnouncementTag> tags) {
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

    public ArtistBean getHiredArtist() {
        return hiredArtist;
    }

    public void setHiredArtist(ArtistBean hiredArtist) {
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

    public void setBaseTitle(String baseTitle) {
        this.baseTitle = baseTitle;
    }

    public String getBaseTitle() {
        return baseTitle != null ? baseTitle : getTitle();
    }

    public void setBaseContent(String baseContent) {
        this.baseContent = baseContent;
    }

    public String getBaseContent() {
        return baseContent != null ? baseContent : getContent();
    }

    public String getId(){
        return this.id;
    }

    public void setId(String id){
        this.id = id;
    }

}
