package bg.softuni.marketplace.domain.entities;

import bg.softuni.marketplace.domain.api.Viewable;
import bg.softuni.marketplace.domain.validation.annotations.composite.user.ValidUserAuthorities;
import bg.softuni.marketplace.domain.validation.annotations.composite.user.ValidUserEmail;
import bg.softuni.marketplace.domain.validation.annotations.composite.user.ValidUserEncryptedPassword;
import bg.softuni.marketplace.domain.validation.annotations.composite.user.ValidUserUsername;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.jpa.QueryHints;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor
@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_users_username", columnNames = {"username"}),
                @UniqueConstraint(name = "uk_users_email", columnNames = {"email"})
        }
)
@NamedQuery(
        name = "User.findUserEager",
        query = "SELECT u FROM User u LEFT JOIN FETCH u.authorities AS a WHERE u.username = :username",
        hints = {
                @QueryHint(name = QueryHints.HINT_READONLY, value = "true")
        }
)
public class User extends BaseUuidEntity implements UserDetails, Viewable<User> {

    private static final long serialVersionUID = 1L;

    @ValidUserUsername
    @Column(nullable = false, updatable = false, length = ValidUserUsername.MAX_LENGTH)
    private String username;

    @ValidUserEncryptedPassword
    @Column(nullable = false, length = ValidUserEncryptedPassword.MAX_LENGTH)
    private String password;

    @ValidUserEmail
    @Column(nullable = false, length = ValidUserEmail.MAX_LENGTH)
    private String email;

    @ValidUserAuthorities
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "users_roles",
            joinColumns = {
                    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_users_roles_users"))},
            inverseJoinColumns = {
                    @JoinColumn(name = "role_id", foreignKey = @ForeignKey(name = "fk_users_roles_roles"))})
    private Set<Role> authorities = new HashSet<>();

    private boolean isAccountNonLocked = true;
    private boolean isAccountNonExpired = true;
    private boolean isCredentialsNonExpired = true;
    private boolean isEnabled = true;

    public User(String username, String password, String email, Collection<Role> authorities) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.authorities.addAll(authorities);
    }
}
