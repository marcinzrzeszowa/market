package com.mj.market.app.notifier;

import com.mj.market.app.pricealert.PriceAlert;

import java.util.Set;

public interface UserNotifier {
     void notify(Set<PriceAlert> priceAlert);
}
