package bg.softuni.marketplace.domain.models.binding.user;

import bg.softuni.marketplace.domain.api.Bindable;
import bg.softuni.marketplace.domain.entities.User;
import bg.softuni.marketplace.domain.models.binding.role.RoleBindingModel;
import bg.softuni.marketplace.domain.validation.annotations.composite.user.ValidUserAuthorities;
import bg.softuni.marketplace.domain.validation.annotations.composite.user.ValidUserEmail;
import bg.softuni.marketplace.domain.validation.annotations.composite.user.ValidUserEncryptedPassword;
import bg.softuni.marketplace.domain.validation.annotations.composite.user.ValidUserUsername;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(of = {"username"})
@NoArgsConstructor
public class UserBindingModel implements Bindable<User>, Serializable {

    private static final long serialVersionUID = 1L;

    @ValidUserUsername
    private String username;

    @ValidUserEncryptedPassword
    private String password;

    @ValidUserEmail
    private String email;

    @ValidUserAuthorities
    private Set<@Valid RoleBindingModel> authorities = new HashSet<>();

    private boolean isAccountNonLocked = true;
    private boolean isAccountNonExpired = true;
    private boolean isCredentialsNonExpired = true;
    private boolean isEnabled = true;

    public UserBindingModel(String username, String email, String password) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
}
