package engineering.persistency;

import engineering.enums.JobAnnouncementTag;
import model.JobAnnouncement;
import model.jobAnnouncementDecorators.*;

import java.util.List;

public final class JobDecoratorManager {

    public static JobAnnouncement applyDecorators(JobAnnouncement a, List<JobAnnouncementTag> tags){
        if (tags == null || tags.isEmpty()) return a;

        for (JobAnnouncementTag tag : tags){

            switch (tag){
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



}
