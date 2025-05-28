package com.example.binance;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class BinanceWebSocketMessageSerDe {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public BinanceTickerMessage[] deserializeArray(String json) throws Exception {
        return objectMapper.readValue(json, BinanceTickerMessage[].class);
    }
}

