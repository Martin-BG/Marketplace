package bg.softuni.marketplace.web.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ViewActionPrefix {

    public static final String REDIRECT = "redirect:";
    public static final String FORWARD = "forward:";
}
