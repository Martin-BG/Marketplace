package bg.softuni.marketplace.web.interceptors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class HelperTest {

    @ParameterizedTest
    @ValueSource(strings = {"redirect:someUrl", "redirect:/", "redirect:"})
    void isRedirectOrForward_withRedirect_isTrue(String url) {
        assertTrue(Helper.isRedirectOrForward(url));
    }

    @ParameterizedTest
    @ValueSource(strings = {"forward:someUrl", "forward:/", "forward:"})
    void isRedirectOrForward_withForward_isTrue(String url) {
        assertTrue(Helper.isRedirectOrForward(url));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Forward:/", "Redirect:/", "FORWARD:/", "REDIRECT:/"})
    void isRedirectOrForward_withNonLowercaseLetters_isFalse(String url) {
        assertFalse(Helper.isRedirectOrForward(url));
    }

    @ParameterizedTest
    @ValueSource(strings = {"/someUrl", "/user/home", "/forward", "/redirect"})
    void isRedirectOrForward_withUrl_isFalse(String url) {
        assertFalse(Helper.isRedirectOrForward(url));
    }

    @Test
    void isRedirectOrForward_withNull_throwsNPE() {
        assertThrows(NullPointerException.class, () -> Helper.isRedirectOrForward(null));
    }
}
