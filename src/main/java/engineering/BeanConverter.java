package engineering;

import bean.*;
import dao.factories.DAOFactory;
import engineering.persistency.JobDecoratorManager;
import model.*;

import java.util.ArrayList;
import java.util.List;

public final class BeanConverter {

    private BeanConverter() {}

    public static PromoterBean fromPromoterToBean(Promoter p){

        if (p != null) return new PromoterBean(
                p.getName(),
                p.getSurname(),
                p.getEmail(),
                p.getGender().name(),
                "", //password is only used in login
                p.howToContact()
        );
        return null;

    }

    public static JobAnnouncementBean fromJobAnnouncementToBean(JobAnnouncement ja){

        if (ja == null) return null;

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

        JobAnnouncement baseJa = JobDecoratorManager.unwrapJobAnnouncement(ja);
        bean.setBaseTitle(baseJa.getTitle());
        bean.setBaseContent(baseJa.getContent());

        bean.setId(DAOFactory.getInstance().getJobAnnouncementDAO().getUniqueId(ja));

        return bean;

    }

    public static MoneyValueBean fromMoneyValueToBean(MoneyValue mv){

        if (mv != null) return new MoneyValueBean(
                mv.whichCurrency().name(),
                mv.moneyAmount()
        );
        return null;

    }

    public static JobApplicationBean fromJobApplicationToBean(JobApplication ja){

        if (ja != null) return new JobApplicationBean(
                fromArtistToBean(ja.whoIsCandidate()),
                ja.currentApplicationStatus().name(),
                ja.currentRaiseAmount(),
                fromJobAnnouncementToBean(ja.whichJobAnnouncement())
        );
        return null;

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

    public static Musician fromBeanToMusician(MusicianBean musician) {

        if (musician == null) return null;

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

        if (instrument != null) return new Instrument(
                instrument.getName(),
                Mastery.valueOf(instrument.getMastery())
        );
        return null;

    }

    public static List<JobAnnouncementBean> fromJobAnnouncementsToBeans(List<JobAnnouncement> jobAnnouncements) {

        if (jobAnnouncements == null) return new ArrayList<>();

        List<JobAnnouncementBean> beans = new ArrayList<>();

        for (JobAnnouncement jobAnnouncement : jobAnnouncements) {

            JobAnnouncementBean jAB = fromJobAnnouncementToBean(jobAnnouncement);

            beans.add(jAB);

        }

        return beans;

    }


    public static NotificationBean fromNotificationToBean(Notification notification) {

        if (notification != null) return new NotificationBean(
                notification.getSender().getName() + " " + notification.getSender().getSurname(),
                notification.getReceiver().getName() + " " + notification.getReceiver().getSurname(),
                notification.getEvent(),
                notification.getTimeStamp(),
                fromJobAnnouncementToBean(notification.getJobAnnouncement())
        );

        return null;

    }

}
