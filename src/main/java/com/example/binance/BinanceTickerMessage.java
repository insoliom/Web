package com.example.binance;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BinanceTickerMessage {

    @JsonProperty("s")
    private String symbol;

    @JsonProperty("c")
    private double price;

    @JsonProperty("E")
    private long eventTime;

    public String getSymbol() {
        return symbol;
    }

    public double getPrice() {
        return price;
    }

    public long getEventTime() {
        return eventTime;
    }
}
