package view;

import bean.*;

import java.util.List;

public class Context {

    // user and session
    private SessionBean session;
    private MusicianBean musician;
    private PromoterBean promoter;

    // announcement context
    private JobAnnouncementBean currentJobAnnouncement;
    private List<JobAnnouncementBean> jobAnnouncements;
    private JobApplicationBean currentJobApplication;
    private List<JobApplicationBean> jobApplications;

    public Context(){}

    public Context(MusicianBean musician){
        this.musician = musician;
    }

    public Context(PromoterBean promoter){
        this.promoter = promoter;
    }

    public SessionBean getSession() {
        return session;
    }

    public void setSession(SessionBean session) {
        this.session = session;
    }

    public MusicianBean getMusician() {
        return musician;
    }

    public void setMusician(MusicianBean musician) {
        this.musician = musician;
    }

    public PromoterBean getPromoter() {
        return promoter;
    }

    public void setPromoter(PromoterBean promoter) {
        this.promoter = promoter;
    }

    public void setCurrentJobAnnouncement(JobAnnouncementBean currentJobAnnouncement) {
        this.currentJobAnnouncement = currentJobAnnouncement;
    }

    public JobAnnouncementBean getCurrentJobAnnouncement() {
        return currentJobAnnouncement;
    }

    public List<JobAnnouncementBean> getJobAnnouncements(){
        return jobAnnouncements;
    }

    public void setJobAnnouncements(List<JobAnnouncementBean> jobAnnouncements){
        this.jobAnnouncements = jobAnnouncements;
    }

    public void addJobAnnouncement(JobAnnouncementBean jobAnnouncement){
        this.jobAnnouncements.add(jobAnnouncement);
    }

    public void setCurrentJobApplication(JobApplicationBean currentJobApplication) {
        this.currentJobApplication = currentJobApplication;
    }

    public JobApplicationBean getCurrentJobApplication() {
        return currentJobApplication;
    }

    public List<JobApplicationBean> getJobApplications(){
        return jobApplications;
    }

    public void setJobApplications(List<JobApplicationBean> jobApplications){
        this.jobApplications = jobApplications;
    }

    public void addJobApplication(JobApplicationBean jobApplication){
        this.jobApplications.add(jobApplication);
    }


}
