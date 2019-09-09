package bg.softuni.marketplace.domain.models.binding.user;

import bg.softuni.marketplace.domain.api.Bindable;
import bg.softuni.marketplace.domain.entities.User;
import bg.softuni.marketplace.domain.validation.annotations.composite.common.ValidId;
import bg.softuni.marketplace.domain.validation.annotations.spel.SpELAssert;
import bg.softuni.marketplace.domain.validation.groups.GroupOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
public class UserStatusBindingModel implements Bindable<User>, Serializable {

    private static final long serialVersionUID = 1L;

    private static final String ROOT_EXPR = "T(bg.softuni.marketplace.domain.enums.Authority).ROOT";

    private static final String ID_VALIDATION = "@userRepository.existsByIdAndAuthorityNot(#this, " + ROOT_EXPR + ")";

    @ValidId
    @SpELAssert(message = "{user.status.invalid-user}", groups = GroupOne.class, value = ID_VALIDATION)
    private UUID id;
}
