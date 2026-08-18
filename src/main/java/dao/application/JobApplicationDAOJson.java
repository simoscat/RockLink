package dao.application;

import dao.factories.DAOFactory;
import dao.musician.MusicianDAO;
import engineering.enums.ApplicationStatus;
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

//TODO CONTROLLA!!!!

public class JobApplicationDAOJson extends JobApplicationDAO {

    private final String PATH;

    public JobApplicationDAOJson() {
        try(InputStream is = new FileInputStream("config.properties")){
            Properties prop = new Properties();
            prop.load(is);
            PATH = prop.getProperty("json.path") + "job_applications.json";
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
            obj.getString("announcementId").equals(jobAnnouncementDAO.getUniqueId(jobAnnouncement))) {
                return parseJson(obj);
            }

        }
        throw new DAOException("Job application not found for email " + candidateEmail + " in announcement "
        + jobAnnouncement.getTitle());
    }

    @Override
    public List<JobApplication> retrieveAllJobApplicationsFromEmail(String email) {
        JSONArray applications = readJsonFile();
        List<JobApplication> result = new ArrayList<>();

        for (int i = 0; i < applications.length(); i++) {
            JSONObject obj = applications.getJSONObject(i);
            if (obj.getString("candidateEmail").equals(email)) {
                result.add(parseJson(obj));
            }
        }

        if (result.isEmpty()) {
            throw new DAOException("No applications found for email: " + email);
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

            if (json.getString("announcementId").equals(announcementId)) {
                result.add(parseJson(json));
            }
        }

        if (result.isEmpty()) {
            throw new DAOException("No applications found for job announcement " + jobAnnouncement.getTitle());
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
            if (json.getString("announcementId").equals(announcementId) &&
            json.getString("candidateEmail").equals(candidateEmail)) {
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
        String announcementId = obj.getString("announcementId");
        String candidateEmail = obj.getString("candidateEmail");
        ApplicationStatus status = ApplicationStatus.valueOf(obj.getString("status"));
        BigDecimal raiseOffer = obj.getBigDecimal("raiseOffer");

        //TODO!!!!
        //right now, musician is the only artist, so this is just a temporary fix
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
        obj.put("announcementId", jobAnnouncementDAO.getUniqueId(jobApp.whichJobAnnouncement()));
        obj.put("candidateEmail", jobApp.whoIsCandidate().getEmail());
        obj.put("status", jobApp.currentApplicationStatus().name());
        obj.put("raiseOffer", jobApp.currentRaiseAmount() != null ? jobApp.currentRaiseAmount() : BigDecimal.ZERO);
        return obj;
    }

    private JSONArray readJsonFile() {
        File file = new File(this.PATH);
        if (!file.exists()) {
            return new JSONArray();
        }

        try {
            String content = new String(Files.readAllBytes(file.toPath()));
            if (content.isBlank()) return new JSONArray();
            return new JSONArray(content);
        } catch (IOException e) {
            throw new DAOException("Error reading json file " + this.PATH, e);
        }
    }

    private void writeJsonFile(JSONArray array) {
        File file = new File(this.PATH);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {

            if (!parentDir.mkdirs()){
                throw new DAOException("Couldn't create parent directory " + parentDir.getAbsolutePath());
            }
        }

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(array.toString(4));
        } catch (IOException e) {
            throw new DAOException("Error writing json file " + this.PATH, e);
        }
    }


}
