package controller;

import engineering.enums.AnnouncementTag;
import model.*;
import model.decorators.ExpertsOnlyDecorator;
import model.decorators.LongTimeContractDecorator;
import model.decorators.NegotiableSalaryDecorator;
import model.decorators.UrgentAnnouncementDecorator;

import java.util.List;

//TODO
public class PublishJobPostingController {




    public Announcement applyDecorators(Announcement a, List<AnnouncementTag> tags){

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
