package dao.application;

import dao.factories.DAOFactory;
import dao.musician.MusicianDAO;
import engineering.enums.ApplicationStatus;
import engineering.persistency.JsonManager;
import exception.DAOException;
import model.JobAnnouncement;
import model.JobApplication;
import model.Musician;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class JobApplicationDAOJson extends JobApplicationDAO {

    private final String path;
    private static final String ANNOUNCEMENT_ID_FIELD = "announcementId";
    private static final String CANDIDATE_EMAIL_FIELD = "candidateEmail";

    public JobApplicationDAOJson() {
        try(InputStream is = new FileInputStream("config.properties")){
            Properties prop = new Properties();
            prop.load(is);
            path = prop.getProperty("json.path") + "job_applications.json";
        } catch (FileNotFoundException e) {
            throw new DAOException("Couldn't find properties file", e);
        } catch (IOException e) {
            throw new DAOException("Couldn't read properties file", e);
        }
    }

    @Override
    protected JobApplication retrieveJobApplication(String candidateEmail, JobAnnouncement jobAnnouncement) {

        JSONArray applications = readJsonFile();

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
        JSONArray applications = readJsonFile();
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

        JSONArray applications = readJsonFile();
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
        JSONArray applications = readJsonFile();

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

        writeJsonFile(applications);
    }

    private JobApplication parseJson(JSONObject obj) {
        String announcementId = obj.getString(ANNOUNCEMENT_ID_FIELD);
        String candidateEmail = obj.getString(CANDIDATE_EMAIL_FIELD);
        ApplicationStatus status = ApplicationStatus.valueOf(obj.getString("status"));
        BigDecimal raiseOffer = obj.getBigDecimal("raiseOffer");

        //right now, musician is the only artist, so this is just a temporary fix TODO
        MusicianDAO musicianDAO = DAOFactory.getInstance().getMusicianDAO();
        Musician m = musicianDAO.getMusicianByEmail(candidateEmail);

        // in the future the appropriate thing to do would be to add a secondary ArtistDAO that
        // checks both the databases to find which artist has the email (assuming that a band has to have a
        // different email address than any musician)

        return new JobApplication(jobAnnouncementDAO.getAnnouncementFromId(announcementId),
                m, status, raiseOffer);
    }

    private JSONObject toJson(JobApplication jobApp) {
        JSONObject obj = new JSONObject();
        obj.put(ANNOUNCEMENT_ID_FIELD, jobAnnouncementDAO.getUniqueId(jobApp.whichJobAnnouncement()));
        obj.put(CANDIDATE_EMAIL_FIELD, jobApp.whoIsCandidate().getEmail());
        obj.put("status", jobApp.currentApplicationStatus().name());
        obj.put("raiseOffer", jobApp.currentRaiseAmount() != null ? jobApp.currentRaiseAmount() : BigDecimal.ZERO);
        return obj;
    }

    private JSONArray readJsonFile() {
        try {
            return JsonManager.readJsonFile(this.path);
        } catch (IOException e) {
            throw new DAOException("Couldn't read Json file "+this.path, e);
        }
    }

    private void writeJsonFile(JSONArray array) {
        try{
            JsonManager.writeJsonFile(array, this.path);
        } catch(IOException e) {
            throw new DAOException("Couldn't write Json file "+this.path, e);
        }
    }


}
