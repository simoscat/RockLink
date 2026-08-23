package engineering;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestEmailChecker {

    @ParameterizedTest
    @CsvSource({
            "'jane  doe@f.com', false", // spaces shouldn't be allowed
            "'jane@@doe.com', false",
            "'janedoe.com', false",
            "'jane@doe.com', true"
    })
    void testEmailValidity(String email, boolean expected){

        boolean output = EmailChecker.isValidEmail(email);

        assertEquals(expected, output);

    }

}
