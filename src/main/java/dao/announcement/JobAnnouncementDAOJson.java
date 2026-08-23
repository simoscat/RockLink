package dao.announcement;

import dao.factories.DAOFactory;
import dao.promoter.PromoterDAO;
import engineering.persistency.ConfigManager;
import engineering.enums.CurrencyType;
import engineering.enums.JobAnnouncementStatus;
import engineering.enums.JobAnnouncementTag;
import engineering.persistency.JobDecoratorManager;
import engineering.persistency.JsonManager;
import exception.DAOException;
import model.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JobAnnouncementDAOJson extends JobAnnouncementDAO {

    private final String path;
    private static final String PROMOTER_EMAIL_FIELD = "promoterEmail";
    private static final String HIRED_ARTIST_FIELD = "hiredArtist";

    public JobAnnouncementDAOJson() {
        path = ConfigManager.getProperty("json.path") + "job_announcements.json";
    }

    @Override
    protected List<JobAnnouncement> retrieveAllPromoterAnnouncementsFromEmail(String email) {

        JSONArray jobs = JsonManager.readJsonFile(this.path);
        List<JobAnnouncement> promoterJobs = new ArrayList<>();

        for (int i = 0; i < jobs.length(); i++) {
            JSONObject obj = jobs.getJSONObject(i);
            if (obj.getString(PROMOTER_EMAIL_FIELD).equals(email)) {
                promoterJobs.add(parseJson(obj));
            }
        }

        return promoterJobs;
    }

    @Override
    public JobAnnouncement retrieveJobAnnouncementById(String id) {
        JSONArray jobs = JsonManager.readJsonFile(this.path);
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
        return job.getPublisher().getEmail() + "~" + job.getAnnouncementPublishDate().toString();
    }

    @Override
    protected List<JobAnnouncement> retrieveAllJobAnnouncements() {
        JSONArray jobs = JsonManager.readJsonFile(this.path);
        List<JobAnnouncement> announcements = new ArrayList<>();
        for (int i = 0; i < jobs.length(); i++) {
            announcements.add(parseJson(jobs.getJSONObject(i)));
        }
        return announcements;
    }

    @Override
    protected void saveToPersistency(JobAnnouncement obj) {
        String id = getUniqueId(obj);
        JsonManager.upsertFile(this.path, json -> json.getString("id").equals(id), toJson(id, obj));
    }

    private JobAnnouncement parseJson(JSONObject obj) {
        String title = obj.getString("title");
        String content = obj.getString("content");
        LocalDateTime date = LocalDateTime.parse(obj.getString("date"));
        JobAnnouncementStatus status = JobAnnouncementStatus.valueOf(obj.getString("status"));
        LocalDateTime publishDate = LocalDateTime.parse(obj.getString("publishDate"));
        String promoterEmail = obj.getString(PROMOTER_EMAIL_FIELD);
        BigDecimal salaryAmount = obj.getBigDecimal("salaryAmount");
        CurrencyType currency = CurrencyType.valueOf(obj.getString("currency"));
        String address = obj.getString("address");

        Artist artist;

        if (!obj.getString(HIRED_ARTIST_FIELD).isEmpty()) {
            artist = DAOFactory.getInstance().getArtistDAO().getArtistByEmail(obj.getString(HIRED_ARTIST_FIELD));
        }
        else{
            artist = null;
        }

        PromoterDAO promoterDAO = DAOFactory.getInstance().getPromoterDAO();
        Promoter promoter = promoterDAO.getPromoterByEmail(promoterEmail);


        JobAnnouncement job =  new ConcreteJobAnnouncement(
                title, content, date, publishDate, promoter, new MoneyValue(salaryAmount, currency), address
        );

        job.hireArtist(artist);
        job.setStatus(status);

        JSONArray tags = obj.getJSONArray("tags");

        List<JobAnnouncementTag> tagList = new ArrayList<>();

        for (int i = 0; i < tags.length(); i++) {
            tagList.add(JobAnnouncementTag.valueOf(tags.getString(i)));
        }

        job = JobDecoratorManager.applyDecorators(job, tagList);

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
        obj.put(PROMOTER_EMAIL_FIELD, cja.getPublisher().getEmail());
        obj.put("salaryAmount", cja.getJobPay().moneyAmount());
        obj.put("currency", cja.getJobPay().whichCurrency().name());
        obj.put("address", cja.getEventAddress());

        if (cja.whoWasHired() != null){
            obj.put(HIRED_ARTIST_FIELD, cja.whoWasHired().getEmail());
        }
        else{
            obj.put(HIRED_ARTIST_FIELD, "");
        }


        JSONArray tags = new JSONArray();

        List<JobAnnouncementTag> tagList = JobDecoratorManager.getTagsList(job);

        for (JobAnnouncementTag tag : tagList) {
            tags.put(tag.name());
        }

        obj.put("tags", tags);

        return obj;
    }
}
