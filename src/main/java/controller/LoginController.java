package controller;

import bean.InstrumentBean;
import bean.MusicianBean;
import bean.PromoterBean;
import bean.SessionBean;
import dao.auth.AuthDAO;
import dao.factories.DAOFactory;
import dao.musician.MusicianDAO;
import dao.promoter.PromoterDAO;
import engineering.PasswordEncrypter;
import engineering.Session;
import engineering.SessionManager;
import exception.ControllerLogicException;
import exception.DAOException;
import exception.WrongCredentialsException;
import model.Credential;
import model.Instrument;
import model.Musician;
import model.Promoter;

import java.util.ArrayList;
import java.util.List;


public class LoginController {

    public SessionBean musicianLogIn(MusicianBean musician) {
        AuthDAO authDAO = DAOFactory.getInstance().getAuthDAO();

        try {
            Credential creds = authDAO.getMusicianCredential(musician.getEmail());

            if (PasswordEncrypter.checkPassword(musician.getPassword(), creds.getCryptPassword())){// correct password
                MusicianDAO mDAO = DAOFactory.getInstance().getMusicianDAO();

                Musician m = mDAO.getMusicianByEmail(musician.getEmail());

                musician.setName(m.getName());
                musician.setSurname(m.getSurname());
                musician.setStageName(m.getArtistName());
                musician.clearPassword();

                List<InstrumentBean> instr = new ArrayList<>();

                for (Instrument i : m.presentInstruments()){
                    instr.add(new InstrumentBean(i.getName(), i.getMastery().name()));
                }

                musician.setInstruments(instr);

                Session newSesh = SessionManager.getInstance().getNewSession(m);

                return new SessionBean(newSesh.getId(), m);
            }
            else{
                throw new ControllerLogicException("Login failed");
            }
        }
        catch (DAOException e){
            throw new WrongCredentialsException("Invalid credentials");
        }

    }

    public SessionBean promoterLogin(PromoterBean promoter) throws WrongCredentialsException {
        AuthDAO authDAO = DAOFactory.getInstance().getAuthDAO();

        try {
            Credential creds = authDAO.getMusicianCredential(promoter.getEmail());

            if (PasswordEncrypter.checkPassword(promoter.getPassword(), creds.getCryptPassword())){// correct password

                PromoterDAO proDAO = DAOFactory.getInstance().getPromoterDAO();
                Promoter p = proDAO.getPromoterByEmail(promoter.getEmail());

                promoter.setName(p.getName());
                promoter.setSurname(p.getSurname());
                promoter.setRole(p.getRole());
                promoter.setJobEvent(p.getJobEvent());
                promoter.clearPassword();

                Session newSesh = SessionManager.getInstance().getNewSession(p);

                return new SessionBean(newSesh.getId(), p);
            }
            else{
                throw new ControllerLogicException("Login failed");
            }
        }
        catch (DAOException e){
            throw new WrongCredentialsException("Invalid credentials");
        }

    }

}
