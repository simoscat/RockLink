package engineering;

import bean.*;
import engineering.enums.*;
import engineering.persistency.JobDecoratorManager;
import model.*;

import java.util.ArrayList;
import java.util.List;

public final class BeanConverter {

    private BeanConverter() {}

    public static PromoterBean fromPromoterToBean(Promoter p){

        return new PromoterBean(
                p.getName(),
                p.getSurname(),
                p.getEmail(),
                p.getGender().name(),
                "", //password is only used in login
                p.howToContact()
        );

    }

    public static JobAnnouncementBean fromJobAnnouncementToBean(JobAnnouncement ja){

        List<JobAnnouncementTag> taglist = JobDecoratorManager.getTagsList(ja);

        JobAnnouncementBean bean = new JobAnnouncementBean(
                ja.getTitle(), //original title without tags
                ja.getContent(),
                ja.getAnnouncementDate(),
                fromPromoterToBean(ja.getPublisher()),
                fromMoneyValueToBean(ja.getJobPay()),
                ja.getEventAddress(),
                taglist
        );

        bean.setPublishDate(ja.getAnnouncementPublishDate());
        bean.setHiredArtist(fromArtistToBean(ja.whoWasHired()));
        bean.setJobAnnouncementStatus(ja.getStatus().name());

        return bean;

    }

    public static MoneyValueBean fromMoneyValueToBean(MoneyValue mv){

        return new MoneyValueBean(
                mv.whichCurrency().name(),
                mv.moneyAmount()
        );

    }

    public static JobApplicationBean fromJobApplicationToBean(JobApplication ja){

        return new JobApplicationBean(
                fromArtistToBean(ja.whoIsCandidate()),
                ja.currentApplicationStatus().name(),
                ja.currentRaiseAmount(),
                fromJobAnnouncementToBean(ja.whichJobAnnouncement())
        );

    }

    private static ArtistBean fromArtistToBean(Artist artist) {
        if (artist != null){
            return new ArtistBean(
                    artist.getArtistName(),
                    artist.getType(),
                    artist.getEmail(),
                    artist.getArtistDetails()
            );
        }
        return null;
    }

    public static JobAnnouncement fromBeanToJobAnnouncement(JobAnnouncementBean jobAnnouncement) {

        JobAnnouncement job = new ConcreteJobAnnouncement(
                jobAnnouncement.getTitle(),
                jobAnnouncement.getContent(),
                jobAnnouncement.getDate(),
                jobAnnouncement.getPublishDate(),
                fromBeanToPromoter(jobAnnouncement.getPromoter()),
                fromBeanToMoneyValue(jobAnnouncement.getMoneyValue()),
                jobAnnouncement.getAddress()
        );

        job.setStatus(JobAnnouncementStatus.valueOf(jobAnnouncement.getJobAnnouncementStatus()));

        List<JobAnnouncementTag> tagList = jobAnnouncement.getTags();

        return JobDecoratorManager.applyDecorators(job, tagList);

    }

    public static MoneyValue fromBeanToMoneyValue(MoneyValueBean moneyValue) {

        return new MoneyValue(
                moneyValue.getValue(),
                CurrencyType.valueOf(moneyValue.getCurrency())
        );

    }

    public static Promoter fromBeanToPromoter(PromoterBean promoter) {

        return new Promoter(
                promoter.getName(),
                promoter.getSurname(),
                promoter.getEmail(),
                Gender.valueOf(promoter.getGender()),
                promoter.getContacts()
        );

    }

    public static Musician fromBeanToMusician(MusicianBean musician) {

        List<Instrument> instruments = new ArrayList<>();

        for (InstrumentBean bean : musician.getInstruments()) {
            instruments.add(fromBeanToInstrument(bean));
        }

        return new Musician(
                musician.getName(),
                musician.getSurname(),
                musician.getStageName(),
                musician.getEmail(),
                Gender.valueOf(musician.getGender()),
                instruments
        );

    }

    public static Instrument fromBeanToInstrument(InstrumentBean instrument) {

        return new Instrument(
                instrument.getName(),
                Mastery.valueOf(instrument.getMastery())
        );

    }

    public static List<JobAnnouncementBean> fromJobAnnouncementsToBeans(List<JobAnnouncement> jobAnnouncements) {

        List<JobAnnouncementBean> beans = new ArrayList<>();

        for (JobAnnouncement jobAnnouncement : jobAnnouncements) {

            JobAnnouncementBean jAB = fromJobAnnouncementToBean(jobAnnouncement);

            beans.add(jAB);

        }

        return beans;

    }


    public static NotificationBean fromNotificationToBean(Notification notification) {

        return new NotificationBean(
                notification.getSender().getEmail(),
                notification.getReceiver().getEmail(),
                notification.getEvent(),
                notification.getTimeStamp(),
                fromJobAnnouncementToBean(notification.getJobAnnouncement())
        );

    }

}
