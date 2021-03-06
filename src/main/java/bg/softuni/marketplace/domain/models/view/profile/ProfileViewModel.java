package bg.softuni.marketplace.domain.models.view.profile;

import bg.softuni.marketplace.domain.api.Viewable;
import bg.softuni.marketplace.domain.entities.Profile;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Getter
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
public final class ProfileViewModel implements Viewable<Profile>, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private UUID id;

    private String email;

    @Setter
    private String username;
}
