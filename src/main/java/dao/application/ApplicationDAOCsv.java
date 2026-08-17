package dao.application;

import dao.factories.DAOFactory;
import engineering.persistency.CsvManager;
import engineering.enums.ApplicationStatus;
import exception.DAOException;
import model.Announcement;
import model.Application;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

//TODO CHECK

public class ApplicationDAOCsv extends ApplicationDAO {

    /*
    Structure:
    announcementId,candidateEmail,status
     */

    private static final String CSV_SEPARATOR = ",";
    private final String PATH;
    private static final int NUM_FIELDS = 3;

    public ApplicationDAOCsv() {
        try (InputStream is = new FileInputStream("config.properties")) {
            Properties prop = new Properties();
            prop.load(is);
            PATH = prop.getProperty("csv.path") + "applications.csv";
        } catch (FileNotFoundException e) {
            throw new DAOException("Couldn't find properties file", e);
        } catch (IOException e) {
            throw new DAOException("Couldn't read properties file", e);
        }

        try {
            CsvManager.initCsvFile(PATH);
        } catch (IOException e) {
            throw new DAOException("Can't initialize csv file " + PATH, e);
        }
    }

    @Override
    protected List<Application> retrieveApplicationsFromAnnouncement(String id) {
        List<Application> applications = new ArrayList<>();
        File file = new File(PATH);

        try (BufferedReader reader = Files.newBufferedReader(file.toPath())) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;

                Application application = parseRowIfMatches(line, id);
                if (application != null) {
                    applications.add(application);
                }
            }

            return applications;
        } catch (IOException e) {
            throw new DAOException("Couldn't read application file: " + PATH, e);
        }
    }

    @Override
    protected void saveToPersistency(Application application) {
        List<String> lines = readAllLinesReplacingApplication(application);

        File file = new File(PATH);
        try (BufferedWriter writer = Files.newBufferedWriter(file.toPath())) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new DAOException("Couldn't save application for announcement " + application.getApplicationAnnouncement().getId(), e);
        }
    }

    private Application parseRowIfMatches(String line, String announcementId) {
        try {
            String[] fields = line.split(CSV_SEPARATOR, -1);

            if (fields.length < NUM_FIELDS || !fields[0].equals(announcementId)) {
                return null;
            }

            Announcement announcement = DAOFactory.getInstance().getJobAnnouncementDAO().getAnnouncementById(fields[0]);

            return new Application(announcement, fields[1], ApplicationStatus.valueOf(fields[2]));

        } catch (IllegalArgumentException e) {
            throw new DAOException("Invalid csv line: " + line, e);
        }
    }

    private List<String> readAllLinesReplacingApplication(Application application) {
        List<String> lines = new ArrayList<>();
        boolean found = false;

        File file = new File(PATH);
        try (BufferedReader reader = Files.newBufferedReader(file.toPath())) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;

                String[] fields = line.split(CSV_SEPARATOR, -1);
                if (fields.length > 0 && fields[0].equals(application.getApplicationAnnouncement().getId())
                        && fields.length > 1 && fields[1].equals(application.getCandidateEmail())) {
                    lines.add(toCsvRow(application));
                    found = true;
                } else {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            throw new DAOException("Couldn't read csv file " + PATH, e);
        }

        if (!found) {
            lines.add(toCsvRow(application));
        }

        return lines;
    }

    private String toCsvRow(Application application) {
        return String.join(CSV_SEPARATOR,
                application.getApplicationAnnouncement().getId(),
                application.getCandidateEmail(),
                application.currentApplicationStatus().name());
    }
}
