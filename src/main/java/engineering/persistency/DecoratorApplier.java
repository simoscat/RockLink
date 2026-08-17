package engineering.persistency;

import engineering.enums.AnnouncementTag;
import model.Announcement;
import model.decorators.*;

import java.util.List;

public class DecoratorApplier {

    public static Announcement applyDecorators(Announcement a, List<AnnouncementTag> tags){
        if (tags == null || tags.isEmpty()) return a;

        for (AnnouncementTag tag : tags){

            switch (tag){
                case AnnouncementTag.URGENT -> a = new UrgentAnnouncementDecorator(a);
                case AnnouncementTag.EXPERTS_ONLY -> a = new ExpertsOnlyDecorator(a);
                case AnnouncementTag.LONG_TIME_CONTRACT -> a = new LongTimeContractDecorator(a);
                case AnnouncementTag.NEGOTIABLE_SALARY -> a = new NegotiableSalaryDecorator(a);
            }

        }

        return a;
    }



}
