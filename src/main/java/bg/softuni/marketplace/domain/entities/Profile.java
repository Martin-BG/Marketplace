package bg.softuni.marketplace.domain.entities;

import bg.softuni.marketplace.domain.api.Viewable;
import bg.softuni.marketplace.domain.validation.annotations.composite.user.ValidUserEmail;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * {@link User} profile data.
 * Unidirectional OneToOne relation to {@link User}.
 *
 * @see <a href="https://vladmihalcea.com/the-best-way-to-map-a-onetoone-relationship-with-jpa-and-hibernate/">
 * Vlad Michalcea: The best way to map a @OneToOne relationship with JPA and Hibernate</a>
 */
@Getter
@NoArgsConstructor
@Entity
@Table(
        name = "profiles",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_profile_email", columnNames = {"email"})
        }
)
public class Profile extends BaseEntity<UUID> implements Viewable<Profile> {

    private static final long serialVersionUID = 1L;

    /**
     * Mapped by {@link User#id}
     * <p>
     * Use space optimized BINARY(16) instead of CHAR/VARCHAR(36) database format
     * <p>
     * Use AccessType.PROPERTY for id as best practice to avoid LazyInitializationException
     * <p>
     * Use private setter to prevent mutability
     */
    @Id
    @Column(unique = true, nullable = false, insertable = false, updatable = false, columnDefinition = "BINARY(16)")
    @Access(AccessType.PROPERTY)
    @Setter(AccessLevel.PRIVATE)
    private UUID id;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_users_id"))
    @MapsId
    private User user;

    @ValidUserEmail
    @Column(nullable = false, length = ValidUserEmail.MAX_LENGTH)
    private String email;

    public Profile(User user, String email) {
        this.user = user;
        this.email = email;
    }
}
