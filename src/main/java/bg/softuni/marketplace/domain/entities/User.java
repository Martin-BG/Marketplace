package bg.softuni.marketplace.domain.entities;

import bg.softuni.marketplace.domain.api.Viewable;
import bg.softuni.marketplace.domain.validation.annotations.composite.user.ValidUserAuthorities;
import bg.softuni.marketplace.domain.validation.annotations.composite.user.ValidUserEmail;
import bg.softuni.marketplace.domain.validation.annotations.composite.user.ValidUserEncryptedPassword;
import bg.softuni.marketplace.domain.validation.annotations.composite.user.ValidUserUsername;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "users")
@NamedQuery(name = "User.findUserEager",
        query = "SELECT u FROM User u LEFT JOIN FETCH u.authorities AS a WHERE u.username = :username")
public class User extends BaseUuidEntity implements UserDetails, Viewable<User> {

    private static final long serialVersionUID = 1L;

    @ValidUserUsername
    @Column(unique = true, nullable = false, updatable = false, length = ValidUserUsername.MAX_LENGTH)
    private String username;

    @ValidUserEncryptedPassword
    @Column(nullable = false, length = ValidUserEncryptedPassword.MAX_LENGTH)
    private String password;

    @ValidUserEmail
    @Column(unique = true, nullable = false, length = ValidUserEmail.MAX_LENGTH)
    private String email;

    @ValidUserAuthorities
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "users_roles",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")})
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
