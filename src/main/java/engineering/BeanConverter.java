package engineering;

import bean.*;
import engineering.enums.*;
import engineering.persistency.JobDecoratorManager;
import model.*;

import java.util.ArrayList;
import java.util.List;

public final class BeanConverter {

    private BeanConverter() {}

    public static JobApplication fromBeanToJobApplication(JobApplicationBean jobApplicationBean) {

        return new JobApplication(
                fromBeanToJobAnnouncement(jobApplicationBean.getJobAnnouncementReference()),
                jobApplicationBean.getArtist(),
                ApplicationStatus.valueOf(jobApplicationBean.getStatus()),
                jobApplicationBean.getRaiseOffer()
        );

    }

    public static PromoterBean fromPromoterToBean(Promoter p){

        return new PromoterBean(
                p.getName(),
                p.getSurname(),
                p.getEmail(),
                p.getGender().name(),
                "", //password is only used in login
                p.promoterContacts()
        );

    }

    public static JobAnnouncementBean fromJobAnnouncementToBean(JobAnnouncement ja){

        List<JobAnnouncementTag> taglist = JobDecoratorManager.getTagsList(ja);

        return new JobAnnouncementBean(
                ja.getTitle(),
                ja.getContent(),
                ja.getAnnouncementDate(),
                ja.getAnnouncementPublishDate(),
                fromPromoterToBean(ja.getPublisher()),
                fromMoneyValueToBean(ja.getJobPay()),
                ja.getEventAddress(),
                ja.whoWasHired(),
                ja.getStatus().name(),
                taglist
        );

    }

    public static MoneyValueBean fromMoneyValueToBean(MoneyValue mv){

        return new MoneyValueBean(
                mv.whichCurrency().name(),
                mv.moneyAmount()
        );

    }

    public static JobApplicationBean fromJobApplicationToBean(JobApplication ja){

        return new JobApplicationBean(
                ja.whoIsCandidate(),
                ja.currentApplicationStatus().name(),
                ja.currentRaiseAmount(),
                fromJobAnnouncementToBean(ja.whichJobAnnouncement())
        );

    }

    public static JobAnnouncement fromBeanToJobAnnouncement(JobAnnouncementBean jobAnnouncement) {

        return new ConcreteJobAnnouncement(
                jobAnnouncement.getTitle(),
                jobAnnouncement.getContent(),
                jobAnnouncement.getDate(),
                JobAnnouncementStatus.valueOf(jobAnnouncement.getJobAnnouncementStatus()),
                jobAnnouncement.getPublishDate(),
                fromBeanToPromoter(jobAnnouncement.getPromoter()),
                fromBeanToMoneyValue(jobAnnouncement.getMoneyValue()),
                jobAnnouncement.getAddress()
        );

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


}
