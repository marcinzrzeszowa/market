package com.mj.market.app.pricealert;

import java.util.List;

public interface PriceAlertsCache {
    List<PriceAlert> findByIsActive(boolean isActive);
}
