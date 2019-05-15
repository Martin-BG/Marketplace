package bg.softuni.marketplace.repository;

import bg.softuni.marketplace.domain.entities.User;
import bg.softuni.marketplace.domain.enums.Authority;
import bg.softuni.marketplace.domain.validation.annotations.composite.user.ValidUserUsername;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;

import javax.persistence.QueryHint;
import java.util.Optional;
import java.util.UUID;

import static org.hibernate.jpa.QueryHints.HINT_READONLY;

@Validated
@Repository
public interface UserRepository extends GenericRepository<User, UUID> {

    /**
     * Find {@link User} by username.
     *
     * @param username the username
     * @return {@link Optional}<{@link User}>
     */
    Optional<User> findUserByUsername(@ValidUserUsername String username);

    /**
     * Check if {@link User} with specified username exists.
     * Uses a custom {@link Query @Query}.
     *
     * @param username {@link String}
     * @return {@code true} - {@link User} with username found,
     * {@code false} - no {@link User} with username found.
     */
    @Query("SELECT (COUNT(u) > 0) FROM User u WHERE u.username = :username")
    @QueryHints(@QueryHint(name = HINT_READONLY, value = "true"))
    boolean hasUsername(@ValidUserUsername String username);

    /**
     * Check if username exists and has {@link Authority#ROOT} authority.
     * Uses a custom {@link Query @Query}.
     *
     * @param username {@link String}
     * @return {@code true} - username with {@link Authority#ROOT ROOT} authority found,
     * {@code false} - either username not found or not {@link Authority#ROOT ROOT}
     */
    @Query("SELECT (COUNT(u) > 0) FROM User u " +
            "WHERE u.username = :username AND u.authority = bg.softuni.marketplace.domain.enums.Authority.ROOT")
    @QueryHints(@QueryHint(name = HINT_READONLY, value = "true"))
    boolean isRoot(@ValidUserUsername String username);

    /**
     * Activate {@link User} by {@code username}
     *
     * @param username the username
     * @return number of activated {@link User} accounts; (0 if no user with {@code username} found)
     */
    @Modifying
    @Query(value = "UPDATE marketplace_db.users AS u " +
            "SET u.active=true " +
            "WHERE u.username = :username",
            nativeQuery = true)
    int activateUser(@ValidUserUsername String username);

    /**
     * Disable {@link User} by {@code username}
     *
     * @param username the username
     * @return number of disabled {@link User} accounts; (0 if no user with {@code username} found)
     */
    @Modifying
    @Query(value = "UPDATE marketplace_db.users AS u " +
            "SET u.active=false " +
            "WHERE u.username = :username",
            nativeQuery = true)
    int disableUser(@ValidUserUsername String username);
}
