package dao.application;

import engineering.enums.ApplicationStatus;
import engineering.persistency.CsvManager;
import exception.DAOException;
import model.JobApplication;

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
    }

    @Override
    public List<JobApplication> getAllJobApplicationsFromEmail(String email) {
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

        if (applications.isEmpty()) {
            throw new DAOException("No applications found for email: " + email);
        }

        return applications;
    }

    @Override
    protected JobApplication retrieveJobApplicationById(String id) {
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
            throw new DAOException("Couldn't find job application with id: " + id);
        } catch (IOException e) {
            throw new DAOException("Can't read job application with id: " + id, e);
        }
    }

    @Override
    protected String getUniqueId(JobApplication jobApp) {
        return jobApp.getCandidateEmail() + "~" + jobApp.getApplicationAnnouncementId();
    }

    @Override
    protected void saveToPersistency(JobApplication obj) {
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
            throw new DAOException("Couldn't save job application with id " + id, e);
        }
    }

    private JobApplication parseRow(String[] fields) {
        String announcementId = fields[1];
        String candidateEmail = fields[2];
        ApplicationStatus status = ApplicationStatus.valueOf(fields[3]);
        BigDecimal raiseOffer = new BigDecimal(fields[4]);

        return new JobApplication(announcementId, candidateEmail, status, raiseOffer);
    }

    private String toCsvRow(String id, JobApplication jobApp) {
        return String.join(CSV_SEPARATOR,
                id,
                jobApp.getApplicationAnnouncementId(),
                jobApp.getCandidateEmail(),
                jobApp.currentApplicationStatus().name(),
                jobApp.currentRaiseAmount() != null ? jobApp.currentRaiseAmount().toString() : "0"
        );
    }
}
