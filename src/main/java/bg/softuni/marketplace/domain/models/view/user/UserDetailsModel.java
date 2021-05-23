package bg.softuni.marketplace.domain.models.view.user;

import bg.softuni.marketplace.domain.entities.User;
import bg.softuni.marketplace.domain.enums.Authority;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;
import java.util.UUID;

/**
 * DTO for {@link User} entity.
 * <br>
 * For use by {@link bg.softuni.marketplace.service.UserServiceImpl#loadUserByUsername  loadUserByUsername} method
 * and as Controller method argument
 * <br>
 * (ex. &#64;AuthenticationPrincipal UserDetailsModel user)
 */
@Getter
public class UserDetailsModel implements UserDetails {

    @Serial
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final String username;
    private final String password;
    private final Authority authority;
    private final boolean active;

    public UserDetailsModel(User user) {
        id = user.getId();
        username = user.getUsername();
        password = user.getPassword();
        authority = user.getAuthority();
        active = user.isActive();
    }

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
