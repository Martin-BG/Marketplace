package bg.softuni.marketplace.domain.models.binding.user;

import bg.softuni.marketplace.domain.api.Bindable;
import bg.softuni.marketplace.domain.entities.User;
import bg.softuni.marketplace.domain.validation.annotations.composite.user.ValidUserEmail;
import bg.softuni.marketplace.domain.validation.annotations.composite.user.ValidUserPassword;
import bg.softuni.marketplace.domain.validation.annotations.composite.user.ValidUserUsername;
import bg.softuni.marketplace.domain.validation.annotations.custom.EqualFields;
import bg.softuni.marketplace.domain.validation.annotations.custom.Unique;
import bg.softuni.marketplace.domain.validation.groups.GroupOne;
import bg.softuni.marketplace.domain.validation.groups.GroupTwo;
import bg.softuni.marketplace.repository.UserRepository;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode(of = {"username"})
@NoArgsConstructor
@EqualFields(message = "{user.passwords.not-match}", groups = {GroupOne.class},
        fields = {"password", "confirmPassword"}, forField = "confirmPassword")
public class UserRegisterBindingModel implements Bindable<User>, Serializable {

    private static final long serialVersionUID = 1L;

    @Unique(message = "{user.username.used}", groups = {GroupTwo.class},
            bean = UserRepository.class, method = "countByUsername")
    @ValidUserUsername
    private String username;

    @ValidUserPassword
    private String password;

    @ValidUserPassword
    private String confirmPassword;

    @Unique(message = "{user.email.used}", groups = {GroupTwo.class},
            bean = UserRepository.class, method = "countByEmail")
    @ValidUserEmail
    private String email;
}
