package dao.announcement;

import dao.factories.DAOFactory;
import dao.promoter.PromoterDAO;
import engineering.enums.CurrencyType;
import engineering.enums.JobAnnouncementStatus;
import engineering.persistency.JobDecoratorManager;
import exception.DAOException;
import model.ConcreteJobAnnouncement;
import model.JobAnnouncement;
import model.MoneyValue;
import model.Promoter;
import model.jobAnnouncementDecorators.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

//TODO CONTROLLA!!!!

public class JobAnnouncementDAOJson extends JobAnnouncementDAO {

    private final String PATH;
    private static final String URGENT = "URGENT";
    private static final String EXPERTS_ONLY = "EXPERTS_ONLY";
    private static final String LONG_TIME_CONTRACT = "LONG_TIME_CONTRACT";
    private static final String NEGOTIABLE_SALARY = "NEGOTIABLE_SALARY";

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
//        return job.getPublisher().getEmail() + "~" +
//                job.getTitle().replaceAll(" ", "_").replaceAll("[^a-zA-Z0-9_]", "") + "~" +
//                job.getAnnouncementPublishDate().toString();

        return job.getPublisher().getEmail() + "~" + job.getAnnouncementPublishDate().toString();

    }

    @Override
    protected List<JobAnnouncement> retrieveAllJobAnnouncements() {
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

        JobAnnouncement job =  new ConcreteJobAnnouncement(
                title, content, date, status, publishDate, promoter, new MoneyValue(salaryAmount, currency), address
        );

        JSONArray tags = obj.getJSONArray("tags");

        for (int i = 0; i < tags.length(); i++) {

            job = switch(tags.getString(i)) {

                case EXPERTS_ONLY -> new ExpertsOnlyDecoratorJob(job);
                case LONG_TIME_CONTRACT -> new LongTimeContractDecoratorJob(job);
                case NEGOTIABLE_SALARY -> new NegotiableSalaryDecoratorJob(job);
                case URGENT -> new UrgentJobAnnouncementDecorator(job);
                default -> job;

            };

        }

        return job;
    }

    private JSONObject toJson(String id, JobAnnouncement job) {
        JSONObject obj = new JSONObject();

        JobAnnouncement cja = JobDecoratorManager.unwrapJobAnnouncement(job);

        obj.put("id", id);
        obj.put("title", cja.getTitle());
        obj.put("content", cja.getContent());
        obj.put("date", cja.getAnnouncementDate().toString());
        obj.put("status", cja.getStatus().name());
        obj.put("publishDate", cja.getAnnouncementPublishDate().toString());
        obj.put("promoterEmail", cja.getPublisher().getEmail());
        obj.put("salaryAmount", cja.getJobPay().moneyAmount());
        obj.put("currency", cja.getJobPay().whichCurrency().name());
        obj.put("address", cja.getEventAddress());

        JSONArray tags = new JSONArray();

        JobAnnouncement current = job;

        while (current instanceof JobAnnouncementDecorator jad){

            if (current instanceof ExpertsOnlyDecoratorJob){
                tags.put(EXPERTS_ONLY);
            }

            else if (current instanceof LongTimeContractDecoratorJob){
                tags.put(LONG_TIME_CONTRACT);
            }

            else if (current instanceof NegotiableSalaryDecoratorJob){
                tags.put(NEGOTIABLE_SALARY);
            }

            else if (current instanceof UrgentJobAnnouncementDecorator){
                tags.put(URGENT);
            }

            current = jad.unwrapAnnouncement();

        }

        obj.put("tags", tags);

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
