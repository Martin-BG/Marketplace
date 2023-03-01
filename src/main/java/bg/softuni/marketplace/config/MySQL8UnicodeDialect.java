package bg.softuni.marketplace.config;

import org.hibernate.dialect.DatabaseVersion;
import org.hibernate.dialect.MySQLDialect;

/**
 * Custom MySQL dialect settings:
 * <ul>
 * <li>engine: InnoDB</li>
 * <li>charset: utf8mb4</li>
 * <li>collate: utf8mb4_unicode_ci</li>
 * </ul>
 * <p>
 * Set in application.properties:
 * <p>
 * spring.jpa.properties.hibernate.dialect=my.package.MySQL8UnicodeDialect
 */
public class MySQL8UnicodeDialect extends MySQLDialect {

    private static final int MY_SQL_DIALECT_VERSION_MAJOR = 8;

    public MySQL8UnicodeDialect() {
        super(DatabaseVersion.make(MY_SQL_DIALECT_VERSION_MAJOR));
    }

    @Override
    public String getTableTypeString() {
        return " ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci";
    }
}
