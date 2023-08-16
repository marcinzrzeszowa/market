package com.mj.market.market;

public enum BinanceSymbol {
    BTCUSDT("BTCUSDT"),
    ETHUSDT("ETHUSDT"),
    BNBUSDT("BNBUSDT"),
    MATICUSDT("MATICUSDT");


    //ADA/USDT  	MKR/USDT 	XRP/BTC STX/USDT DOT/USDT FTM/USDT ALGO/USDT ZIL/USDT MANA/USDT
    private String code;

    BinanceSymbol(String code){
        this.code = code;}


    static BinanceSymbol findSymbol(String symbol) {
        for (BinanceSymbol e : values()) {
            if (e.code == symbol) {
                return e;
            }
        }
        return null;
    };
}
