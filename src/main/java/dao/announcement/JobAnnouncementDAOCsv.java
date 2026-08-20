package dao.announcement;

import dao.factories.DAOFactory;
import dao.promoter.PromoterDAO;
import engineering.enums.CurrencyType;
import engineering.enums.JobAnnouncementStatus;
import engineering.enums.JobAnnouncementTag;
import engineering.persistency.CsvManager;
import engineering.persistency.JobDecoratorManager;
import exception.DAOException;
import model.ConcreteJobAnnouncement;
import model.JobAnnouncement;
import model.MoneyValue;
import model.Promoter;
import model.jobAnnouncementDecorators.*;

import java.io.*;
import java.math.BigDecimal;
import java.nio.Buffer;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

//TODO CONTROLLA!!!!


public class JobAnnouncementDAOCsv extends JobAnnouncementDAO {

    private static final String CSV_SEPARATOR = ",";
    private static final String LIST_SEPARATOR = ";";
    private final String PATH;

    private static final String URGENT = "URGENT";
    private static final String EXPERTS_ONLY = "EXPERTS_ONLY";
    private static final String LONG_TIME_CONTRACT = "LONG_TIME_CONTRACT";
    private static final String NEGOTIABLE_SALARY = "NEGOTIABLE_SALARY";

    public JobAnnouncementDAOCsv() {
        try(InputStream is = new FileInputStream("config.properties")){
            Properties prop = new Properties();
            prop.load(is);
            PATH = prop.getProperty("csv.path") + "job_announcements.csv";
        } catch (FileNotFoundException e) {
            throw new DAOException("Couldn't find properties file", e);
        } catch (IOException e) {
            throw new DAOException("Couldn't read properties file", e);
        }

        try {
            CsvManager.initCsvFile(this.PATH);
        } catch (IOException e) {
            throw new DAOException("Can't initialize csv file " + this.PATH, e);
        }
    }

    @Override
    protected List<JobAnnouncement> retrieveAllPromoterAnnouncementsFromEmail(String email) {

        File file = new File(this.PATH);
        List<JobAnnouncement> jobAnnouncements = new ArrayList<>();

        try(BufferedReader reader = Files.newBufferedReader(file.toPath())){

            String line;
            while ((line = reader.readLine()) != null){

                if (line.isBlank()) continue;

                String[] fields = line.split(CSV_SEPARATOR, -1);

                if (fields[6].equals(email)){
                    jobAnnouncements.add(parseRow(fields));
                }

            }

        } catch (IOException e) {
            throw new DAOException("Can't read promoter job announcements with email: "+email, e);
        }

        return jobAnnouncements;

    }

    @Override
    public JobAnnouncement retrieveJobAnnouncementById(String id) {
        File file = new File(this.PATH);
        try (BufferedReader reader = Files.newBufferedReader(file.toPath())) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] fields = line.split(CSV_SEPARATOR, -1);
                if (fields[0].equals(id)) {
                    return parseRow(fields);
                }
            }
            throw new DAOException("Couldn't find job with id: " + id);
        } catch (IOException e) {
            throw new DAOException("Can't read job announcement with id: " + id, e);
        }
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
        List<JobAnnouncement> announcements = new ArrayList<>();
        File file = new File(this.PATH);
        try (BufferedReader reader = Files.newBufferedReader(file.toPath())) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] fields = line.split(CSV_SEPARATOR, -1);
                announcements.add(parseRow(fields));
            }
        } catch (IOException e) {
            throw new DAOException("Can't read job announcements", e);
        }
        return announcements;
    }


    @Override
    protected void saveToPersistency(JobAnnouncement obj) {

        List<String> lines = new ArrayList<>();
        boolean found = false;
        String id = getUniqueId(obj);

        File file = new File(this.PATH);
        try (BufferedReader reader = Files.newBufferedReader(file.toPath())) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] fields = line.split(CSV_SEPARATOR, -1);
                if (fields[0].equals(id)) {
                    lines.add(toCsvRow(id, obj));
                    found = true;
                } else {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            throw new DAOException("Couldn't read csv file " + this.PATH, e);
        }

        if (!found) {
            lines.add(toCsvRow(id, obj));
        }

        try (BufferedWriter writer = Files.newBufferedWriter(file.toPath())) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new DAOException("Couldn't save job announcement with id " + id, e);
        }
    }

    private JobAnnouncement parseRow(String[] fields) {
        String title = fields[1].replace("%2C", ",").replace("%0A", "\n");
        String content = fields[2].replace("%2C", ",").replace("%0A", "\n");
        LocalDateTime date = LocalDateTime.parse(fields[3]);
        JobAnnouncementStatus status = JobAnnouncementStatus.valueOf(fields[4]);
        LocalDateTime publishDate = LocalDateTime.parse(fields[5]);
        String promoterEmail = fields[6];
        BigDecimal salaryAmount = new BigDecimal(fields[7]);
        CurrencyType currency = CurrencyType.valueOf(fields[8]);
        String address = fields[9].replace("%2C", ",");

        String decorators = fields[10];

        PromoterDAO promoterDAO = DAOFactory.getInstance().getPromoterDAO();
        Promoter promoter = promoterDAO.getPromoterByEmail(promoterEmail);

        JobAnnouncement job = new ConcreteJobAnnouncement(
                title, content, date, status, publishDate, promoter, new MoneyValue(salaryAmount, currency), address
        );



        JobAnnouncement decoratedJob = JobDecoratorManager.applyDecorators(job, getTagList(decorators));

        return decoratedJob;

    }

    private String toCsvRow(String id, JobAnnouncement job) {

        JobAnnouncement cja = JobDecoratorManager.unwrapJobAnnouncement(job);

        return String.join(CSV_SEPARATOR,
                id,
                cja.getTitle().replace(",", "%2C").replace("\n", "%0A"),
                cja.getContent().replace(",", "%2C").replace("\n", "%0A"),
                cja.getAnnouncementDate().toString(),
                cja.getStatus().name(),
                cja.getAnnouncementPublishDate().toString(),
                cja.getPublisher().getEmail(),
                cja.getJobPay().moneyAmount().toString(),
                cja.getJobPay().whichCurrency().name(),
                cja.getEventAddress().replace(",", "%2C"),
                getWrappingChainCsv(job)
        );
    }

    private String getWrappingChainCsv(JobAnnouncement job) {

        JobAnnouncement current = job;

        StringBuilder sb = new StringBuilder();

        while (current instanceof JobAnnouncementDecorator jad){

            if (current instanceof ExpertsOnlyDecoratorJob){
                sb.append(EXPERTS_ONLY);
            }

            else if (current instanceof LongTimeContractDecoratorJob){
                sb.append(LONG_TIME_CONTRACT);
            }

            else if (current instanceof NegotiableSalaryDecoratorJob){
                sb.append(NEGOTIABLE_SALARY);
            }

            else if (current instanceof UrgentJobAnnouncementDecorator){
                sb.append(URGENT);
            }

            current = jad.unwrapAnnouncement();

            if (current instanceof JobAnnouncementDecorator){
                sb.append(LIST_SEPARATOR);
            }

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

            }

        }

        return tags;

    }
}
