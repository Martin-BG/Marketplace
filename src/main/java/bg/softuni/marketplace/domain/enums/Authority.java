package bg.softuni.marketplace.domain.enums;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Authority {
    ROOT,
    ADMIN,
    TRADER,
    USER;

    private static final Map<String, Authority> STRING_TO_ENUM = Stream.of(Authority.values())
            .collect(Collectors.toUnmodifiableMap(Authority::asRole, authority -> authority));

    private static final String ROLE_PREFIX = "ROLE_";

    private final String role;

    Authority() {
        this.role = ROLE_PREFIX + name();
    }

    public static Authority fromRole(String role) {
        return role == null ? null : STRING_TO_ENUM.get(role);
    }

    public String asRole() {
        return role;
    }

    public static final class Role {
        public static final String ROOT = ROLE_PREFIX + "ROOT";
        public static final String ADMIN = ROLE_PREFIX + "ADMIN";
        public static final String TRADER = ROLE_PREFIX + "TRADER";
        public static final String USER = ROLE_PREFIX + "USER";

        private Role() {
        }
    }
}
