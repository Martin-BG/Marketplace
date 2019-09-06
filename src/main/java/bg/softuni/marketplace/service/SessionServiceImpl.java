package bg.softuni.marketplace.service;

import bg.softuni.marketplace.domain.validation.annotations.composite.user.ValidUserUsername;
import lombok.RequiredArgsConstructor;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@RequiredArgsConstructor
@Service
@Validated
@Transactional
public class SessionServiceImpl implements SessionService {

    private final FindByIndexNameSessionRepository<? extends Session> sessions;

    @Override
    public void logoutUser(@ValidUserUsername String username) {
        sessions
                .findByPrincipalName(username)
                .keySet()
                .forEach(sessions::deleteById);
    }
}
