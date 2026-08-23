package dao.application;

import dao.artist.ArtistDAO;
import dao.factories.DAOFactory;

import engineering.persistency.ConfigManager;
import engineering.enums.ApplicationStatus;
import engineering.persistency.JsonManager;
import model.Artist;
import model.JobAnnouncement;
import model.JobApplication;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class JobApplicationDAOJson extends JobApplicationDAO {

    private final String path;
    private static final String ANNOUNCEMENT_ID_FIELD = "announcementId";
    private static final String CANDIDATE_EMAIL_FIELD = "candidateEmail";

    public JobApplicationDAOJson() {
        path = ConfigManager.getProperty("json.path") + "job_applications.json";
    }

    @Override
    protected JobApplication retrieveJobApplication(String candidateEmail, JobAnnouncement jobAnnouncement) {
        String announcementId = jobAnnouncementDAO.getUniqueId(jobAnnouncement);

        JSONObject obj = JsonManager.findInFile(this.path,
                o -> o.getString("email").equals(candidateEmail) &&
                        o.getString(ANNOUNCEMENT_ID_FIELD).equals(announcementId));

        if (obj == null) {
            return null;
        }

        return parseJson(obj);
    }

    @Override
    public List<JobApplication> retrieveAllJobApplicationsFromEmail(String email) {
        List<JobApplication> result = new ArrayList<>();

        for (JSONObject obj : JsonManager.filterInFile(this.path, o -> o.getString(CANDIDATE_EMAIL_FIELD).equals(email))) {
            result.add(parseJson(obj));
        }

        return result;
    }

    @Override
    protected List<JobApplication> retrieveAllJobApplicationsFromJob(JobAnnouncement jobAnnouncement) {
        String announcementId = jobAnnouncementDAO.getUniqueId(jobAnnouncement);
        List<JobApplication> result = new ArrayList<>();

        for (JSONObject obj : JsonManager.filterInFile(this.path, o -> o.getString(ANNOUNCEMENT_ID_FIELD).equals(announcementId))) {
            result.add(parseJson(obj));
        }

        return result;
    }

    @Override
    protected void saveToPersistency(JobApplication obj) {
        String candidateEmail = obj.whoIsCandidate().getEmail();
        String announcementId = jobAnnouncementDAO.getUniqueId(obj.whichJobAnnouncement());

        JsonManager.upsertFile(this.path,
                json -> json.getString(ANNOUNCEMENT_ID_FIELD).equals(announcementId) &&
                        json.getString(CANDIDATE_EMAIL_FIELD).equals(candidateEmail),
                toJson(obj));
    }

    private JobApplication parseJson(JSONObject obj) {
        String announcementId = obj.getString(ANNOUNCEMENT_ID_FIELD);
        String candidateEmail = obj.getString(CANDIDATE_EMAIL_FIELD);
        ApplicationStatus status = ApplicationStatus.valueOf(obj.getString("status"));
        BigDecimal raiseOffer = obj.getBigDecimal("raiseOffer");

        ArtistDAO artistDAO = DAOFactory.getInstance().getArtistDAO();
        Artist a = artistDAO.getArtistByEmail(candidateEmail);


        return new JobApplication(jobAnnouncementDAO.getAnnouncementFromId(announcementId),
                a, status, raiseOffer);
    }

    private JSONObject toJson(JobApplication jobApp) {
        JSONObject obj = new JSONObject();
        obj.put(ANNOUNCEMENT_ID_FIELD, jobAnnouncementDAO.getUniqueId(jobApp.whichJobAnnouncement()));
        obj.put(CANDIDATE_EMAIL_FIELD, jobApp.whoIsCandidate().getEmail());
        obj.put("status", jobApp.currentApplicationStatus().name());
        obj.put("raiseOffer", jobApp.currentRaiseAmount() != null ? jobApp.currentRaiseAmount() : BigDecimal.ZERO);
        return obj;
    }


}
