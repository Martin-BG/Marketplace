package bg.softuni.marketplace.domain.entities;

import bg.softuni.marketplace.domain.api.Viewable;
import bg.softuni.marketplace.domain.converters.AuthorityConverter;
import bg.softuni.marketplace.domain.enums.Authority;
import bg.softuni.marketplace.domain.validation.annotations.composite.role.ValidAuthority;
import bg.softuni.marketplace.domain.validation.annotations.composite.role.ValidRoleAuthority;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
public class Role extends BaseLongEntity implements GrantedAuthority, Viewable<Role> {

    private static final long serialVersionUID = 1L;

    @ValidAuthority
    @Convert(converter = AuthorityConverter.class)
    @Column(unique = true, nullable = false, length = ValidRoleAuthority.MAX_LENGTH)
    private Authority authority;

    @Override
    public String getAuthority() {
        return authority.asRole();
    }
}
