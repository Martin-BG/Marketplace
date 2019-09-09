package bg.softuni.marketplace.domain.models.binding.user;

import bg.softuni.marketplace.domain.api.Bindable;
import bg.softuni.marketplace.domain.entities.User;
import bg.softuni.marketplace.domain.validation.annotations.composite.user.ValidUserEmail;
import bg.softuni.marketplace.domain.validation.annotations.composite.user.ValidUserPassword;
import bg.softuni.marketplace.domain.validation.annotations.composite.user.ValidUserUsername;
import bg.softuni.marketplace.domain.validation.annotations.custom.EqualFields;
import bg.softuni.marketplace.domain.validation.annotations.spel.SpELAssert;
import bg.softuni.marketplace.domain.validation.groups.GroupOne;
import bg.softuni.marketplace.domain.validation.groups.GroupTwo;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode(of = {"username"})
@NoArgsConstructor
@EqualFields(message = "{user.passwords.not-match}", groups = GroupOne.class,
        fields = {"password", "confirmPassword"}, forField = "confirmPassword")
public class UserRegisterBindingModel implements Bindable<User>, Serializable {

    private static final long serialVersionUID = 1L;

    @ValidUserUsername
    @SpELAssert(message = "{user.username.used}", groups = GroupTwo.class,
            value = "not @userRepository.existsByUsername(#this)")
    private String username;

    @ValidUserPassword
    private String password;

    @ValidUserPassword
    private String confirmPassword;

    @ValidUserEmail
    @SpELAssert(message = "{user.email.used}", groups = GroupTwo.class,
            value = "not @profileRepository.existsByEmail(#this)")
    private String email;
}
