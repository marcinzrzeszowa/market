package com.mj.market.app.user.registration;

import com.mj.market.app.user.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RegistrationTokenService {
    private final RegistrationTokenRepository tokenRepository;

    private void saveRegistrationToken(RegistrationToken token){
        tokenRepository.save(token);
    }

    public RegistrationToken createRegistrationToken(User user) {
        String token = UUID.randomUUID().toString();
        RegistrationToken registrationToken = new RegistrationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                user
        );
        saveRegistrationToken(registrationToken);
        return registrationToken;
    }

    public User confirmRegistrationToken(String token) {
        RegistrationToken repoToken = tokenRepository.findByToken(token)
                .orElseThrow(()->new IllegalStateException("Błędny token"));

        if(repoToken.getConfirmAt() != null){
            throw new IllegalStateException("Email już potwierdzony");
        };

        if(LocalDateTime.now().isAfter(repoToken.getExpiresAt())){
            throw new IllegalStateException("Token przedawniony");
        };

        return repoToken.getUser();
    }
}
