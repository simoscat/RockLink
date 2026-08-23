package dao.application;

import dao.artist.ArtistDAO;
import dao.factories.DAOFactory;

import engineering.persistency.ConfigManager;
import engineering.enums.ApplicationStatus;
import engineering.persistency.JsonManager;
import exception.DAOException;
import model.Artist;
import model.JobAnnouncement;
import model.JobApplication;

import org.json.JSONArray;
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

        JSONArray applications = JsonManager.readJsonFile(this.path);

        for (int i = 0; i < applications.length(); i++) {

            JSONObject obj = applications.getJSONObject(i);
            if (obj.getString("email").equals(candidateEmail) &&
            obj.getString(ANNOUNCEMENT_ID_FIELD).equals(jobAnnouncementDAO.getUniqueId(jobAnnouncement))) {
                return parseJson(obj);
            }

        }
        return null;
    }

    @Override
    public List<JobApplication> retrieveAllJobApplicationsFromEmail(String email) {
        JSONArray applications = JsonManager.readJsonFile(this.path);
        List<JobApplication> result = new ArrayList<>();

        for (int i = 0; i < applications.length(); i++) {
            JSONObject obj = applications.getJSONObject(i);
            if (obj.getString(CANDIDATE_EMAIL_FIELD).equals(email)) {
                result.add(parseJson(obj));
            }
        }

        return result;
    }

    @Override
    protected List<JobApplication> retrieveAllJobApplicationsFromJob(JobAnnouncement jobAnnouncement) {

        String announcementId = jobAnnouncementDAO.getUniqueId(jobAnnouncement);

        JSONArray applications = JsonManager.readJsonFile(this.path);
        List<JobApplication> result = new ArrayList<>();

        for (int i = 0; i < applications.length(); i++) {

            JSONObject json = applications.getJSONObject(i);

            if (json.getString(ANNOUNCEMENT_ID_FIELD).equals(announcementId)) {
                result.add(parseJson(json));
            }
        }

        return result;

    }

    @Override
    protected void saveToPersistency(JobApplication obj) {
        JSONArray applications = JsonManager.readJsonFile(this.path);

        String candidateEmail = obj.whoIsCandidate().getEmail();
        String announcementId = jobAnnouncementDAO.getUniqueId(obj.whichJobAnnouncement());

        boolean found = false;

        for (int i = 0; i < applications.length(); i++) {
            JSONObject json = applications.getJSONObject(i);
            if (json.getString(ANNOUNCEMENT_ID_FIELD).equals(announcementId) &&
            json.getString(CANDIDATE_EMAIL_FIELD).equals(candidateEmail)) {
                applications.put(i, toJson(obj));
                found = true;
                break;
            }
        }

        if (!found) {
            applications.put(toJson(obj));
        }

        JsonManager.writeJsonFile(applications, this.path);
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
