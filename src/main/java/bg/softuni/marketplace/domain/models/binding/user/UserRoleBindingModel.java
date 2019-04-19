package bg.softuni.marketplace.domain.models.binding.user;

import bg.softuni.marketplace.domain.api.Bindable;
import bg.softuni.marketplace.domain.entities.User;
import bg.softuni.marketplace.domain.enums.Authority;
import bg.softuni.marketplace.domain.validation.annotations.composite.role.ValidAuthority;
import bg.softuni.marketplace.domain.validation.annotations.composite.user.ValidUserUsername;
import bg.softuni.marketplace.domain.validation.groups.GroupOne;
import bg.softuni.marketplace.domain.validation.groups.GroupTwo;
import cz.jirutka.validator.spring.SpELAssert;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode(of = {"username"})
@NoArgsConstructor
public class UserRoleBindingModel implements Bindable<User>, Serializable {

    private static final long serialVersionUID = 1L;

    @SpELAssert(message = "{user.update.username.not-found}", groups = GroupTwo.class,
            value = "@userRepository.countByUsername(#this) == 1L")
    @ValidUserUsername
    private String username;

    @SpELAssert(message = "{user.update.role.root}", groups = GroupOne.class,
            value = "#this != T(bg.softuni.marketplace.domain.enums.Authority).ROOT")
    @ValidAuthority
    private Authority authority;
}
