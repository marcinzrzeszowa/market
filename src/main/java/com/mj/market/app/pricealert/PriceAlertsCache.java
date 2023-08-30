package com.mj.market.app.pricealert;

import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PriceAlertsCache {
    List<PriceAlert> findByIsActive(boolean isActive);
}
