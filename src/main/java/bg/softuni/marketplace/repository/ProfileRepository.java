package bg.softuni.marketplace.repository;

import bg.softuni.marketplace.domain.entities.Profile;
import bg.softuni.marketplace.domain.validation.annotations.composite.common.ValidId;
import bg.softuni.marketplace.domain.validation.annotations.composite.user.ValidUserEmail;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Validated
@Repository
public interface ProfileRepository extends GenericRepository<Profile, UUID> {

    boolean existsByEmail(@ValidUserEmail String email);

    boolean existsByEmailAndId(@ValidUserEmail String email, @ValidId UUID id);
}
