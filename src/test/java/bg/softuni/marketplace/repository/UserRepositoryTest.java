package bg.softuni.marketplace.repository;

import bg.softuni.marketplace.domain.validation.annotations.composite.user.ValidUserUsername;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import jakarta.validation.ConstraintViolationException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;

@SpringBootTest
@DisplayName("repository/UserRepository.class")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MessageSource messageSource;

    @Nested
    @DisplayName("findUserByUsername(String username) method")
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class findUserByUsernameTests {

        private final String TOO_LONG_USER_NAME = "Too long user name is used here...";
        private final String VALID_USER_NAME = "John Smith";
        private final String blankMessage = messageSource
                .getMessage("user.username.blank", null, LocaleContextHolder.getLocale());
        private final String lengthMessage = messageSource
                .getMessage("user.username.length", null, LocaleContextHolder.getLocale())
                .replaceAll("\\{min}", Integer.toString(ValidUserUsername.MIN_LENGTH))
                .replaceAll("\\{max}", Integer.toString(ValidUserUsername.MAX_LENGTH));

        @DisplayName("valid username -> not throwing")
        @Test
        void findUserByUsernameWithValidString() {
            assertThatNoException()
                    .isThrownBy(() -> userRepository.findUserByUsername(VALID_USER_NAME));
        }

        @DisplayName("null username -> throws")
        @Test
        void findUserByUsernameWithNull() {
            assertThatExceptionOfType(ConstraintViolationException.class)
                    .isThrownBy(() -> userRepository.findUserByUsername(null))
                    .withMessageContainingAll(blankMessage);
        }

        @DisplayName("empty username -> throws")
        @Test
        void findUserByUsernameWithEmptyString() {
            assertThatExceptionOfType(ConstraintViolationException.class)
                    .isThrownBy(() -> userRepository.findUserByUsername(""))
                    .withMessageContainingAll(blankMessage, lengthMessage);
        }

        @DisplayName("too long username -> throws")
        @Test
        void findUserByUsernameWithTooLongString() {
            assertThatExceptionOfType(ConstraintViolationException.class)
                    .isThrownBy(() -> userRepository.findUserByUsername(TOO_LONG_USER_NAME))
                    .withMessageContaining(lengthMessage);
        }
    }
}
