package bg.softuni.marketplace.domain.models.projection.user;

import bg.softuni.marketplace.domain.api.Viewable;
import bg.softuni.marketplace.domain.entities.User;

public interface UserUsernameProjection extends Viewable<User> {

    String getUsername();
}
