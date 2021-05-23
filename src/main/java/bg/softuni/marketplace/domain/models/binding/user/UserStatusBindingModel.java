package bg.softuni.marketplace.domain.models.binding.user;

import bg.softuni.marketplace.domain.api.Bindable;
import bg.softuni.marketplace.domain.entities.User;
import bg.softuni.marketplace.domain.validation.annotations.composite.common.ValidId;
import bg.softuni.marketplace.domain.validation.annotations.composite.user.ModifyUserById;
import bg.softuni.marketplace.domain.validation.groups.GroupOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
public class UserStatusBindingModel implements Bindable<User>, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ValidId
    @ModifyUserById(groups = GroupOne.class)
    private UUID id;
}
