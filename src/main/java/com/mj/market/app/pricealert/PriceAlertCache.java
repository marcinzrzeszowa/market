package com.mj.market.app.pricealert;

import java.util.List;

public interface PriceAlertCache {
    List<PriceAlert> findByIsActive(boolean isActive);
}
