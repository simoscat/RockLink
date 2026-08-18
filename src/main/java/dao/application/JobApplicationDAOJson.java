package dao.application;

import engineering.enums.ApplicationStatus;
import exception.DAOException;
import model.JobApplication;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

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
    public List<JobApplication> getAllJobApplicationsFromEmail(String email) {
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
    protected JobApplication retrieveJobApplicationById(String id) {
        JSONArray applications = readJsonFile();
        for (int i = 0; i < applications.length(); i++) {
            JSONObject obj = applications.getJSONObject(i);
            if (obj.getString("id").equals(id)) {
                return parseJson(obj);
            }
        }
        throw new DAOException("Couldn't find job application with id: " + id);
    }

    @Override
    protected String getUniqueId(JobApplication jobApp) {
        return jobApp.getCandidateEmail() + "~" + jobApp.getApplicationAnnouncementId();
    }

    @Override
    protected void saveToPersistency(JobApplication obj) {
        JSONArray applications = readJsonFile();
        String id = getUniqueId(obj);
        boolean found = false;

        for (int i = 0; i < applications.length(); i++) {
            JSONObject json = applications.getJSONObject(i);
            if (json.getString("id").equals(id)) {
                applications.put(i, toJson(id, obj));
                found = true;
                break;
            }
        }

        if (!found) {
            applications.put(toJson(id, obj));
        }

        writeJsonFile(applications);
    }

    private JobApplication parseJson(JSONObject obj) {
        String announcementId = obj.getString("announcementId");
        String candidateEmail = obj.getString("candidateEmail");
        ApplicationStatus status = ApplicationStatus.valueOf(obj.getString("status"));
        BigDecimal raiseOffer = obj.getBigDecimal("raiseOffer");

        return new JobApplication(announcementId, candidateEmail, status, raiseOffer);
    }

    private JSONObject toJson(String id, JobApplication jobApp) {
        JSONObject obj = new JSONObject();
        obj.put("id", id);
        obj.put("announcementId", jobApp.getApplicationAnnouncementId());
        obj.put("candidateEmail", jobApp.getCandidateEmail());
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
            parentDir.mkdirs();
        }

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(array.toString(4));
        } catch (IOException e) {
            throw new DAOException("Error writing json file " + this.PATH, e);
        }
    }
}
