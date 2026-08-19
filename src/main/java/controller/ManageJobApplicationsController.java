package controller;

import bean.*;
import dao.announcement.JobAnnouncementDAO;
import dao.application.JobApplicationDAO;
import dao.factories.DAOFactory;
import dao.musician.MusicianDAO;
import dao.promoter.PromoterDAO;
import engineering.enums.*;
import exception.ControllerLogicException;
import exception.DAOException;
import model.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class ManageJobApplicationsController {

    private final MusicianDAO musicianDAO = DAOFactory.getInstance().getMusicianDAO();
    private final PromoterDAO promoterDAO = DAOFactory.getInstance().getPromoterDAO();
    private final JobAnnouncementDAO jobAnnouncementDAO = DAOFactory.getInstance().getJobAnnouncementDAO();
    private final JobApplicationDAO jobApplicationDAO = DAOFactory.getInstance().getJobApplicationDAO();

    public List<JobAnnouncementBean> findOpenJobAnnouncements() {

        try {

            return fromJobAnnouncementsToBeans(jobAnnouncementDAO.getAllOpenJobAnnouncements());


        }
        catch (DAOException e) {

            throw new ControllerLogicException("No open announcements found");

        }

    }

    public List<JobAnnouncementBean> findAllJobAnnouncements() {

        try{

            return fromJobAnnouncementsToBeans(jobAnnouncementDAO.getAllJobAnnouncements());

        }
        catch (DAOException e) {
            throw new ControllerLogicException("No announcements found");
        }
    }

    private List<JobAnnouncementBean> fromJobAnnouncementsToBeans(List<JobAnnouncement> jobAnnouncements) {

        List<JobAnnouncementBean> beans = new ArrayList<>();

        for (JobAnnouncement jobAnnouncement : jobAnnouncements) {

            JobAnnouncementBean jAB = fromJobAnnouncementToBean(jobAnnouncement);

            beans.add(jAB);

        }

        return beans;

    }

    public List<JobApplicationBean> findMusicianJobApplications(MusicianBean m){

        try {

            List<JobApplication> jobApplications = jobApplicationDAO.getAllMusicianJobApplicationsFromEmail(m.getEmail());
            List<JobApplicationBean> beans = new ArrayList<>();

            for (JobApplication jobApplication : jobApplications) {

                beans.add(fromJobApplicationToBean(jobApplication));

            }
            return beans;

        }
        catch (DAOException e) {
            throw new ControllerLogicException("No job applications found for this musician");
        }
    }

    public List<JobAnnouncementBean> findPromoterPublishedJobAnnouncements(PromoterBean p){

        try{

            List<JobAnnouncement> jobs = jobAnnouncementDAO.getAllPromoterAnnouncementsFromEmail(p.getEmail());

            List<JobAnnouncementBean> beans = new ArrayList<>();

            for (JobAnnouncement job : jobs){

                beans.add(fromJobAnnouncementToBean(job));

            }

            return beans;

        }
        catch (DAOException e) {

            throw new ControllerLogicException("No job announcements found for this promoter");

        }

    }

    public List<JobApplicationBean> findJobAnnouncementApplications(JobAnnouncementBean jobAnnouncement){

        JobAnnouncement job = fromBeanToJobAnnouncement(jobAnnouncement);

        List<JobApplication> applications = jobApplicationDAO.getAllJobAnnouncementApplications(job);

        List<JobApplicationBean> beans = new ArrayList<>();

        for (JobApplication jobApplication : applications){

            beans.add(fromJobApplicationToBean(jobApplication));
        }

        return beans;
    }

    public void acceptApplication(JobApplicationBean jobApplicationBean){

        try {
            JobApplication jobApplication = fromBeanToJobApplication(jobApplicationBean);

            JobAnnouncement jobAnnouncement = jobApplication.whichJobAnnouncement();

            jobApplication.acceptApplication();
            jobAnnouncement.hireArtist(jobApplication.whoIsCandidate());

            jobApplicationDAO.save(jobApplication);
            jobAnnouncementDAO.save(jobAnnouncement);
        } catch (DAOException _) {
            throw new ControllerLogicException("Could not accept job application");
        }

    }

    public void rejectApplication(JobApplicationBean jobApplicationBean){

        try {
            JobApplication jobApplication = fromBeanToJobApplication(jobApplicationBean);

            jobApplication.rejectApplication();
            jobApplicationDAO.save(jobApplication);
        } catch (DAOException _) {
            throw new ControllerLogicException("Could not reject job application");
        }

    }

    public void closeJobAnnouncement(JobAnnouncementBean jobAnnouncementBean){

        try {
            JobAnnouncement job = fromBeanToJobAnnouncement(jobAnnouncementBean);

            job.closeAnnouncement();
            jobAnnouncementDAO.save(job);

            List<JobApplication> applicationsToReject = jobApplicationDAO.getAllJobAnnouncementApplications(job);

            for (JobApplication jobApplication : applicationsToReject) {

                jobApplication.rejectApplication();
                jobApplicationDAO.save(jobApplication);

            }
        }
        catch (DAOException _) {
            throw new ControllerLogicException("Could not close job announcement");
        }

    }



    public void applyForJobAnnouncement(JobAnnouncementBean jobAnnouncementBean, MusicianBean applicant){

        try {

            Musician m = fromBeanToMusician(applicant);

            JobAnnouncement jobAnnouncement = fromBeanToJobAnnouncement(jobAnnouncementBean);

            JobApplication application = new JobApplication(
                    jobAnnouncement,
                    m
            );

            jobApplicationDAO.save(application);


        } catch (DAOException _) {
            throw new ControllerLogicException("Could not apply for job announcement");
        }

    }


    // ------------------------------------------
    // bean conversion utilities
    //-------------------------------------------

    private JobApplication fromBeanToJobApplication(JobApplicationBean jobApplicationBean) {

        return new JobApplication(
                fromBeanToJobAnnouncement(jobApplicationBean.getJobAnnouncementReference()),
                jobApplicationBean.getJobAnnouncementReference().getHiredArtist(),
                ApplicationStatus.valueOf(jobApplicationBean.getStatus()),
                jobApplicationBean.getRaiseOffer()
        );

    }

    private PromoterBean fromPromoterToBean(Promoter p){

        return new PromoterBean(
                p.getName(),
                p.getSurname(),
                p.getEmail(),
                p.getGender().name(),
                "", //password is only used in login
                p.promoterContacts()
        );

    }

    private JobAnnouncementBean fromJobAnnouncementToBean(JobAnnouncement ja){

        return new JobAnnouncementBean(
                fromPromoterToBean(ja.getPublisher()),
                fromMoneyValueToBean(ja.getJobPay()),
                ja.getEventAddress(),
                ja.whoWasHired(),
                ja.getStatus().name()
        );

    }

    private MoneyValueBean fromMoneyValueToBean(MoneyValue mv){

        return new MoneyValueBean(
                mv.whichCurrency().name(),
                mv.moneyAmount()
        );

    }

    private JobApplicationBean fromJobApplicationToBean(JobApplication ja){

        return new JobApplicationBean(
                ja.currentRaiseAmount(),
                fromJobAnnouncementToBean(ja.whichJobAnnouncement())
        );

    }

    private JobAnnouncement fromBeanToJobAnnouncement(JobAnnouncementBean jobAnnouncement) {

        return new ConcreteJobAnnouncement(
                jobAnnouncement.getTitle(),
                jobAnnouncement.getContent(),
                LocalDateTime.parse(jobAnnouncement.getDate()),
                JobAnnouncementStatus.valueOf(jobAnnouncement.getJobAnnouncementStatus()),
                LocalDateTime.parse(jobAnnouncement.getPublishDate()),
                fromBeanToPromoter(jobAnnouncement.getPromoter()),
                fromBeanToMoneyValue(jobAnnouncement.getMoneyValue()),
                jobAnnouncement.getAddress()
        );

    }

    private MoneyValue fromBeanToMoneyValue(MoneyValueBean moneyValue) {

        return new MoneyValue(
                moneyValue.getValue(),
                CurrencyType.valueOf(moneyValue.getCurrency())
        );

    }

    private Promoter fromBeanToPromoter(PromoterBean promoter) {

        return new Promoter(
                promoter.getName(),
                promoter.getSurname(),
                promoter.getEmail(),
                Gender.valueOf(promoter.getGender()),
                promoter.getContacts()
        );

    }

    private Musician fromBeanToMusician(MusicianBean musician) {

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

    private Instrument fromBeanToInstrument(InstrumentBean instrument) {

        return new Instrument(
                instrument.getName(),
                Mastery.valueOf(instrument.getMastery())
        );

    }


}