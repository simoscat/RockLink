package bean;

import model.MoneyValue;

public class JobAnnouncementBean extends BaseAnnouncementBean {

    private PromoterBean promoter;
    private MoneyValueBean salary;
    private String address;

    public JobAnnouncementBean(String id, String title, String content, String date, String announcementStatus, String hiredArtist,
                               PromoterBean promoter, MoneyValueBean salary, String address) {
        super(id, title, content, date, announcementStatus, hiredArtist);
        this.promoter = promoter;
        this.salary = salary;
        this.address = address;
    }

    public PromoterBean getPromoter() {
        return promoter;
    }

    public MoneyValueBean getSalary() {
        return salary;
    }

    public void setPromoter(PromoterBean promoter) {
        this.promoter = promoter;
    }

    public void setSalary(MoneyValueBean salary) {
        this.salary = salary;
    }

}
