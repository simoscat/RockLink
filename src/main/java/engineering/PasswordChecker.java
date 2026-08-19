package engineering;

import java.util.List;

public class PasswordChecker {

    private static final List<String> INVALID_CHARACTERS =
            List.of(",", ";", ":", "|", "/");

    public static boolean isPasswordValid(String password){
        for (String invalidCharacter : INVALID_CHARACTERS){

            if (password.contains(invalidCharacter)){
                return false;
            }

        }

        return true;
    }

    public static String getInvalidCharacters(){
        return INVALID_CHARACTERS.toString();
    }

}
