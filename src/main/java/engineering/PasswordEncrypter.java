package engineering;


public final class PasswordEncrypter {

    private PasswordEncrypter() {}

    public static String encryptPassword(String password){
        StringBuilder encryptedPassword = new StringBuilder();

        for (int i = 0; i < password.length(); i++) {
            encryptedPassword.append(password.charAt(password.length() -i -1));
        }

        return encryptedPassword.toString();
    }

    public static boolean checkPassword(String currentPassword, String encryptedPassword){
        return encryptedPassword.equals(encryptPassword(currentPassword));
    }

}
