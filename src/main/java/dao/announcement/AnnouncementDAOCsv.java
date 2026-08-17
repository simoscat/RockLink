package dao.announcement;

import engineering.enums.AnnouncementStatus;
import engineering.enums.AnnouncementTag;
import engineering.enums.AnnouncementType;
import model.Announcement;
import model.BaseAnnouncement;
import model.JobAnnouncement;
import model.decorators.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AnnouncementDAOCsv extends AnnouncementDAO {

    /*
    Id structure:
    - job announcement: "J"+<id>
    if in the future other announcements will be added (e.g., BandAnnouncement) the id will be
    <L>+<id>, where <L> is the first letter of the announcement type name
     */

    /*
    Structure for job postings:
    id,DECORATOR1;DECORATOR2;...;DECORATORn,title,content,date,
     */

    private static final String CSV_SEPARATOR = ",";
    private static final String LIST_SEPARATOR = ";";
    private static final String DATE_SEPARATOR = "-";
    private static final String ID_TYPE_SEPARATOR = "?";

    private String getDecoratorChain(Announcement announcement) {

        StringBuilder chain =  new StringBuilder();

        Announcement current = announcement;

        while (current instanceof AnnouncementDecorator ad){

            switch (current) {
                case ExpertsOnlyDecorator expertsOnlyDecorator -> chain.append(AnnouncementTag.EXPERTS_ONLY.name());
                case LongTimeContractDecorator longTimeContractDecorator ->
                        chain.append(AnnouncementTag.LONG_TIME_CONTRACT.name());
                case NegotiableSalaryDecorator negotiableSalaryDecorator ->
                        chain.append(AnnouncementTag.NEGOTIABLE_SALARY);
                case UrgentAnnouncementDecorator urgentAnnouncementDecorator ->
                        chain.append(AnnouncementTag.URGENT.name());
                default -> throw new IllegalStateException("Unexpected value: " + current);
            }

            current = ad.unwrapAnnouncement();

            if (current instanceof AnnouncementDecorator){
                chain.append(";");
            }

        }

        return chain.toString();

    }

    private List<AnnouncementTag> getAnnouncementTags(String chain,
                                                             String LIST_SEPARATOR){

        String[] tokens = chain.split(LIST_SEPARATOR);

        List<AnnouncementTag> tags = new ArrayList<>();

        for (String token : tokens) {

            tags.add(AnnouncementTag.valueOf(token));

        }

        return tags;

    }

    //TODO
}
