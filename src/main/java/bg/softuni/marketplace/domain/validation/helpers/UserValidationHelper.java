package bg.softuni.marketplace.domain.validation.helpers;

import bg.softuni.marketplace.domain.entities.Role;
import bg.softuni.marketplace.domain.entities.User;
import bg.softuni.marketplace.domain.enums.Authority;

import java.util.Objects;

/**
 * Contains static methods used for validation of {@linkplain User} models
 */
public final class UserValidationHelper {

    private UserValidationHelper() {
    }

    /**
     * @param user {@linkplain User} instance to be validated
     * @return true if provided user doesn't have {@linkplain Authority#ROOT}
     * @throws NullPointerException for {@code null} argument
     */
    public static boolean isNotRoot(User user) {
        return Objects
                .requireNonNull(user)
                .getAuthorities()
                .stream()
                .map(Role::getAuthority)
                .noneMatch(Authority.Role.ROOT::equals);
    }
}
