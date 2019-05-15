package bg.softuni.marketplace.domain.models.view.user;

import bg.softuni.marketplace.domain.api.Viewable;
import bg.softuni.marketplace.domain.entities.User;
import bg.softuni.marketplace.domain.enums.Authority;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Getter
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
@AllArgsConstructor
public final class UserViewModel implements Viewable<User>, Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;

    private String username;

    private boolean active;

    @Setter
    private Authority authority;
}
