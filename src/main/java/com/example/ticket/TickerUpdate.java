package com.example.ticket;

import java.time.Instant;

public class TickerUpdate {
    private final Coin coin;
    private final double price;
    private final Instant timestamp;

    public TickerUpdate(Coin coin, double price, long timestamp) {
        this.coin = coin;
        this.price = price;
        this.timestamp = Instant.ofEpochMilli(timestamp);
    }

    public Coin getCoin() { return coin; }
    public double getPrice() { return price; }
    public Instant getTimestamp() { return timestamp; }

    public String toJsonString() {
        return String.format(
                "{\"coin\":\"%s\",\"timestamp\":%d,\"price\":%s}",
                coin.toString(), timestamp.toEpochMilli(), price
        );
    }
}

