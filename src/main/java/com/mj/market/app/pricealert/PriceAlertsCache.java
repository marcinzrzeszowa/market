package com.mj.market.app.pricealert;

import java.util.List;
import java.util.Vector;

public interface PriceAlertsCache {
    Vector<PriceAlert> findByIsActive(boolean isActive);
}
