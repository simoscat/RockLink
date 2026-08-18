package engineering;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestEmailChecker {

    @Test
    void testEmailWithSpaces(){

        String email = "jane  doe@f.com"; //spaces shouldn't be allowed
        boolean output = EmailChecker.isValidEmail(email);

        assertEquals(false, output);

    }

    @Test
    void testEmailWithDoubleAt(){

        String email = "jane@@doe.com";

        boolean output = EmailChecker.isValidEmail(email);

        assertEquals(false, output);

    }

    @Test
    void testNoAtEmail(){

        String email = "janedoe.com";

        boolean output = EmailChecker.isValidEmail(email);

        assertEquals(false, output);

    }


    @Test
    void testValidEmail(){

        String email = "jane@doe.com";

        boolean output = EmailChecker.isValidEmail(email);

        assertEquals(true, output);

    }

}
