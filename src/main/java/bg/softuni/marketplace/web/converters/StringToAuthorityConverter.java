package bg.softuni.marketplace.web.converters;

import bg.softuni.marketplace.domain.enums.Authority;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class StringToAuthorityConverter implements Converter<String, Authority> {

    @Override
    public Authority convert(String authority) {
        return Authority.valueOf(Objects.requireNonNull(authority));
    }
}
