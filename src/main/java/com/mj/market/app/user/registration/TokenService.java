package com.mj.market.app.user.registration;

import com.mj.market.app.user.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TokenService {
    private final TokenRepository tokenRepository;

    private void saveToken(Token token){
        tokenRepository.save(token);
    }

    public Token createToken(User user) {
        String token = UUID.randomUUID().toString();
        Token registrationToken = new Token(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                user
        );
        saveToken(registrationToken);
        return registrationToken;
    }

    public User confirmRegistrationToken(String token) {
        Token repoToken = tokenRepository.findByToken(token)
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
