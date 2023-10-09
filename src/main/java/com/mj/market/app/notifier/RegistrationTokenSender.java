package com.mj.market.app.notifier;

import com.mj.market.app.user.registration.RegistrationToken;

public interface RegistrationTokenSender{
    void sendRegistrationToken(RegistrationToken token);
}
