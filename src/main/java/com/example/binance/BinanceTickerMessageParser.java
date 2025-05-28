package com.example.binance;

import com.example.ticket.TickerUpdate;
import com.example.ticket.Coin;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BinanceTickerMessageParser {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static TickerUpdate parse(String msg) throws Exception {
        JsonNode root = mapper.readTree(msg);
        JsonNode data = root.get("data");
        String symbol = data.get("s").asText();
        double price = Double.parseDouble(data.get("c").asText());
        long timestamp = data.get("E").asLong();
        Coin coin = Coin.valueOf(symbol.replace("USDT", ""));
        return new TickerUpdate(coin, price, timestamp);
    }
}
