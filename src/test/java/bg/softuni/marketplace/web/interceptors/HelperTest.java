package bg.softuni.marketplace.web.interceptors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("interceptors/Helper.class")
class HelperTest {

    @Nested
    @DisplayName("isRedirectOrForward(String url) method")
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class isRedirectOrForwardTests {

        @DisplayName("redirect URLs -> true")
        @ParameterizedTest
        @ValueSource(strings = {"redirect:someUrl", "redirect:/", "redirect:"})
        void isRedirectOrForward_withRedirect_isTrue(String url) {
            assertTrue(Helper.isRedirectOrForward(url));
        }

        @DisplayName("forward URLs -> true")
        @ParameterizedTest
        @ValueSource(strings = {"forward:someUrl", "forward:/", "forward:"})
        void isRedirectOrForward_withForward_isTrue(String url) {
            assertTrue(Helper.isRedirectOrForward(url));
        }

        @DisplayName("forward and redirect with uppercase or mixed letters -> false")
        @ParameterizedTest
        @ValueSource(strings = {"Forward:/", "Redirect:/", "FORWARD:/", "REDIRECT:/"})
        void isRedirectOrForward_withNonLowercaseLetters_isFalse(String url) {
            assertFalse(Helper.isRedirectOrForward(url));
        }

        @DisplayName("simple URLs -> false")
        @ParameterizedTest
        @EmptySource
        @ValueSource(strings = {"/someUrl", "/user/home", "/forward", "/redirect"})
        void isRedirectOrForward_withUrl_isFalse(String url) {
            assertFalse(Helper.isRedirectOrForward(url));
        }

        @DisplayName("null -> NullPointerException")
        @ParameterizedTest
        @NullSource
        void isRedirectOrForward_withNull_throwsNPE(String url) {
            assertThrows(NullPointerException.class, () -> Helper.isRedirectOrForward(url));
        }
    }
}
