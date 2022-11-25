package bg.softuni.marketplace.repository;

import bg.softuni.marketplace.domain.entities.User;
import bg.softuni.marketplace.domain.enums.Authority;
import bg.softuni.marketplace.domain.validation.annotations.composite.common.ValidId;
import bg.softuni.marketplace.domain.validation.annotations.composite.user.ValidUserUsername;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotNull;

import java.util.Optional;
import java.util.UUID;

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
     *
     * @param username {@link String}
     * @return {@code true} - {@link User} with username found,
     * {@code false} - no {@link User} with username found.
     */
    boolean existsByUsername(@ValidUserUsername String username);

    /**
     * User exists by id and not authority.
     *
     * @param id        the id
     * @param authority the authority
     * @return true if user with {@code id} and not {@code authority} exists,
     * false if user id not found or user {@link Authority} equals {@code authority}
     */
    boolean existsByIdAndAuthorityNot(@ValidId UUID id, @NotNull Authority authority);

    /**
     * User exists by id, active and not authority.
     *
     * @param id        the id
     * @param active    true for active user, false for inactive user
     * @param authority the authority
     * @return true if user with {@code id}, {@code active} and not {@code authority} exists,
     * false if user id not found, {@code active} not equals user status
     * or user {@link Authority} equals {@code authority}
     */
    boolean existsByIdAndActiveAndAuthorityNot(@ValidId UUID id, boolean active, @NotNull Authority authority);
}
