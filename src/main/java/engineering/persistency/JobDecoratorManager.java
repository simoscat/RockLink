package engineering.persistency;

import engineering.enums.JobAnnouncementTag;
import model.JobAnnouncement;
import model.jobannouncementdecorators.*;

import java.util.ArrayList;
import java.util.List;

public final class JobDecoratorManager {

    private JobDecoratorManager() {}

    public static JobAnnouncement applyDecorators(JobAnnouncement a, List<JobAnnouncementTag> tags){
        if (tags == null || tags.isEmpty()) return a;

        for (int i = tags.size() - 1; i >= 0; i--) {

            switch (tags.get(i)) {
                case JobAnnouncementTag.URGENT -> a = new UrgentJobAnnouncementDecorator(a);
                case JobAnnouncementTag.EXPERTS_ONLY -> a = new ExpertsOnlyDecoratorJob(a);
                case JobAnnouncementTag.LONG_TIME_CONTRACT -> a = new LongTimeContractDecoratorJob(a);
                case JobAnnouncementTag.NEGOTIABLE_SALARY -> a = new NegotiableSalaryDecoratorJob(a);
            }

        }

        return a;
    }

    public static JobAnnouncement unwrapJobAnnouncement(JobAnnouncement job) {

        JobAnnouncement current = job;

        while (current instanceof JobAnnouncementDecorator jad){
            current = jad.unwrapAnnouncement();
        }

        return current;

    }

    public static List<JobAnnouncementTag> getTagsList(JobAnnouncement job){

        List<JobAnnouncementTag> tags = new ArrayList<>();

        while (job instanceof JobAnnouncementDecorator jad){

            if (job instanceof UrgentJobAnnouncementDecorator){
                tags.add(JobAnnouncementTag.URGENT);
            }

            else if (job instanceof ExpertsOnlyDecoratorJob){
                tags.add(JobAnnouncementTag.EXPERTS_ONLY);
            }

            else if (job instanceof LongTimeContractDecoratorJob){
                tags.add(JobAnnouncementTag.LONG_TIME_CONTRACT);
            }

            else if (job instanceof NegotiableSalaryDecoratorJob){
                tags.add(JobAnnouncementTag.NEGOTIABLE_SALARY);
            }

            job = jad.unwrapAnnouncement();

        }

        return tags;

    }



}
