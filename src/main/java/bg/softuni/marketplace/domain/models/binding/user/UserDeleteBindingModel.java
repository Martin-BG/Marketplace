package bg.softuni.marketplace.domain.models.binding.user;

import bg.softuni.marketplace.domain.api.Bindable;
import bg.softuni.marketplace.domain.entities.User;
import bg.softuni.marketplace.domain.validation.annotations.composite.common.ValidId;
import bg.softuni.marketplace.domain.validation.annotations.spel.SpELAssert;
import bg.softuni.marketplace.domain.validation.groups.GroupOne;
import bg.softuni.marketplace.service.UserServiceImpl;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode(of = {"username"})
@NoArgsConstructor
public class UserDeleteBindingModel implements Bindable<User>, Serializable {

    private static final long serialVersionUID = 1L;

    private static final String ROOT_EXPR = "T(bg.softuni.marketplace.domain.enums.Authority).ROOT";

    private static final String ID_VALIDATION =
            "@userRepository.existsByIdAndActiveAndAuthorityNot(#this, false, " + ROOT_EXPR + ")";

    /**
     * Populated by {@link UserServiceImpl#deleteUser}, for use by UI alerts
     */
    private String username;

    @ValidId
    @SpELAssert(message = "{user.delete.invalid-user}", groups = GroupOne.class, value = ID_VALIDATION)
    private UUID id;
}
