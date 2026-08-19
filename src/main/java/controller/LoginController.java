package controller;

import bean.InstrumentBean;
import bean.MusicianBean;
import bean.PromoterBean;
import bean.SessionBean;
import dao.auth.AuthDAO;
import dao.factories.DAOFactory;
import dao.instrument.InstrumentDAO;
import dao.musician.MusicianDAO;
import dao.promoter.PromoterDAO;
import engineering.PasswordEncrypter;
import engineering.Session;
import engineering.SessionManager;
import engineering.enums.Gender;
import engineering.enums.Mastery;
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

    private final AuthDAO authDAO = DAOFactory.getInstance().getAuthDAO();
    private final MusicianDAO musicianDAO = DAOFactory.getInstance().getMusicianDAO();
    private final PromoterDAO promoterDAO = DAOFactory.getInstance().getPromoterDAO();


    public SessionBean musicianLogIn(MusicianBean musician) {

        try {
            Credential creds = authDAO.getMusicianCredential(musician.getEmail());

            if (PasswordEncrypter.checkPassword(musician.getPassword(), creds.getCryptPassword())){// correct password

                Musician m = musicianDAO.getMusicianByEmail(musician.getEmail());


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
            throw new WrongCredentialsException("Invalid musician credentials");
        }

    }

    public SessionBean promoterLogin(PromoterBean promoter) throws WrongCredentialsException {

        try {
            Credential creds = authDAO.getPromoterCredential(promoter.getEmail());

            if (PasswordEncrypter.checkPassword(promoter.getPassword(), creds.getCryptPassword())){// correct password

                Promoter p = promoterDAO.getPromoterByEmail(promoter.getEmail());

                promoter.setName(p.getName());
                promoter.setSurname(p.getSurname());
                promoter.setContacts(p.promoterContacts());
                promoter.setEmail(p.getEmail());
                promoter.setGender(p.getGender().name());
                promoter.clearPassword();

                Session newSesh = SessionManager.getInstance().getNewSession(p);

                return new SessionBean(newSesh.getId(), p);
            }
            else{
                throw new ControllerLogicException("Login failed");
            }
        }
        catch (DAOException e){
            throw new WrongCredentialsException("Invalid promoter credentials");
        }

    }

    public SessionBean musicianRegistration(MusicianBean musician) {

        try{

            String email = musician.getEmail();

            if (authDAO.isMusicianAlreadyRegistered(email)){

                throw new ControllerLogicException("Email is already in use");

            }

            String cryptPassword = PasswordEncrypter.encryptPassword(musician.getPassword());

            Credential creds = new Credential(email, cryptPassword);

            authDAO.registerMusician(creds);

            this.createAndSaveMusician(musician);

            return this.musicianLogIn(musician); // we log in after registration

        }

        catch (DAOException e){
            throw new ControllerLogicException("Registration failed");
        }

    }

    public SessionBean promoterRegistration(PromoterBean promoter) {

        try{
            String email = promoter.getEmail();

            if (authDAO.isPromoterAlreadyRegistered(email)){
                throw new ControllerLogicException("Email is already in use");
            }

            String cryptPassword = PasswordEncrypter.encryptPassword(promoter.getPassword());

            Credential creds = new Credential(email, cryptPassword);

            authDAO.registerPromoter(creds);

            this.createAndSavePromoter(promoter);

            return this.promoterLogin(promoter);

        }
        catch (DAOException e){

            throw new ControllerLogicException("Registration failed");

        }


    }

    private void createAndSaveMusician(MusicianBean musician) {

        List<Instrument> instruments = new ArrayList<>();

        for (InstrumentBean iBean : musician.getInstruments()){

            Instrument i = new Instrument(
                    iBean.getName(),
                    Mastery.valueOf(iBean.getMastery())
            );
            instruments.add(i);
        }

        Musician m = new Musician(
                musician.getName(),
                musician.getSurname(),
                musician.getStageName(),
                musician.getEmail(),
                Gender.valueOf(musician.getGender()),
                instruments
        );

        try {
            musicianDAO.save(m);
        }
        catch(DAOException e){
            throw new ControllerLogicException("Musician saving failed. Please contact support.");
        }

    }

    private void createAndSavePromoter(PromoterBean promoter) {

        Promoter p = new Promoter(
                promoter.getName(),
                promoter.getSurname(),
                promoter.getEmail(),
                Gender.valueOf(promoter.getGender()),
                promoter.getContacts()
        );

        try {
            promoterDAO.save(p);
        }
        catch(DAOException e){
            throw new ControllerLogicException("Promoter saving failed. Please contact support.");
        }

    }

}
