package dao.announcement;

import dao.factories.DAOFactory;
import dao.promoter.PromoterDAO;
import engineering.enums.CurrencyType;
import engineering.enums.JobAnnouncementStatus;
import exception.DAOException;
import model.ConcreteJobAnnouncement;
import model.JobAnnouncement;
import model.MoneyValue;
import model.Promoter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class JobAnnouncementDAOJson extends JobAnnouncementDAO {

    private final String PATH;

    public JobAnnouncementDAOJson() {
        try(InputStream is = new FileInputStream("config.properties")){
            Properties prop = new Properties();
            prop.load(is);
            PATH = prop.getProperty("json.path") + "job_announcements.json";
        } catch (FileNotFoundException e) {
            throw new DAOException("Couldn't find properties file", e);
        } catch (IOException e) {
            throw new DAOException("Couldn't read properties file", e);
        }
    }

    @Override
    public JobAnnouncement retrieveJobAnnouncementById(String id) {
        JSONArray jobs = readJsonFile();
        for (int i = 0; i < jobs.length(); i++) {
            JSONObject obj = jobs.getJSONObject(i);
            if (obj.getString("id").equals(id)) {
                return parseJson(obj);
            }
        }
        throw new DAOException("Couldn't find job with id: " + id);
    }

    @Override
    public String getUniqueId(JobAnnouncement job) {
        return job.getPublisher().getEmail() + "~" +
                job.getTitle().replaceAll(" ", "_").replaceAll("[^a-zA-Z0-9_]", "") + "~" +
                job.getAnnouncementPublishDate().toString();
    }

    @Override
    public List<JobAnnouncement> getAllJobAnnouncements() {
        JSONArray jobs = readJsonFile();
        List<JobAnnouncement> announcements = new ArrayList<>();
        for (int i = 0; i < jobs.length(); i++) {
            announcements.add(parseJson(jobs.getJSONObject(i)));
        }
        return announcements;
    }

    @Override
    protected void saveToPersistency(JobAnnouncement obj) {
        JSONArray jobs = readJsonFile();
        String id = getUniqueId(obj);
        boolean found = false;

        for (int i = 0; i < jobs.length(); i++) {
            JSONObject json = jobs.getJSONObject(i);
            if (json.getString("id").equals(id)) {
                jobs.put(i, toJson(id, obj));
                found = true;
                break;
            }
        }

        if (!found) {
            jobs.put(toJson(id, obj));
        }

        writeJsonFile(jobs);
    }

    private JobAnnouncement parseJson(JSONObject obj) {
        String title = obj.getString("title");
        String content = obj.getString("content");
        LocalDateTime date = LocalDateTime.parse(obj.getString("date"));
        JobAnnouncementStatus status = JobAnnouncementStatus.valueOf(obj.getString("status"));
        LocalDateTime publishDate = LocalDateTime.parse(obj.getString("publishDate"));
        String promoterEmail = obj.getString("promoterEmail");
        BigDecimal salaryAmount = obj.getBigDecimal("salaryAmount");
        CurrencyType currency = CurrencyType.valueOf(obj.getString("currency"));
        String address = obj.getString("address");

        PromoterDAO promoterDAO = DAOFactory.getInstance().getPromoterDAO();
        Promoter promoter = promoterDAO.getPromoterByEmail(promoterEmail);

        return new ConcreteJobAnnouncement(
                title, content, date, status, publishDate, promoter, new MoneyValue(salaryAmount, currency), address
        );
    }

    private JSONObject toJson(String id, JobAnnouncement job) {
        ConcreteJobAnnouncement cja = (ConcreteJobAnnouncement) job;
        JSONObject obj = new JSONObject();
        obj.put("id", id);
        obj.put("title", cja.getTitle());
        obj.put("content", cja.getContent());
        obj.put("date", cja.getAnnouncementDate().toString());
        obj.put("status", cja.getStatus().name());
        obj.put("publishDate", cja.getAnnouncementPublishDate().toString());
        obj.put("promoterEmail", cja.getPublisher().getEmail());
        obj.put("salaryAmount", cja.getJobPay().getAmount());
        obj.put("currency", cja.getJobPay().getCurrency().name());
        obj.put("address", cja.getEventAddress());
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
