package controller;

import bean.NotificationBean;
import bean.UserBean;
import dao.factories.DAOFactory;
import dao.notification.NotificationDAO;
import engineering.BeanConverter;
import exception.ControllerLogicException;
import exception.DAOException;
import model.Notification;

import java.util.ArrayList;
import java.util.List;

//TODO NON È UN CASO D'USO MA HO IL CONTROLLER, NON SO SE VA BENE

public class ManageNotificationsController {

    public List<NotificationBean> getNotifications(UserBean u){

        return getUserNotifications(u.getEmail());

    }

    public List<NotificationBean> getUserNotifications(String email){

        try{

            NotificationDAO notificationDAO = DAOFactory.getInstance().getNotificationDAO();

            List<Notification> notifications = notificationDAO.getUserNotificationsByEmail(email);

            List<NotificationBean> notificationBeans = new ArrayList<>();

            for (Notification notification : notifications){

                notificationBeans.add(BeanConverter.fromNotificationToBean(notification));

            }

            return notificationBeans;

        }
        catch(DAOException _){
            throw new ControllerLogicException("Couldn't retrieve user notifications");
        }

    }

}
