package dao.application;

import dao.artist.ArtistDAO;
import dao.factories.DAOFactory;
import engineering.persistency.ConfigManager;
import engineering.enums.ApplicationStatus;
import engineering.persistency.CsvManager;
import exception.DAOException;
import model.Artist;
import model.JobAnnouncement;
import model.JobApplication;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;




public class JobApplicationDAOCsv extends JobApplicationDAO {

    private static final String CSV_SEPARATOR = ",";
    private final String path;
    private static final int EMAIL_FIELD = 0;
    private static final int ID_FIELD = 1;
    private static final int STATUS_FIELD = 2;
    private static final int RAISE_FIELD = 3;

    public JobApplicationDAOCsv() {
        path = ConfigManager.getProperty("csv.path") + "job_applications.csv";

        try {
            CsvManager.initCsvFile(this.path);
        } catch (IOException e) {
            throw new DAOException("Can't initialize csv file " + this.path, e);
        }

    }

    @Override
    public List<JobApplication> retrieveAllJobApplicationsFromEmail(String email) {
        List<JobApplication> applications = new ArrayList<>();

        for (String[] fields : CsvManager.filterRows(this.path, f -> f[EMAIL_FIELD].equals(email))) {
            applications.add(parseRow(fields));
        }

        return applications;
    }


    @Override
    protected void saveToPersistency(JobApplication obj) {
        String candidateEmail = obj.whoIsCandidate().getEmail();
        String announcementId = jobAnnouncementDAO.getUniqueId(obj.whichJobAnnouncement());

        CsvManager.upsertRow(this.path,
                fields -> fields[EMAIL_FIELD].equals(candidateEmail) && fields[ID_FIELD].equals(announcementId),
                toCsvRow(obj));
    }

    private JobApplication parseRow(String[] fields) {
        String candidateEmail = fields[EMAIL_FIELD];
        String announcementId = fields[ID_FIELD];
        ApplicationStatus status = ApplicationStatus.valueOf(fields[STATUS_FIELD]);
        BigDecimal raiseOffer = new BigDecimal(fields[RAISE_FIELD]);

        ArtistDAO artistDAO = DAOFactory.getInstance().getArtistDAO();

        Artist a = artistDAO.getArtistByEmail(candidateEmail);

        return new JobApplication(
                jobAnnouncementDAO.getAnnouncementFromId(announcementId),
                a, status, raiseOffer
        );
    }

    private String toCsvRow(JobApplication jobApp) {
        return String.join(CSV_SEPARATOR,
                jobApp.whoIsCandidate().getEmail(),
                jobAnnouncementDAO.getUniqueId(jobApp.whichJobAnnouncement()),
                jobApp.currentApplicationStatus().name(),
                jobApp.currentRaiseAmount() != null ? jobApp.currentRaiseAmount().toString() : "0"
        );
    }

    @Override
    protected List<JobApplication> retrieveAllJobApplicationsFromJob(JobAnnouncement jobAnnouncement) {
        String jobId = jobAnnouncementDAO.getUniqueId(jobAnnouncement);
        List<JobApplication> applications = new ArrayList<>();

        for (String[] fields : CsvManager.filterRows(this.path, f -> f[ID_FIELD].equals(jobId))) {
            applications.add(parseRow(fields));
        }

        return applications;
    }

    @Override
    protected JobApplication retrieveJobApplication(String candidateEmail, JobAnnouncement jobAnnouncement) {
        String jobId = jobAnnouncementDAO.getUniqueId(jobAnnouncement);

        String[] fields = CsvManager.findRow(this.path,
                f -> f[EMAIL_FIELD].equals(candidateEmail) && f[ID_FIELD].equals(jobId));

        if (fields == null) {
            return null;
        }

        return parseRow(fields);
    }

}
