package bg.softuni.marketplace.domain.models.binding.user;

import bg.softuni.marketplace.domain.api.Bindable;
import bg.softuni.marketplace.domain.entities.User;
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
public class UserStatusBindingModel implements Bindable<User>, Serializable {

    private static final long serialVersionUID = 1L;

    @ValidUserUsername
    @SpELAssert(message = "{user.status.username.not-found}", groups = GroupOne.class,
            value = "@userRepository.hasUsername(#this)")
    @SpELAssert(message = "{user.status.username.is-root}", groups = GroupTwo.class,
            value = "not @userRepository.isRoot(#this)")
    private String username;
}
