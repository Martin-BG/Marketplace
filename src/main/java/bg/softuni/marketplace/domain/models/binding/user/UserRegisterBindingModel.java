package bg.softuni.marketplace.domain.models.binding.user;

import bg.softuni.marketplace.domain.api.Bindable;
import bg.softuni.marketplace.domain.entities.User;
import bg.softuni.marketplace.domain.validation.annotations.composite.user.*;
import bg.softuni.marketplace.domain.validation.groups.GroupOne;
import bg.softuni.marketplace.domain.validation.groups.GroupTwo;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode(of = {"username"})
@NoArgsConstructor
@MatchPasswords(groups = GroupOne.class)
public class UserRegisterBindingModel implements Bindable<User>, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ValidUserUsername
    @UniqueUserUsername(groups = GroupTwo.class)
    private String username;

    @ValidUserPassword
    private String password;

    @ValidUserPassword
    private String confirmPassword;

    @ValidUserEmail
    @UniqueUserEmail(groups = GroupTwo.class)
    private String email;
}
