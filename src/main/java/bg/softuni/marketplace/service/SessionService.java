package bg.softuni.marketplace.service;

import bg.softuni.marketplace.domain.validation.annotations.composite.user.ValidUserUsername;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public interface SessionService {

    /**
     * End all sessions for user specified by username.
     *
     * @param username the username
     */
    void logoutUser(@ValidUserUsername String username);

    /**
     * End all sessions for user specified by user ID.
     *
     * @param id the user ID
     */
    void logoutUser(@NotNull UUID id);
}
