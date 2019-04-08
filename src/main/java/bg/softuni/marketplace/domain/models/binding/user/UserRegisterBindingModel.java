package bg.softuni.marketplace.domain.models.binding.user;

import bg.softuni.marketplace.domain.api.Bindable;
import bg.softuni.marketplace.domain.entities.User;
import bg.softuni.marketplace.domain.validation.annotations.composite.user.ValidUserEmail;
import bg.softuni.marketplace.domain.validation.annotations.composite.user.ValidUserPassword;
import bg.softuni.marketplace.domain.validation.annotations.composite.user.ValidUserUsername;
import bg.softuni.marketplace.domain.validation.annotations.custom.EqualFields;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode(of = {"username"})
@NoArgsConstructor
@EqualFields(message = "{user.password.not-match}", fields = {"password", "confirmPassword"})
public class UserRegisterBindingModel implements Bindable<User>, Serializable {

    private static final long serialVersionUID = 1L;

    @ValidUserUsername
    private String username;

    @ValidUserPassword
    private String password;

    @ValidUserPassword
    private String confirmPassword;

    @ValidUserEmail
    private String email;
}
