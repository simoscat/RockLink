package passwordEncryption;

import engineering.PasswordEncrypter;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class TestPasswordEncrypt {

    @Test
    void testPasswordEncrypting(){

        String password = "CiaoSonoUnaPassword1234!!!";
        String expected = "!!!4321drowssaPanUonoSoaiC";

        String output = PasswordEncrypter.encryptPassword(password);

        assertEquals(expected,output);

    }

}
