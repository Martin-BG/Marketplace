package bg.softuni.marketplace.service;

import bg.softuni.marketplace.domain.validation.annotations.composite.user.ValidUserUsername;

public interface SessionService {

    /**
     * End all sessions for user specified by username.
     *
     * @param username the username
     */
    void logoutUser(@ValidUserUsername String username);
}
