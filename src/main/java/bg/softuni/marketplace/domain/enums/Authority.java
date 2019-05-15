package bg.softuni.marketplace.domain.enums;

import org.springframework.security.core.GrantedAuthority;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Authority implements GrantedAuthority {

    ROOT("root"),
    ADMIN("admin"),
    TRADER("trader"),
    USER("user");

    /**
     * @see #fromId(String)
     */
    private static final Map<String, Authority> STRING_TO_ENUM = Stream
            .of(Authority.values())
            .collect(Collectors.toUnmodifiableMap(Authority::getId, authority -> authority));
    /**
     * @see #getGrantedAuthorities()
     */
    private static final Map<Authority, Set<Authority>> GRANTED_AUTHORITIES = Stream
            .of(Authority.values())
            .collect(Collectors.toMap(
                    k -> k,
                    k -> EnumSet.range(k, Authority.USER),
                    (l, r) -> {
                        throw new IllegalArgumentException("Duplicate keys " + l + "and " + r + ".");
                    },
                    () -> new EnumMap<>(Authority.class)));

    private static final String ROLE_PREFIX = "ROLE_";

    private final String role;
    private final String id;

    Authority(String id) {
        this.id = id;
        role = ROLE_PREFIX + name();
    }

    /**
     * Gets Authority from authority id.
     *
     * @param id the id
     * @return the authority
     * @see bg.softuni.marketplace.domain.converters.AuthorityConverter#convertToEntityAttribute(String)
     */
    public static Authority fromId(String id) {
        return STRING_TO_ENUM.get(id);
    }

    /**
     * Gets authority id, used for persisting.
     *
     * @return the id
     * @see bg.softuni.marketplace.domain.converters.AuthorityConverter#convertToDatabaseColumn(Authority)
     */
    public String getId() {
        return id;
    }

    @Override
    public String getAuthority() {
        return role;
    }

    /**
     * Gets granted authorities set for this authority.
     *
     * @return the granted authorities set
     */
    public Set<Authority> getGrantedAuthorities() {
        return GRANTED_AUTHORITIES.get(this);
    }
}
