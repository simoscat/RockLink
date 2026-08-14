package engineering;

import java.util.Objects;

public class PasswordEncrypter {

    public static String encryptPassword(String password){
        String encryptedPassword = "";

        for (int i = 0; i < password.length(); i++) {
            encryptedPassword = encryptedPassword + password.charAt(password.length() -i -1);
        }

        return encryptedPassword;
    }

    public static boolean checkPassword(String currentPassword, String encryptedPassword){
        return encryptedPassword.equals(encryptPassword(currentPassword));
    }

}
