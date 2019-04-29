package bg.softuni.marketplace.domain.models.binding.user;

import bg.softuni.marketplace.domain.api.Bindable;
import bg.softuni.marketplace.domain.entities.User;
import bg.softuni.marketplace.domain.enums.Authority;
import bg.softuni.marketplace.domain.validation.annotations.composite.role.ValidAuthority;
import bg.softuni.marketplace.domain.validation.annotations.composite.user.ValidUserUsername;
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
public class UserDeleteBindingModel implements Bindable<User>, Serializable {

    private static final long serialVersionUID = 1L;

    @ValidUserUsername
    @SpELAssert(message = "{user.delete.username.not-found}", groups = GroupOne.class,
            value = "@userRepository.hasUsername(#this)")
    @SpELAssert(message = "{user.delete.username.is-root}", groups = GroupTwo.class,
            value = "not @userRepository.hasAuthority(#this, T(bg.softuni.marketplace.domain.enums.Authority).ROOT)")
    private String username;

    @ValidAuthority
    @SpELAssert(message = "{user.delete.authority.is-root}", groups = GroupOne.class,
            value = "#this != T(bg.softuni.marketplace.domain.enums.Authority).ROOT")
    private Authority authority;
}
