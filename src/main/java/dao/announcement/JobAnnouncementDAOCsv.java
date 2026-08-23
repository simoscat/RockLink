package dao.announcement;

import dao.factories.DAOFactory;
import dao.promoter.PromoterDAO;
import engineering.persistency.ConfigManager;
import engineering.enums.CurrencyType;
import engineering.enums.JobAnnouncementStatus;
import engineering.enums.JobAnnouncementTag;
import engineering.persistency.CsvManager;
import engineering.persistency.JobDecoratorManager;
import exception.DAOException;
import model.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JobAnnouncementDAOCsv extends JobAnnouncementDAO {

    private static final String CSV_SEPARATOR = ",";
    private static final String LIST_SEPARATOR = ";";
    private final String path;

    private static final String URGENT = "URGENT";
    private static final String EXPERTS_ONLY = "EXPERTS_ONLY";
    private static final String LONG_TIME_CONTRACT = "LONG_TIME_CONTRACT";
    private static final String NEGOTIABLE_SALARY = "NEGOTIABLE_SALARY";

    public JobAnnouncementDAOCsv() {
        path = ConfigManager.getProperty("csv.path") + "job_announcements.csv";

        try {
            CsvManager.initCsvFile(this.path);
        } catch (IOException e) {
            throw new DAOException("Can't initialize csv file " + this.path, e);
        }
    }

    @Override
    protected List<JobAnnouncement> retrieveAllPromoterAnnouncementsFromEmail(String email) {
        List<JobAnnouncement> jobAnnouncements = new ArrayList<>();

        for (String[] fields : CsvManager.filterRows(this.path, f -> f[7].equals(email))) {
            jobAnnouncements.add(parseRow(fields));
        }

        return jobAnnouncements;
    }

    @Override
    public JobAnnouncement retrieveJobAnnouncementById(String id) {
        String[] fields = CsvManager.findRow(this.path, f -> f[0].equals(id));

        if (fields == null) {
            throw new DAOException("Couldn't find job with id: " + id);
        }

        return parseRow(fields);
    }

    @Override
    public String getUniqueId(JobAnnouncement job) {
        return job.getPublisher().getEmail() + "~" + job.getAnnouncementPublishDate().toString();
    }

    @Override
    protected List<JobAnnouncement> retrieveAllJobAnnouncements() {
        List<JobAnnouncement> announcements = new ArrayList<>();

        for (String line : CsvManager.readAllLines(this.path)) {
            announcements.add(parseRow(line.split(CSV_SEPARATOR, -1)));
        }

        return announcements;
    }


    @Override
    protected void saveToPersistency(JobAnnouncement obj) {
        String id = getUniqueId(obj);
        CsvManager.upsertRow(this.path, fields -> fields[0].equals(id), toCsvRow(id, obj));
    }

    private JobAnnouncement parseRow(String[] fields) {

        String title = fields[1].replace("%2C", ",").replace("%0A", "\n");
        String content = fields[2].replace("%2C", ",").replace("%0A", "\n");
        LocalDateTime date = LocalDateTime.parse(fields[3]);
        JobAnnouncementStatus status = JobAnnouncementStatus.valueOf(fields[4]);

        Artist artist;

        if (!fields[5].isEmpty()) {
            artist = DAOFactory.getInstance().getArtistDAO().getArtistByEmail(fields[5]);
        }
        else{
            artist = null;
        }

        LocalDateTime publishDate = LocalDateTime.parse(fields[6]);
        String promoterEmail = fields[7];
        BigDecimal salaryAmount = new BigDecimal(fields[8]);
        CurrencyType currency = CurrencyType.valueOf(fields[9]);
        String address = fields[10].replace("%2C", ",");

        String decorators = fields[11];

        PromoterDAO promoterDAO = DAOFactory.getInstance().getPromoterDAO();
        Promoter promoter = promoterDAO.getPromoterByEmail(promoterEmail);

        JobAnnouncement job = new ConcreteJobAnnouncement(
                title, content, date, publishDate, promoter, new MoneyValue(salaryAmount, currency), address
        );

        job.hireArtist(artist);
        job.setStatus(status);

        return JobDecoratorManager.applyDecorators(job, getTagList(decorators));

    }

    private String toCsvRow(String id, JobAnnouncement job) {

        JobAnnouncement cja = JobDecoratorManager.unwrapJobAnnouncement(job);

        return String.join(CSV_SEPARATOR,
                id,
                cja.getTitle().replace(",", "%2C").replace("\n", "%0A"),
                cja.getContent().replace(",", "%2C").replace("\n", "%0A"),
                cja.getAnnouncementDate().toString(),
                cja.getStatus().name(),
                cja.whoWasHired() != null ? cja.whoWasHired().getEmail() : "",
                cja.getAnnouncementPublishDate().toString(),
                cja.getPublisher().getEmail(),
                cja.getJobPay().moneyAmount().toString(),
                cja.getJobPay().whichCurrency().name(),
                cja.getEventAddress().replace(",", "%2C"),
                getWrappingChainCsv(job)
        );
    }

    private String getWrappingChainCsv(JobAnnouncement job) {

        List<JobAnnouncementTag> tags = JobDecoratorManager.getTagsList(job);

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < tags.size(); i++) {
            sb.append(tags.get(i).name());

            if (i < tags.size() - 1) sb.append(LIST_SEPARATOR);
        }

        return sb.toString();

    }

    private List<JobAnnouncementTag> getTagList(String wrappingChain) {

        List<JobAnnouncementTag> tags = new ArrayList<>();

        String[] tagNames = wrappingChain.split(LIST_SEPARATOR);

        for (String tagName : tagNames) {

            switch (tagName) {

                case EXPERTS_ONLY -> tags.add(JobAnnouncementTag.EXPERTS_ONLY);
                case LONG_TIME_CONTRACT -> tags.add(JobAnnouncementTag.LONG_TIME_CONTRACT);
                case NEGOTIABLE_SALARY ->  tags.add(JobAnnouncementTag.NEGOTIABLE_SALARY);
                case URGENT ->  tags.add(JobAnnouncementTag.URGENT);
                default -> {//tags are empty, nothing to do
                    }

            }

        }

        return tags;

    }
}
