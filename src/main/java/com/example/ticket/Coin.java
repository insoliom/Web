package com.example.ticket;

public enum Coin {
    BTCUSDT, ETHUSDT, XRPUSDT, DOGEUSDT;

    public static Coin fromSymbol(String symbol) {
        switch (symbol.toUpperCase()) {
            case "BTCUSDT": return BTCUSDT;
            case "ETHUSDT": return ETHUSDT;
            case "XRPUSDT": return XRPUSDT;
            case "DOGEUSDT": return DOGEUSDT;
            default: throw new IllegalArgumentException("Unknown symbol: " + symbol);
        }
    }
}

