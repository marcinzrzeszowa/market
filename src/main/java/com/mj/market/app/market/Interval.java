package com.mj.market.app.market;

public enum Interval {
    FIVE_MINUTES("5m"),
    HOUR("1h"),
    SIX_HOURS("6h"),
    TWELVE_HOURS("12h"),
    DAY("1d"),
    WEEK("1w"),
    MONTH("1m");

    String code;

    Interval(String code){
        this.code = code;
    }

    public static Interval valueOfLabel(String label) {
        for (Interval e : values()) {
            if (e.code.equals(label)) {
                return e;
            }
        }
        return null;
    }
}
