package bg.softuni.marketplace.domain.entities;

import bg.softuni.marketplace.domain.api.Viewable;
import bg.softuni.marketplace.domain.converters.AuthorityConverter;
import bg.softuni.marketplace.domain.enums.Authority;
import bg.softuni.marketplace.domain.validation.annotations.composite.user.ValidUserAuthority;
import bg.softuni.marketplace.domain.validation.annotations.composite.user.ValidUserEncryptedPassword;
import bg.softuni.marketplace.domain.validation.annotations.composite.user.ValidUserUsername;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_users_username", columnNames = {"username"})
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

    @Setter
    @ValidUserAuthority
    @Convert(converter = AuthorityConverter.class)
    @Column(nullable = false, length = ValidUserAuthority.MAX_LENGTH)
    private Authority authority;

    private boolean active;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authority.getGrantedAuthorities();
    }

    @Override
    public boolean isAccountNonExpired() {
        return active;
    }

    @Override
    public boolean isAccountNonLocked() {
        return active;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return active;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }
}
