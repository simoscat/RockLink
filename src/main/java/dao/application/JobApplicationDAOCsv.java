package dao.application;

import dao.factories.DAOFactory;
import dao.musician.MusicianDAO;
import engineering.enums.ApplicationStatus;
import engineering.persistency.CsvManager;
import exception.DAOException;
import model.Artist;
import model.JobAnnouncement;
import model.JobApplication;
import model.Musician;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;




public class JobApplicationDAOCsv extends JobApplicationDAO {

    private static final String CSV_SEPARATOR = ",";
    private final String PATH;

    public JobApplicationDAOCsv() {
        try(InputStream is = new FileInputStream("config.properties")){
            Properties prop = new Properties();
            prop.load(is);
            PATH = prop.getProperty("csv.path") + "job_applications.csv";
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

        jobAnnouncementDAO = DAOFactory.getInstance().getJobAnnouncementDAO();

    }

    @Override
    public List<JobApplication> retrieveAllJobApplicationsFromEmail(String email) {
        List<JobApplication> applications = new ArrayList<>();
        File file = new File(this.PATH);
        try (BufferedReader reader = Files.newBufferedReader(file.toPath())) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] fields = line.split(CSV_SEPARATOR, -1);
                if (fields[2].equals(email)) {
                    applications.add(parseRow(fields));
                }
            }
        } catch (IOException e) {
            throw new DAOException("Can't read job applications", e);
        }

        return applications;
    }


    @Override
    protected void saveToPersistency(JobApplication obj) {
        List<String> lines = new ArrayList<>();
        boolean found = false;

        File file = new File(this.PATH);
        try (BufferedReader reader = Files.newBufferedReader(file.toPath())) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] fields = line.split(CSV_SEPARATOR, -1);
                if (fields[0].equals(obj.whoIsCandidate().getEmail()) &&
                        fields[1].equals(jobAnnouncementDAO.getUniqueId(obj.whichJobAnnouncement()))) {
                    lines.add(toCsvRow(obj));
                    found = true;
                } else {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            throw new DAOException("Couldn't read csv file " + this.PATH, e);
        }

        if (!found) {
            lines.add(toCsvRow(obj));
        }

        try (BufferedWriter writer = Files.newBufferedWriter(file.toPath())) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new DAOException("Couldn't save job application with email " + obj.whoIsCandidate().getEmail() +
                    " and announcement id " + jobAnnouncementDAO.getUniqueId(obj.whichJobAnnouncement()), e);
        }
    }

    private JobApplication parseRow(String[] fields) {
        String candidateEmail = fields[0];
        String announcementId = fields[1];
        ApplicationStatus status = ApplicationStatus.valueOf(fields[3]);
        BigDecimal raiseOffer = new BigDecimal(fields[4]);

        //right now, musician is the only artist, so this is just a temporary fix
        MusicianDAO musicianDAO = DAOFactory.getInstance().getMusicianDAO();

        Musician m = musicianDAO.getMusicianByEmail(candidateEmail);

        return new JobApplication(
                jobAnnouncementDAO.getAnnouncementFromId(announcementId),
                m, status, raiseOffer
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

        File file = new File(this.PATH);

        try (BufferedReader reader = Files.newBufferedReader(file.toPath())) {

            String line;

            while ((line = reader.readLine()) != null) {

                if (line.isBlank()) continue;

                String[] fields = line.split(CSV_SEPARATOR, -1);

                if (fields[1].equals(jobId)) {
                    applications.add(parseRow(fields));
                }

            }

        } catch (IOException e) {
            throw new DAOException("Can't read job applications", e);
        }

        return applications;


    }

    @Override
    protected JobApplication retrieveJobApplication(String candidateEmail, JobAnnouncement jobAnnouncement) {

        String jobId = jobAnnouncementDAO.getUniqueId(jobAnnouncement);

        File file = new File(this.PATH);

        try (BufferedReader reader = Files.newBufferedReader(file.toPath())) {

            String line;

            while ((line = reader.readLine()) != null) {

                if (line.isBlank()) continue;

                String[] fields = line.split(CSV_SEPARATOR, -1);

                if (fields[0].equals(candidateEmail) && fields[1].equals(jobId)) {
                    return parseRow(fields);
                }

            }

        } catch (IOException e) {
            throw new DAOException("Can't read job applications", e);
        }

        return null;

    }

}
