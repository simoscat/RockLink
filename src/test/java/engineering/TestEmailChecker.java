package engineering;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestEmailChecker {

    @Test
    void testInvalidEmail(){

        String email = "jane@@doe.com";

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
