package com.mj.market.app.notifier;

import com.mj.market.app.user.registration.Token;

public interface RegistrationTokenSender{
    void sendRegistrationToken(Token token);
}
