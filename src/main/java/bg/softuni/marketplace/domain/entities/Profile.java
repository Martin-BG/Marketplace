package bg.softuni.marketplace.domain.entities;

import bg.softuni.marketplace.domain.api.Viewable;
import bg.softuni.marketplace.domain.validation.annotations.composite.user.ValidUserEmail;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Access;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serial;
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

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Mapped by {@link User#getId() User.id}
     * <p>
     * Space optimized BINARY(16) instead of CHAR/VARCHAR(36) database format
     * <p>
     * {@link AccessType#PROPERTY} to avoid LazyInitializationException
     * <p>
     * Private setter to prevent mutability
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

    @Setter
    @ValidUserEmail
    @Column(nullable = false, length = ValidUserEmail.MAX_LENGTH)
    private String email;

    public Profile(User user, String email) {
        this.user = user;
        this.email = email;
    }
}
