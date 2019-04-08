package bg.softuni.marketplace.domain.models.binding.role;

import bg.softuni.marketplace.domain.api.Bindable;
import bg.softuni.marketplace.domain.entities.Role;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class RoleBindingModel implements Bindable<Role>, Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    private Long id;
}
