package bg.softuni.marketplace.domain.converters;

import bg.softuni.marketplace.domain.enums.Authority;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class AuthorityConverter implements AttributeConverter<Authority, String> {

    @Override
    public String convertToDatabaseColumn(Authority authority) {
        return authority == null ? null : authority.name();
    }

    @Override
    public Authority convertToEntityAttribute(String authority) {
        return Authority.valueOf(authority);
    }
}
