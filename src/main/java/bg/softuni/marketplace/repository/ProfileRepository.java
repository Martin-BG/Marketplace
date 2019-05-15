package bg.softuni.marketplace.repository;

import bg.softuni.marketplace.domain.entities.Profile;
import bg.softuni.marketplace.domain.validation.annotations.composite.user.ValidUserEmail;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;

import javax.persistence.QueryHint;
import java.util.UUID;

import static org.hibernate.jpa.QueryHints.HINT_READONLY;

@Validated
@Repository
public interface ProfileRepository extends GenericRepository<Profile, UUID> {

    /**
     * Check if {@link Profile} with specified email exists.
     * Uses a custom {@link Query @Query}.
     *
     * @param email {@link String}
     * @return {@code true} - {@link Profile} with email found,
     * {@code false} - no {@link Profile} with email found.
     */
    @Query("SELECT (COUNT(p) > 0) FROM Profile p WHERE p.email = :email")
    @QueryHints(@QueryHint(name = HINT_READONLY, value = "true"))
    boolean hasEmail(@ValidUserEmail String email);
}
